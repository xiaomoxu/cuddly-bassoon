package com.bassoon.stockanalyzer.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkTest {
    public static void main(String argz[]) {
        SparkSession spark = SparkSession.builder().config(new SparkConf().setAppName("myapp").setMaster("spark://dlndevtrux1l01.dev.rocketsoftware.com:7077")).getOrCreate();
        Dataset<Row> peopleDF = spark.read().json("D:\\work\\project\\github\\cuddly-bassoon\\backend\\stock-analyzer\\src\\main\\resources\\people.json");

        // DataFrames can be saved as Parquet files, maintaining the schema information
        peopleDF.write().parquet("people.parquet");

        // Read in the Parquet file created above.
        // Parquet files are self-describing so the schema is preserved
        // The result of loading a parquet file is also a DataFrame
        Dataset<Row> parquetFileDF = spark.read().parquet("people.parquet");

        // Parquet files can also be used to create a temporary view and then used in SQL statements
        parquetFileDF.createOrReplaceTempView("parquetFile");
        Dataset<Row> namesDF = spark.sql("SELECT name FROM parquetFile WHERE age BETWEEN 13 AND 19");
        Dataset<String> namesDS = namesDF.map(
                (MapFunction<Row, String>) row -> "Name: " + row.getString(0),
                Encoders.STRING());
        namesDS.show();
        spark.stop();



    }
}
