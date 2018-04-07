package com.bassoon.stockanalyzer.spark.service;

import com.bassoon.stockanalyzer.service.JsonUtils;
import com.bassoon.stockanalyzer.spark.config.SparkRepository;
import com.bassoon.stockanalyzer.spark.model.StockStatisticsValue;
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

    public List<StockStatisticsValue> staticStockByKey(String key , boolean reload) {
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

    public List<StockStatisticsValue> staticStockWithCity(boolean reload) {
        Jedis jedis = new Jedis(host, port);
        if (!reload) {
            //先从缓存取
//            List<AlternateValue> result = (List<AlternateValue>) this.sparkJedisRepository.get("tow_eight", List.class, AlternateValue.class);
            String json = jedis.get("stock_city");
            if (json != null && !json.equals("")) {
                List<StockStatisticsValue> result = JsonUtils.jsonToObject(json, List.class, StockStatisticsValue.class);
                if (result != null) {
                    return result;
                }
            }
        }
        String table = "stock";
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<Row> ds = dataset.groupBy(dataset.col("region").as("key")).count();
        Dataset<StockStatisticsValue> _ds = ds.as(Encoders.bean(StockStatisticsValue.class));
        jedis.set("stock_city", JsonUtils.objectToJson(_ds.collectAsList()));
        return _ds.collectAsList();
    }

    public List<StockStatisticsValue> staticStockWithIndustry(boolean reload) {
        Jedis jedis = new Jedis(host, port);
        if (!reload) {
            //先从缓存取
//            List<AlternateValue> result = (List<AlternateValue>) this.sparkJedisRepository.get("tow_eight", List.class, AlternateValue.class);
            String json = jedis.get("stock_industry");
            if (json != null && !json.equals("")) {
                List<StockStatisticsValue> result = JsonUtils.jsonToObject(json, List.class, StockStatisticsValue.class);
                if (result != null) {
                    return result;
                }
            }
        }
        String table = "stock_basics";
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<Row> ds = dataset.groupBy(dataset.col("industry").as("key")).count();
        Dataset<StockStatisticsValue> _ds = ds.as(Encoders.bean(StockStatisticsValue.class));
        jedis.set("stock_industry", JsonUtils.objectToJson(_ds.collectAsList()));
        return _ds.collectAsList();
    }

    public List<StockStatisticsValue> staticStockWithConcept(boolean reload) {
        Jedis jedis = new Jedis(host, port);
        if (!reload) {
            //先从缓存取
//            List<AlternateValue> result = (List<AlternateValue>) this.sparkJedisRepository.get("tow_eight", List.class, AlternateValue.class);
            String json = jedis.get("stock_concept");
            if (json != null && !json.equals("")) {
                List<StockStatisticsValue> result = JsonUtils.jsonToObject(json, List.class, StockStatisticsValue.class);
                if (result != null) {
                    return result;
                }
            }
        }
        String table = "concept";
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<Row> ds = dataset.groupBy(dataset.col("c_name").as("key")).count();
        Dataset<StockStatisticsValue> _ds = ds.as(Encoders.bean(StockStatisticsValue.class));
        jedis.set("stock_concept", JsonUtils.objectToJson(_ds.collectAsList()));
        return _ds.collectAsList();
    }

    public List<StockStatisticsValue> staticStockWithProvince(boolean reload) {
        Jedis jedis = new Jedis(host, port);
        if (!reload) {
            //先从缓存取
//            List<AlternateValue> result = (List<AlternateValue>) this.sparkJedisRepository.get("tow_eight", List.class, AlternateValue.class);
            String json = jedis.get("stock_province");
            if (json != null && !json.equals("")) {
                List<StockStatisticsValue> result = JsonUtils.jsonToObject(json, List.class, StockStatisticsValue.class);
                if (result != null) {
                    return result;
                }
            }
        }
        String table = "area";
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<Row> ds = dataset.groupBy(dataset.col("area").as("key")).count();
        Dataset<StockStatisticsValue> _ds = ds.as(Encoders.bean(StockStatisticsValue.class));
        jedis.set("stock_province", JsonUtils.objectToJson(_ds.collectAsList()));
        return _ds.collectAsList();
    }
}
