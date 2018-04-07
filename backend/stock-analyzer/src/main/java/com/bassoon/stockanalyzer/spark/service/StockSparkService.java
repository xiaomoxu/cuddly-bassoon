package com.bassoon.stockanalyzer.spark.service;

import com.bassoon.stockanalyzer.domain.Stock;
import com.bassoon.stockanalyzer.service.JsonUtils;
import com.bassoon.stockanalyzer.spark.config.SparkRepository;
import com.bassoon.stockanalyzer.spark.model.StockListWrapper;
import com.bassoon.stockanalyzer.spark.model.StockScoreValue;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class StockSparkService {
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

    public List<StockScoreValue> getBasicStockList() {
        Dataset<Row> dataset = sparkRepository.getDatasetByTable("stock_basics");
        dataset.createOrReplaceTempView("stock_basics_temp_view");
        dataset = dataset.sqlContext().sql("select code , name from stock_basics_temp_view");
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<StockScoreValue> ds = dataset.map(new MapFunction<Row, StockScoreValue>() {
            @Override
            public StockScoreValue call(Row row) throws Exception {
                String code = (String) row.getAs("code");
                String name = (String) row.getAs("name");
                StockScoreValue node = new StockScoreValue();
                if (code != null || !code.equals("")) {
                    node.setCode(code);
                    node.setName(name);
                }
                return node;
            }
        }, Encoders.bean(StockScoreValue.class));
        return ds.collectAsList();
    }
}
