package com.bassoon.stockanalyzer.spark.service;

import com.bassoon.stockanalyzer.mapper.ScoringMapper;
import com.bassoon.stockanalyzer.spark.config.SparkRepository;
import com.bassoon.stockanalyzer.spark.model.BasicStockValue;
import com.bassoon.stockanalyzer.spark.model.StockScoreValue;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScorePolicyService {
    @Autowired
    private StockSparkService stockSparkService;
    @Autowired
    private transient ScoringMapper scoringMapper;
    @Autowired
    private SparkRepository sparkRepository;

    //    public List<StockScoreValue> scoring(int year) {
//
//        List<BasicStockValue> basicStockList = stockSparkService.getBasicStockList(false);
//        for (BasicStockValue BasicStockValue : basicStockList) {
//            List<Double> roeArray = scoringMapper.getRoeInProfitByYear(BasicStockValue.getCode(), year);
//            List<Double> lastyear_roeArray = scoringMapper.getRoeInProfitByYear(BasicStockValue.getCode(), year - 1);
//            try {
//                Double total_Roe = roeArray.stream().mapToDouble(p -> p.doubleValue()).sum();
//                Double total_lastyear_roeArray = lastyear_roeArray.stream().mapToDouble(p -> p.doubleValue()).sum();
//                if (total_Roe > 0) {
//                    BasicStockValue.setScore(1);
//                    if (total_Roe > total_lastyear_roeArray) {
//                        BasicStockValue.setScore(1);
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("exception: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
//                continue;
//            }
//
//        }
//        return basicStockList;
//    }
    public List<StockScoreValue> scoring(int year) {
        Dataset<Row> dataset = sparkRepository.getDatasetByTable("stock_basics");
        dataset.createOrReplaceTempView("stock_basics_temp_view");
        dataset = dataset.sqlContext().sql("select code , name from stock_basics_temp_view");
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<BasicStockValue> ds = dataset.map(new MapFunction<Row, BasicStockValue>() {
            @Override
            public BasicStockValue call(Row row) throws Exception {
                String code = (String) row.getAs("code");
                String name = (String) row.getAs("name");
                BasicStockValue node = new BasicStockValue();
                if (code != null || !code.equals("")) {
                    node.setCode(code);
                    node.setName(name);
                    List<Double> roeArray = scoringMapper.getRoeInProfitByYear(code, year);
                    Double total_Roe = roeArray.stream().mapToDouble(p -> p.doubleValue()).sum();
                    System.out.println(code + " " + name + " " + total_Roe);

                }
                return node;
            }
        }, Encoders.bean(BasicStockValue.class));
        return null;
    }
}
