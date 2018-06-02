package com.bassoon.stockanalyzer.spark.service;

import com.bassoon.stockanalyzer.domain.BasicStock;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

@Service
public class ScorePolicyService {
    @Autowired
    private StockSparkService stockSparkService;
    @Autowired
    private transient ScoringMapper scoringMapper;
    @Autowired
    private SparkRepository sparkRepository;

    public List<StockScoreValue> scoringTasks(int year) {
//        ConcurrentHashMap<String, LinkedList<StockScoreValue>> concurrentHashMap = new ConcurrentHashMap<String, LinkedList<StockScoreValue>>();
        List<BasicStockValue> basicStockList = stockSparkService.getBasicStockList(false);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(5);
        ScoringThread thread1 = new ScoringThread(basicStockList.subList(0, 600), year, countDownLatch);
        ScoringThread thread2 = new ScoringThread(basicStockList.subList(601, 1200), year, countDownLatch);
        ScoringThread thread3 = new ScoringThread(basicStockList.subList(1201, 1800), year, countDownLatch);
        ScoringThread thread4 = new ScoringThread(basicStockList.subList(1801, 2400), year, countDownLatch);
        ScoringThread thread5 = new ScoringThread(basicStockList.subList(2400, basicStockList.size() - 1), year, countDownLatch);
        List<ScoringThread> threads = new ArrayList<ScoringThread>();
        threads.add(thread1);
        threads.add(thread2);
        threads.add(thread3);
        threads.add(thread4);
        threads.add(thread5);
        for (ScoringThread thread : threads) {
            executorService.execute(thread);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class ScoringThread implements Runnable {
        private CountDownLatch countDownLatch = null;
        private List<BasicStockValue> basicStockList = null;
        private int year;
        private List<StockScoreValue> result = null;

        public ScoringThread(List<BasicStockValue> basicStockList, int year, CountDownLatch countDownLatch) {
            this.basicStockList = basicStockList;
            this.countDownLatch = countDownLatch;
            this.year = year;
        }

        @Override
        public void run() {
            this.result = scoring(basicStockList, year);
            countDownLatch.countDown();
        }

        public List<StockScoreValue> getResult() {
            return result;
        }
    }

    /**
     * 先用单线程本地常规方法解决这个需求
     * 之后我会再用spark重新计算
     *
     * @param year
     * @return
     */
    public List<StockScoreValue> scoring(List<BasicStockValue> basicStockList, int year) {
        List<StockScoreValue> scoreValueList = new LinkedList<StockScoreValue>();
        for (BasicStockValue basicStockValue : basicStockList) {
            StockScoreValue stockScoreValue = new StockScoreValue();
            stockScoreValue.setCode(basicStockValue.getCode());
            stockScoreValue.setName(basicStockValue.getName());
            stockScoreValue = this.scoringForROE(stockScoreValue, year);//2分
            stockScoreValue = this.scoringCurrentAssetTurnover(stockScoreValue, year);//1分
            stockScoreValue = this.scoringForCashFlowRatio(stockScoreValue, year);//2分
            stockScoreValue = this.scoringForDebtCurrentRatio(stockScoreValue, year);//1分
            stockScoreValue = this.scoringForEPS(stockScoreValue, year);//2分
//            if (stockScoreValue.getScore() >= 6) {
//                scoreValueList.add(stockScoreValue);
//            }
            scoreValueList.add(stockScoreValue);

        }
        return scoreValueList;
    }

    public StockScoreValue scoringForROE(StockScoreValue stockScoreValue, int year) {
        List<BasicStock> thisArray = null;
        List<BasicStock> previousArray = null;
        try {
            thisArray = scoringMapper.getRoeByYear(stockScoreValue.getCode(), year);
            previousArray = scoringMapper.getRoeByYear(stockScoreValue.getCode(), year - 1);
        } catch (Exception e) {
            System.out.println("exception for ROE: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
        }
        try {
            if (thisArray.size() == 4 && previousArray.size() == 4) {
                Double thisDoubleValue = thisArray.stream().mapToDouble(BasicStock::getRoe).sum();
                Double previousDoubleValue = previousArray.stream().mapToDouble(BasicStock::getRoe).sum();
                stockScoreValue.getFilterCondition().setRoe(thisDoubleValue);
                stockScoreValue.getPreviousFilterCondition().setRoe(previousDoubleValue);
                if (thisDoubleValue > 0) {
                    stockScoreValue.setScore(1);
                    if (thisDoubleValue > previousDoubleValue) {
                        stockScoreValue.setScore(1);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("exception for ROE: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
        }
        try {
            if (thisArray.size() == 4 && previousArray.size() == 4) {
                Double thisDoubleValue = thisArray.stream().mapToDouble(BasicStock::getGrossProfitRate).sum();
                Double previousDoubleValue = previousArray.stream().mapToDouble(BasicStock::getGrossProfitRate).sum();
                stockScoreValue.getFilterCondition().setGrossProfitRatio(thisDoubleValue);
                stockScoreValue.getPreviousFilterCondition().setGrossProfitRatio(previousDoubleValue);
                if (thisDoubleValue > previousDoubleValue) {
                    stockScoreValue.setScore(1);
                }
            }
        } catch (Exception e) {
            System.out.println("exception for GROSS_PROFIT: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
        }
        return stockScoreValue;
    }

    public StockScoreValue scoringForCashFlowRatio(StockScoreValue stockScoreValue, int year) {
        List<Double> thisArray = null;
        List<Double> previousArray = null;
        try {
            thisArray = scoringMapper.getCashFlowRatioByYear(stockScoreValue.getCode(), year);
            previousArray = scoringMapper.getCashFlowRatioByYear(stockScoreValue.getCode(), year - 1);
        } catch (Exception e) {
            System.out.println("exception for CASH_FLOW: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
        }
        try {
            if (thisArray.size() == 4 && previousArray.size() == 4) {
                Double thisDoubleValue = thisArray.stream().mapToDouble(Double::doubleValue).sum();
                Double previousDoubleValue = previousArray.stream().mapToDouble(Double::doubleValue).sum();
                stockScoreValue.getFilterCondition().setCashFlowRatio(thisDoubleValue);
                stockScoreValue.getPreviousFilterCondition().setCashFlowRatio(previousDoubleValue);
                if (thisDoubleValue > 0) {
                    stockScoreValue.setScore(1);
                    if (thisDoubleValue > previousDoubleValue) {
                        stockScoreValue.setScore(1);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("exception for CASH_FLOW: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
        }
        return stockScoreValue;
    }

    public StockScoreValue scoringForDebtCurrentRatio(StockScoreValue stockScoreValue, int year) {
        List<Double> thisArray = null;
        List<Double> previousArray = null;
        try {
            thisArray = scoringMapper.getCurrentDebtRatioByYear(stockScoreValue.getCode(), year);
            previousArray = scoringMapper.getCashFlowRatioByYear(stockScoreValue.getCode(), year - 1);
        } catch (Exception e) {
            System.out.println("exception for DEBY_CURRENT_RATIO: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
        }
        try {
            if (thisArray.size() == 4 && previousArray.size() == 4) {
                Double thisDoubleValue = thisArray.stream().mapToDouble(Double::doubleValue).sum();
                Double previousDoubleValue = previousArray.stream().mapToDouble(Double::doubleValue).sum();
                stockScoreValue.getFilterCondition().setDebtCurrentRatio(thisDoubleValue);
                stockScoreValue.getPreviousFilterCondition().setDebtCurrentRatio(previousDoubleValue);
                if (thisDoubleValue > previousDoubleValue) {
                    stockScoreValue.setScore(1);
                }
            }
        } catch (Exception e) {
            System.out.println("exception for CASH_FLOW: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
        }
        return stockScoreValue;
    }

    public StockScoreValue scoringForEPS(StockScoreValue stockScoreValue, int year) {
        List<Double> thisArray = null;
        List<Double> previousArray = null;
        try {
            thisArray = scoringMapper.getEpsgByYear(stockScoreValue.getCode(), year);
            previousArray = scoringMapper.getEpsgByYear(stockScoreValue.getCode(), year - 1);
        } catch (Exception e) {
            System.out.println("exception for EPS: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
        }
        try {
            if (thisArray.size() == 4 && previousArray.size() == 4) {
                Double thisDoubleValue = thisArray.stream().mapToDouble(Double::doubleValue).sum();
                Double previousDoubleValue = previousArray.stream().mapToDouble(Double::doubleValue).sum();
                stockScoreValue.getFilterCondition().setEpsg(thisDoubleValue);
                stockScoreValue.getPreviousFilterCondition().setEpsg(previousDoubleValue);
                if (thisDoubleValue > 0) {
                    stockScoreValue.setScore(1);
                    if (thisDoubleValue > previousDoubleValue) {
                        stockScoreValue.setScore(1);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("exception for EPS: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
        }
        return stockScoreValue;
    }

    public StockScoreValue scoringCurrentAssetTurnover(StockScoreValue stockScoreValue, int year) {
        List<Double> thisArray = null;
        List<Double> previousArray = null;
        try {
            thisArray = scoringMapper.getCurrentassetTurnoverByYear(stockScoreValue.getCode(), year);
            previousArray = scoringMapper.getCurrentassetTurnoverByYear(stockScoreValue.getCode(), year - 1);
        } catch (Exception e) {
            System.out.println("exception for CurrentAssetTurnover: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
        }
        try {
            if (thisArray.size() == 4 && previousArray.size() == 4) {
                Double thisDoubleValue = thisArray.stream().mapToDouble(Double::doubleValue).sum();
                Double previousDoubleValue = previousArray.stream().mapToDouble(Double::doubleValue).sum();
                stockScoreValue.getFilterCondition().setCurrentAssetTurnover(thisDoubleValue);
                stockScoreValue.getPreviousFilterCondition().setCurrentAssetTurnover(previousDoubleValue);
                if (thisDoubleValue > previousDoubleValue) {
                    stockScoreValue.setScore(1);
                }
            }
        } catch (Exception e) {
            System.out.println("exception for CurrentAssetTurnover: " + stockScoreValue.getCode() + " " + stockScoreValue.getName());
        }
        return stockScoreValue;
    }
}
