package com.bassoon.stockanalyzer.spark.service;

import com.bassoon.stockanalyzer.mapper.ScoringMapper;
import com.bassoon.stockanalyzer.spark.model.BasicStockValue;
import com.bassoon.stockanalyzer.spark.model.StockScoreValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScorePolicyService {
    @Autowired
    private StockSparkService stockSparkService;
    @Autowired
    private ScoringMapper scoringMapper;

//    public List<StockScoreValue> scoring(int year) {
//
//        List<BasicStockValue> basicStockList = stockSparkService.getBasicStockList(false);
//        for (BasicStockValue BasicStockValue : basicStockList) {
//            List<Double> roeArray = scoringMapper.getRoeInProfitByYear(BasicStockValue.getCode(), year);
//            List<Double> lastyear_roeArray = scoringMapper.getRoeInProfitByYear(BasicStockValue.getCode(), year - 1);
//            try {
//                Double total_Roe = roeArray.stream().mapToDouble(p -> p.doubleValue()).sum();
//                Double total_lastyear_roeArray = roeArray.stream().mapToDouble(p -> p.doubleValue()).sum();
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
}
