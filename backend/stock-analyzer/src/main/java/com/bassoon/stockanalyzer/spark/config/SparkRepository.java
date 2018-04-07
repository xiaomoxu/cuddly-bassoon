package com.bassoon.stockanalyzer.spark.config;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class SparkRepository implements Serializable {

    @Value("${spring.datasource.url}")
    private String dataSource;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbUserPwd;

    @Value("${app.name}")
    private String appName;
    @Value("${spark.cores.max}")
    private String cores;
    @Value("${master.uri}")
    private String masterUri;
    @Value("${hadoop.home.dir}")
    private String hadoopHome;

    @Value("${jar.file.path}")
    private String jarFile;

    private transient SparkContext sparkContext = null;

    public Dataset<Row> getDatasetByTable(String tableName) {
        Dataset<Row> jdbcDF = sparkSession().read()
                .format("jdbc")
                .option("url", dataSource)
                .option("dbtable", tableName)
                .option("user", dbUsername)
                .option("password", dbUserPwd)
                .load();
        return jdbcDF;
    }

    private SparkConf createSparkConfig() {
        System.setProperty("hadoop.home.dir", hadoopHome);//好像只有针对windwos系统有效果>linux下这句话无效
        return new SparkConf().setAppName(appName).set("spark.cores.max", cores).setMaster(masterUri).
                setJars(new String[]{jarFile});
    }

    private SparkContext createSparkContext() {
        sparkContext = new SparkContext(createSparkConfig());
        return sparkContext;
    }

    private SparkSession sparkSession() {
        return SparkSession.builder().config(createSparkContext().getConf()).getOrCreate();
    }

    public void stopAndClose() {
        if (sparkContext != null && !sparkContext.isStopped()) {
            sparkContext.stop();
        }
    }
}
