package com.bassoon.stockanalyzer.spark.service;

import com.bassoon.stockanalyzer.mapper.ScoringMapper;
import com.bassoon.stockanalyzer.spark.config.SparkRepository;
import com.bassoon.stockanalyzer.spark.model.BasicStockValue;
import com.bassoon.stockanalyzer.spark.model.StockScoreValue;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ScorePolicyService {
    @Autowired
    private StockSparkService stockSparkService;
    @Autowired
    private transient ScoringMapper scoringMapper;
    @Autowired
    private SparkRepository sparkRepository;

    /**
     * 先用单线程本地常规方法解决这个需求
     * 之后我会再用spark重新计算
     *
     * @param year
     * @return
     */
    public List<StockScoreValue> scoring(int year) {
        List<BasicStockValue> basicStockList = stockSparkService.getBasicStockList(false);
        List<StockScoreValue> scoreValueList = new LinkedList<StockScoreValue>();
        for (BasicStockValue basicStockValue : basicStockList) {
            StockScoreValue stockScoreValue = new StockScoreValue();
            stockScoreValue.setCode(basicStockValue.getCode());
            stockScoreValue.setName(basicStockValue.getName());
            try {
                this.scoringForROE(stockScoreValue, year);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return scoreValueList;
    }

    public StockScoreValue scoringForROE(StockScoreValue stockScoreValue, int year) throws Exception {
        List<Double> roeArray = scoringMapper.getRoeByYear(stockScoreValue.getCode(), year);
        List<Double> lastyear_roeArray = scoringMapper.getRoeByYear(stockScoreValue.getCode(), year - 1);
        try {
            Double total_Roe = roeArray.stream().mapToDouble(p -> p.doubleValue()).sum();
            Double total_lastyear_roeArray = lastyear_roeArray.stream().mapToDouble(p -> p.doubleValue()).sum();
            if (total_Roe > 0) {
                stockScoreValue.setScore(1);
                if (total_Roe > total_lastyear_roeArray) {
                    stockScoreValue.setScore(1);
                }
            }
        } catch (Exception e) {
            throw new Exception("exception for ROE: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
        }
        return stockScoreValue;
    }

//    public List<StockScoreValue> scoring(int year) {
//        Dataset<Row> dataset = sparkRepository.getDatasetByTable("stock_basics");
//        dataset.createOrReplaceTempView("stock_basics_temp_view");
//        dataset = dataset.sqlContext().sql("select code , name from stock_basics_temp_view");
//        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
//        Dataset<BasicStockValue> ds = dataset.map(new MapFunction<Row, BasicStockValue>() {
//            @Override
//            public BasicStockValue call(Row row) throws Exception {
//                String code = (String) row.getAs("code");
//                String name = (String) row.getAs("name");
//                BasicStockValue node = new BasicStockValue();
//                if (code != null || !code.equals("")) {
//                    node.setCode(code);
//                    node.setName(name);
//                    List<Double> roeArray = scoringMapper.getRoeInProfitByYear(code, year);
//                    Double total_Roe = roeArray.stream().mapToDouble(p -> p.doubleValue()).sum();
//                    System.out.println(code + " " + name + " " + total_Roe);
//
//                }
//                return node;
//            }
//        }, Encoders.bean(BasicStockValue.class));
//        return null;
//    }
}
