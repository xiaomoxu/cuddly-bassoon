package com.bassoon.stockanalyzer.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.List;

public class SparkTest {
    public static void main(String argz[]) {
        System.setProperty("hadoop.home.dir", "C:\\home\\xxu\\github\\hadoop-common-2.2.0-bin\\");

        SparkConf sparkConf = new SparkConf().setAppName("myapp").set("spark.cores.max", "4").setMaster("spark://10.20.116.107:7077");

        SparkContext sc = new SparkContext(sparkConf);

        SparkSession spark = SparkSession.builder().config(sc.getConf()).getOrCreate();

        Dataset<Row> jdbcDF = spark.read()
                .format("jdbc")
                .option("url", "jdbc:mysql://10.20.116.107:3306/CN_BASSOON?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull")
                .option("dbtable", "stock_hs_k_data")
                .option("user", "root")
                .option("password", "Liaobi()7595k")
                .load();
        jdbcDF.persist();
        jdbcDF.show();
//        Properties connectionProperties = new Properties();
//        connectionProperties.put("user", "username");
//        connectionProperties.put("password", "password");
//        Dataset<Row> jdbcDF2 = spark.read()
//                .jdbc("jdbc:mysql://10.20.116.107:3306/CN_STOCK?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull", "schema.tablename", connectionProperties);


    }
}
