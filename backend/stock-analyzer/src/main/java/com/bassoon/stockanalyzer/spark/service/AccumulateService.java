package com.bassoon.stockanalyzer.spark.service;

import com.bassoon.stockanalyzer.service.JsonUtils;
import com.bassoon.stockanalyzer.spark.config.SparkRepository;
import com.bassoon.stockanalyzer.spark.model.*;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;

import static org.apache.spark.sql.functions.col;
import org.apache.spark.storage.StorageLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import scala.Int;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AccumulateService implements Serializable{
    //@Autowired
    //private transient JavaSparkContext javaSparkContext;
    @Autowired
    private SparkRepository sparkRepository;

    @Autowired
    private transient SparkSession spark;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redisport}")
    private int port;

    private static double start_memory = 50000;

    public List<AccumulateValue> getAccumulatedData(String year, boolean reload) {
        List<AccumulateValue> accum_sum = new ArrayList<>();
        String redis_key = String.format("getAccumulatedData_%s",year);

        Jedis jedis = new Jedis(host, port);
        if (!reload) {
            String json = jedis.get(redis_key);
            if (json != null && !json.equals("")) {
                System.out.println("------Get date from redis by key " + redis_key);
                return JsonUtils.jsonToObject(json,ArrayList.class,AccumulateValue.class);
            }
        }
        //init summary list
        for(int m = 1; m <= 12; m++){
            accum_sum.add(new AccumulateValue(String.format("%s-%02d-01",year,m),0));
        }
        //get selected stocks list
        List<StockScoreValue> selects = getQualityStocks(year, false);

        //get stock date and summary
        for(int i = 0; i < selects.size(); i++) {
            boolean got = false;
            List<AccumulateValue> accum = getAccumulateOfYear(selects.get(i).getCode(), year);
            //add to summary
            for(int m1 = 0; m1 < 12; m1++){
                got = false;
                String cd = accum_sum.get(m1).getDate();
                for(int m2 = 0; m2 < accum.size(); m2++){
                    if(cd.equals(accum.get(m2).getDate())){
                        double t = accum_sum.get(m1).getValue() + accum.get(m2).getValue();
                        accum_sum.set(m1,new AccumulateValue(accum_sum.get(m1).getDate(),t));
                        got = true;
                        break;
                    }
                }
                //cannot find a value in current mouth
                if(got == false && m1 > 1){
                    System.out.println(String.format("------Cannot find mouth data %s %s", cd,selects.get(i).getCode()));
                    accum_sum.set(m1,new AccumulateValue(accum_sum.get(m1).getDate(),
                            accum_sum.get(m1-1).getValue()));
                }
            }
        }
        //save to redis
        jedis.set(redis_key, JsonUtils.objectToJson(accum_sum));

        return accum_sum;
    }


    private List<AccumulateValue> getAccumulateOfYear(String code, String year){
        double close = 0;
        double pre_close = 0;
        double total_memory = start_memory;
        String table_stock = "stock_k_data_none";

        List<AccumulateValue> y_list = new ArrayList<>();

        //Get the selected stock
        Dataset<Row> ds_stock = sparkRepository.getDatasetByTable(table_stock)
                .filter(col("code").equalTo(code))
                .filter(col("date").geq(String.format("%s-01-01",year)))
                .filter(col("date").leq(String.format("%s-12-31",year)))
                .select(col("date"),col("open"),col("close"));
        ds_stock = ds_stock.persist(StorageLevel.MEMORY_AND_DISK());

        //Get 1st day from 1 to 12 mouth
        for (int mouth = 1; mouth <= 12; mouth++){
            String s_d = String.format("%s-%02d-01",year,mouth);
            String e_d = String.format("%s-%02d-%02d",year,mouth,getLastDayOfMouth(s_d));
            //Using like is not a good choice
            Dataset<Row> m_ds = ds_stock.filter(col("date").geq(s_d))
                    .filter(col("date").leq(e_d))
                    //.like(String.format("%s-%02d-%%",year,mouth)))
                    .sort(col("date").desc());
            //m_ds.show();
            if (m_ds.count() > 0) {
                close = m_ds.first().getDouble(2);
                //m_ds.
                if(pre_close == 0){
                    pre_close = close;
                }
                else{
                    total_memory = total_memory * (1+ (close - pre_close)/pre_close);
                    pre_close = close;
                }
                y_list.add(new AccumulateValue(String.format("%s-%02d-01", year,mouth), total_memory));
            }
            else {
                System.out.println(String.format("-----cannot find stock %s on %s",code,e_d));
            }
        }

        return y_list;
    }

    public List<StockScoreValue> getQualityStocks(String year,boolean reload)
    {
        int col_code = 1;
        int col_name = 2;
        int col_roe = 3;//净资产收益率 来源于 stock_profit_data  roe > 0  按照4个季度总和计算
        int col_grossProfitRatio;//毛利率 stock_profit_data  roe  current > last year 按照4个季度中和
        int col_cashFlowRatio = 4;//现金流量比率  来源于stock_cashflow_data  cashflowratio (Operating Cash Flow Ratio)  > 0 按照4个季度总和
        int col_debtCurrentRatio = 5;//* current > last year 4个季度总和 stock_debtpay_data
        int col_epsg = 6;//每股收益增长率 current > last year > 0 4季度总和    stock_growth_data
        int col_currentAssetTurnover = 7; //资产周转率 stock_operation_data  current > last year 按照4个季度总和
        int col_max = 9;
        String pre_year = String.valueOf(Integer.parseInt(year) - 1);
        String table_stock = "stock_summing";

        String redis_key = String.format("getQualityStocks_%s",year);
        Jedis jedis = new Jedis(host, port);
        //test
        reload = false;
        if (!reload) {
            String json = jedis.get(redis_key);
            if (json != null && !json.equals("")) {
                System.out.println("------Get date from redis by key " + redis_key);
                return JsonUtils.jsonToObject(json,ArrayList.class,StockScoreValue.class);
            }
        }

        //Get the selected stock
        Dataset<Row> ds = sparkRepository.getDatasetByTable(table_stock);
        Dataset<Row> ds_cur = ds.filter(col("year").equalTo(year));
        Dataset<Row> ds_pre = ds.filter(col("year").equalTo(pre_year));

        Dataset<Row> ds_all = ds_cur.join(ds_pre,ds_cur.col("code")
                .equalTo(ds_pre.col("code")),"inner");
        ds_all.show();

        Encoder<StockScoreValue> stockQualityEncoder = Encoders.bean(StockScoreValue.class);
        Dataset<StockScoreValue> ds_quality = ds_all.map(new MapFunction<Row, StockScoreValue>() {
            @Override
            public StockScoreValue call(Row row) throws Exception {
                StockScoreValue stockScoreValue = new StockScoreValue();
                stockScoreValue.setCode(row.getString(col_code));
                stockScoreValue.setName(row.getString(col_name));
                FilterCondition cur_filter = new FilterCondition();
                FilterCondition pre_filter = new FilterCondition();
                cur_filter.setRoe(row.getDouble(col_roe));
                cur_filter.setCashFlowRatio(row.getDouble(col_cashFlowRatio));
                cur_filter.setDebtCurrentRatio(row.getDouble(col_debtCurrentRatio));
                cur_filter.setEpsg(row.getDouble(col_epsg));
                cur_filter.setCurrentAssetTurnover(row.getDouble(col_currentAssetTurnover));
                pre_filter.setRoe(row.getDouble(col_roe + col_max));
                pre_filter.setCashFlowRatio(row.getDouble(col_cashFlowRatio + col_max));
                pre_filter.setDebtCurrentRatio(row.getDouble(col_debtCurrentRatio + col_max));
                pre_filter.setEpsg(row.getDouble(col_epsg + col_max));
                pre_filter.setCurrentAssetTurnover(row.getDouble(col_currentAssetTurnover + col_max));

                int quality = 0;
                if (row.getDouble(col_roe) > 0)
                    quality += 1;
                if (row.getDouble(col_cashFlowRatio) > 0)
                    quality += 1;
                if (row.getDouble(col_debtCurrentRatio) > row.getDouble(col_debtCurrentRatio + col_max))
                    quality += 1;
                if (row.getDouble(col_epsg) > 0)
                    quality += 1;
                if (row.getDouble(col_epsg) > row.getDouble(col_epsg + col_max))
                    quality += 1;
                if (row.getDouble(col_currentAssetTurnover) > row.getDouble(col_currentAssetTurnover + col_max))
                    quality += 1;
                stockScoreValue.setScore(quality);
                stockScoreValue.setFilterCondition(cur_filter);
                stockScoreValue.setPreviousFilterCondition(pre_filter);
                return stockScoreValue;
            }
        },stockQualityEncoder);
        ds_quality.show();

        Dataset<StockScoreValue> ds_sort = ds_quality.sort(col("score").desc());
        ds_sort.show();
        List<StockScoreValue> q_list = ds_sort.takeAsList(20);

        for(int i =0 ;i < q_list.size();i++){
            System.out.println(q_list.get(i).getCode() + "--" + q_list.get(i).getScore());
        }

        //save to redis
        jedis.set(redis_key, JsonUtils.objectToJson(q_list));

        return q_list;
    }

    private static int getLastDayOfMouth(String date){
        LocalDate convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-d"));
        LocalDate d = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));

        //System.out.println(d);
        //System.out.println(d.getDayOfMonth());
        return d.getDayOfMonth();
    }
}
