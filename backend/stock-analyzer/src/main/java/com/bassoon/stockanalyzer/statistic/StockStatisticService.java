package com.bassoon.stockanalyzer.statistic;

import com.bassoon.stockanalyzer.policy.TwoEightNode;
import com.bassoon.stockanalyzer.spark.SparkRepository;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.storage.StorageLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class StockStatisticService implements Serializable {
    @Autowired
    private SparkRepository sparkRepository;

    public List<StockValue> staticStockByKey(String key) {
        if (key.equals("city")) {
            return this.staticStockWithCity();
        } else if (key.equals("province")) {
            return this.staticStockWithProvince();
        } else if (key.equals("industry")) {
            return this.staticStockWithIndustry();
        } else if (key.equals("concept")) {
            return this.staticStockWithConcept();
        }
        return null;
    }

    public List<StockValue> staticStockWithCity() {
        String table = "stock";
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<Row> ds = dataset.groupBy(dataset.col("region").as("key")).count();
        Dataset<StockValue> _ds = ds.as(Encoders.bean(StockValue.class));
        return _ds.collectAsList();
    }

    public List<StockValue> staticStockWithIndustry() {
        String table = "stock_basics";
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<Row> ds = dataset.groupBy(dataset.col("industry").as("key")).count();
        Dataset<StockValue> _ds = ds.as(Encoders.bean(StockValue.class));
        return _ds.collectAsList();
    }

    public List<StockValue> staticStockWithConcept() {
        String table = "concept";
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<Row> ds = dataset.groupBy(dataset.col("c_name").as("key")).count();
        Dataset<StockValue> _ds = ds.as(Encoders.bean(StockValue.class));
        return _ds.collectAsList();
    }

    public List<StockValue> staticStockWithProvince() {
        String table = "area";
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<Row> ds = dataset.groupBy(dataset.col("area").as("key")).count();
        Dataset<StockValue> _ds = ds.as(Encoders.bean(StockValue.class));
        return _ds.collectAsList();
    }
}
