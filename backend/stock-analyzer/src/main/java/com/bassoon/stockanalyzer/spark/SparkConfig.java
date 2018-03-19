package com.bassoon.stockanalyzer.spark;


import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class SparkConfig {
    @Autowired
    private Environment env;

    @Value("${app.name}")
    private String appName;
    @Value("${spark.cores.max}")
    private String cores;
    @Value("${master.uri}")
    private String masterUri;
    @Value("${hadoop.home.dir}")
    private String hadoopHome;

    @Bean
    public SparkConf sparkConf() {
        System.setProperty("hadoop.home.dir", hadoopHome);//好像只有针对windwos系统有效果>linux下这句话无效
        return new SparkConf().setAppName(appName).set("spark.cores.max", cores).setMaster(masterUri).
                setJars(new String[]{"C:\\home\\xxu\\github\\cuddly-bassoon\\backend\\stock-analyzer\\target\\stock-analyzer-0.0.1-SNAPSHOT.jar.original"});
    }

    @Bean
    public SparkContext sparkContext() {
        SparkContext sparkContext = new SparkContext(sparkConf());
        return sparkContext;
    }

    @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder().config(sparkContext().getConf()).getOrCreate();
    }

}
