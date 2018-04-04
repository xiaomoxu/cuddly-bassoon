package com.bassoon.stockanalyzer.policy;


//股票基本面筛选规则
//
//        1. 选择目标股票池并且进行一定的排序，这个选择和排序其实是决定这个策略更接近于value investment还是growth investment. 一开始我选择了利润增长作为排序方式。
//        2. 在目标股票池当中用打分机制进一步筛选，看股票的三个方面，我选择了以下条件，满足一个条件得一分
//
//
//        1. 盈利性
//        ROA(current) > 0
//        ROA(current)>ROA(last year)
//        OCF(current)> 0OCF(current)>OCF(last year)
//        2. 借债情况
//        current ratio(current)> current ratio (last year)
//        debt to equity ratio (current) < debt to equity ratio (last year)
//        3. 运营状况
//        gross margin(current)> gross margin(last year)
//        asset turnover (current) > asset turnover (last year)
//        1. 大于等于7分选入最终股票池，控制在一定数目内(所以最初的排序指标是重要的)，选择调仓频率，仓位的调整方式（我选择了最多20个股票，平均分配资金）


import com.bassoon.stockanalyzer.spark.SparkRepository;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.ReduceFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.storage.StorageLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class StockFilter implements Serializable {
    @Autowired
    private SparkRepository sparkRepository;

    public List<StockNode> stockEvalution(int year) {
        Dataset<Row> dataset = sparkRepository.getDatasetByTable("stock_basics");
        dataset.createOrReplaceTempView("stock_basics_temp_view");
        dataset = dataset.sqlContext().sql("select code , name from stock_basics_temp_view");
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<StockNode> ds = dataset.map(new MapFunction<Row, StockNode>() {
            @Override
            public StockNode call(Row row) throws Exception {
                String code = (String) row.getAs("code");
                String name = (String) row.getAs("name");
                StockNode node = new StockNode();
                if (code != null || !code.equals("")) {
                    node.setCode(code);
                    node.setName(name);
                    int score = scoringROE(Integer.valueOf(code.trim()), year);
                    node.setScore(score);
                }
                return node;
            }
        }, Encoders.bean(StockNode.class));
        return ds.collectAsList();
    }

    private int scoringROE(int code, int year) {
        //计算ROE
        //条件 ROE>0 , ROE(YEAR) > ROE(LAST YEAR)
        //ROE_TOTAL = 4季度ROE总和
        Dataset<Row> stock_profit_dataset = sparkRepository.getDatasetByTable("stock_profit_data");
        stock_profit_dataset.createOrReplaceTempView("stock_profit_data_temp_view");
        stock_profit_dataset = stock_profit_dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<Double> roe_dataset = stock_profit_dataset.sqlContext().
                sql("select roe from stock_profit_data_temp_view where code=" + code + " and year=" + year).as(Encoders.DOUBLE());
        Double doubleValue = roe_dataset.toJavaRDD().reduce(new Function2<Double, Double, Double>() {
            @Override
            public Double call(Double aDouble, Double aDouble2) throws Exception {
                return aDouble + aDouble2;
            }
        });
        if (doubleValue > 0) {
            return 1;
        }
        return 0;
    }

}
