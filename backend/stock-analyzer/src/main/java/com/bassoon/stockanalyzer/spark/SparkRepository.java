package com.bassoon.stockanalyzer.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SparkRepository {
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

}
