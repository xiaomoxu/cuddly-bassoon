package com.bassoon.stockanalyzer.spark.service;

import com.bassoon.stockanalyzer.mapper.ScoringMapper;
import com.bassoon.stockanalyzer.spark.model.StockScoreValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockScore {
    @Autowired
    private StockSparkService stockSparkService;
    @Autowired
    private ScoringMapper scoringMapper;

    public List<StockScoreValue> scoring(int year) {

        List<StockScoreValue> basicStockList = stockSparkService.getBasicStockList();
        for (StockScoreValue stockScoreValue : basicStockList) {
            List<Double> roeArray = scoringMapper.getRoeInProfitByYear(stockScoreValue.getCode(), year);
            List<Double> lastyear_roeArray = scoringMapper.getRoeInProfitByYear(stockScoreValue.getCode(), year - 1);
            try {
                Double total_Roe = roeArray.stream().mapToDouble(p -> p.doubleValue()).sum();
                Double total_lastyear_roeArray = roeArray.stream().mapToDouble(p -> p.doubleValue()).sum();
                if (total_Roe > 0) {
                    stockScoreValue.setScore(1);
                    if (total_Roe > total_lastyear_roeArray) {
                        stockScoreValue.setScore(1);
                    }
                }
            } catch (Exception e) {
                System.out.println("exception: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
                continue;
            }

        }
        return basicStockList;
    }
}
