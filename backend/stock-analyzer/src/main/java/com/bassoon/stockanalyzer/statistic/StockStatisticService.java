package com.bassoon.stockanalyzer.statistic;

import com.bassoon.stockanalyzer.policy.TwoEightNode;
import com.bassoon.stockanalyzer.service.JsonUtils;
import com.bassoon.stockanalyzer.spark.SparkRepository;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.storage.StorageLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.List;

@Service
public class StockStatisticService implements Serializable {
    @Autowired
    private SparkRepository sparkRepository;

    public List<StockValue> staticStockByKey(String key , boolean reload) {
        if (key.equals("city")) {
            return this.staticStockWithCity(reload);
        } else if (key.equals("province")) {
            return this.staticStockWithProvince(reload);
        } else if (key.equals("industry")) {
            return this.staticStockWithIndustry(reload);
        } else if (key.equals("concept")) {
            return this.staticStockWithConcept(reload);
        }
        return null;
    }

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redisport}")
    private int port;

    public List<StockValue> staticStockWithCity(boolean reload) {
        Jedis jedis = new Jedis(host, port);
        if (!reload) {
            //先从缓存取
//            List<TwoEightNode> result = (List<TwoEightNode>) this.sparkJedisRepository.get("tow_eight", List.class, TwoEightNode.class);
            String json = jedis.get("stock_city");
            if (json != null && !json.equals("")) {
                List<StockValue> result = JsonUtils.jsonToObject(json, List.class, StockValue.class);
                if (result != null) {
                    return result;
                }
            }
        }
        String table = "stock";
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<Row> ds = dataset.groupBy(dataset.col("region").as("key")).count();
        Dataset<StockValue> _ds = ds.as(Encoders.bean(StockValue.class));
        jedis.set("stock_city", JsonUtils.objectToJson(_ds.collectAsList()));
        return _ds.collectAsList();
    }

    public List<StockValue> staticStockWithIndustry(boolean reload) {
        Jedis jedis = new Jedis(host, port);
        if (!reload) {
            //先从缓存取
//            List<TwoEightNode> result = (List<TwoEightNode>) this.sparkJedisRepository.get("tow_eight", List.class, TwoEightNode.class);
            String json = jedis.get("stock_industry");
            if (json != null && !json.equals("")) {
                List<StockValue> result = JsonUtils.jsonToObject(json, List.class, StockValue.class);
                if (result != null) {
                    return result;
                }
            }
        }
        String table = "stock_basics";
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<Row> ds = dataset.groupBy(dataset.col("industry").as("key")).count();
        Dataset<StockValue> _ds = ds.as(Encoders.bean(StockValue.class));
        jedis.set("stock_industry", JsonUtils.objectToJson(_ds.collectAsList()));
        return _ds.collectAsList();
    }

    public List<StockValue> staticStockWithConcept(boolean reload) {
        Jedis jedis = new Jedis(host, port);
        if (!reload) {
            //先从缓存取
//            List<TwoEightNode> result = (List<TwoEightNode>) this.sparkJedisRepository.get("tow_eight", List.class, TwoEightNode.class);
            String json = jedis.get("stock_concept");
            if (json != null && !json.equals("")) {
                List<StockValue> result = JsonUtils.jsonToObject(json, List.class, StockValue.class);
                if (result != null) {
                    return result;
                }
            }
        }
        String table = "concept";
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<Row> ds = dataset.groupBy(dataset.col("c_name").as("key")).count();
        Dataset<StockValue> _ds = ds.as(Encoders.bean(StockValue.class));
        jedis.set("stock_concept", JsonUtils.objectToJson(_ds.collectAsList()));
        return _ds.collectAsList();
    }

    public List<StockValue> staticStockWithProvince(boolean reload) {
        Jedis jedis = new Jedis(host, port);
        if (!reload) {
            //先从缓存取
//            List<TwoEightNode> result = (List<TwoEightNode>) this.sparkJedisRepository.get("tow_eight", List.class, TwoEightNode.class);
            String json = jedis.get("stock_province");
            if (json != null && !json.equals("")) {
                List<StockValue> result = JsonUtils.jsonToObject(json, List.class, StockValue.class);
                if (result != null) {
                    return result;
                }
            }
        }
        String table = "area";
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<Row> ds = dataset.groupBy(dataset.col("area").as("key")).count();
        Dataset<StockValue> _ds = ds.as(Encoders.bean(StockValue.class));
        jedis.set("stock_province", JsonUtils.objectToJson(_ds.collectAsList()));
        return _ds.collectAsList();
    }
}
