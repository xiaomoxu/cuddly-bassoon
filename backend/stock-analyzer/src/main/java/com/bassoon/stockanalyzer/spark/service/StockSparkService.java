package com.bassoon.stockanalyzer.spark.service;

import com.bassoon.stockanalyzer.domain.Stock;
import com.bassoon.stockanalyzer.service.JsonUtils;
import com.bassoon.stockanalyzer.spark.config.SparkRepository;
import com.bassoon.stockanalyzer.spark.model.BasicStockValue;
import com.bassoon.stockanalyzer.spark.model.StockIndexValue;
import com.bassoon.stockanalyzer.spark.model.StockListWrapper;
import com.bassoon.stockanalyzer.spark.model.StockScoreValue;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.List;

@Service
public class StockSparkService implements Serializable{
    @Autowired
    private SparkRepository sparkRepository;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redisport}")
    private int port;

    public StockListWrapper getStocks(boolean reload) {
        Jedis jedis = new Jedis(host, port);
        if (!reload) {
            //先从缓存取
            String json = jedis.get("stock");
            if (json != null && !json.equals("")) {
                StockListWrapper result = JsonUtils.jsonToObject(json, null, StockListWrapper.class);
                if (result != null) {
                    return result;
                }
            }
        }
        Dataset<Stock> dataset = sparkRepository.getDatasetByTable("stock").as(Encoders.bean(Stock.class));
        dataset.persist(StorageLevel.MEMORY_AND_DISK());
        List<Stock> stockList = dataset.collectAsList();
        sparkRepository.stopAndClose();
        StockListWrapper stocks = new StockListWrapper();
        stocks.setStockList(stockList);
        jedis.set("stock", JsonUtils.objectToJson(stocks));
        return stocks;
    }

    public List<BasicStockValue> getBasicStockList(boolean reload) {
        Jedis jedis = new Jedis(host, port);
        if (!reload) {
            //先从缓存取
            String json = jedis.get("basics_stock");
            if (json != null && !json.equals("")) {
                List<BasicStockValue> result = JsonUtils.jsonToObject(json, List.class, BasicStockValue.class);
                if (result != null) {
                    return result;
                }
            }
        }
        Dataset<Row> dataset = sparkRepository.getDatasetByTable("stock_basics");
        dataset.createOrReplaceTempView("stock_basics_temp_view");
        dataset = dataset.sqlContext().sql("select code , name from stock_basics_temp_view");
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<BasicStockValue> ds = dataset.map(new MapFunction<Row, BasicStockValue>() {
            @Override
            public BasicStockValue call(Row row) throws Exception {
                String code = (String) row.getAs("code");
                String name = (String) row.getAs("name");
                BasicStockValue node = new BasicStockValue();
                if (code != null || !code.equals("")) {
                    node.setCode(code);
                    node.setName(name);
                }
                return node;
            }
        }, Encoders.bean(BasicStockValue.class));
        List<BasicStockValue> _ds = ds.collectAsList();
        jedis.set("basics_stock", JsonUtils.objectToJson(_ds));
        return _ds;
    }

    public List<StockIndexValue> getStockIndexValueToday(String... codes) {
        Dataset<StockIndexValue> dataset = sparkRepository.getDatasetByTable("stock_index").as(Encoders.bean(StockIndexValue.class));
        dataset = dataset.filter(new FilterFunction<StockIndexValue>() {
            @Override
            public boolean call(StockIndexValue stockIndexValue) throws Exception {
                for (String code : codes) {
                    if (stockIndexValue.getCode().equals(code)) {
                        return true;
                    }
                }
                return false;
            }
        });
        List<StockIndexValue> indexList = dataset.collectAsList();
        sparkRepository.stopAndClose();
        return indexList;
    }
}
