package com.bassoon.stockanalyzer.spark.config;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class SparkRepository implements Serializable{
    @Autowired
    private SparkSession sparkSession;

    @Value("${spring.datasource.url}")
    private String dataSource;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbUserPwd;

    public Dataset<Row> getDatasetByTable(String tableName) {
        Dataset<Row> jdbcDF = sparkSession.read()
                .format("jdbc")
                .option("url", dataSource)
                .option("dbtable", tableName)
                .option("user", dbUsername)
                .option("password", dbUserPwd)
                .load();
        return jdbcDF;
    }

    /**
     * Create or replace a temp view from the specified table, the temp view will have the same name
     * as the table.
     */
    public void createOrReplaceTempViewByTable(String tableName) {
        createOrReplaceTempViewByTable(tableName, tableName);
    }

    /**
     * Create or replace a temp view from the specified table and will be named with the specified
     * view name.
     */
    public void createOrReplaceTempViewByTable(String tableName, String viewName) {
        getDatasetByTable(tableName).createOrReplaceTempView(viewName);
    }

    /**
     * Shortcut to dataset.sqlContext().sql(String sqlText)
     */
    public Dataset<Row> getDatasetBySql(String sqlText) {
        return sparkSession.sql(sqlText);
    }

    public void registerUdaf(String name, UserDefinedAggregateFunction udaf) {
        sparkSession.udf().register(name, udaf);
    }

}
