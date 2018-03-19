package com.bassoon.stockanalyzer.policy;

import com.bassoon.stockanalyzer.spark.SparkRepository;
import com.bassoon.stockanalyzer.utils.DateUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class TwoEightRotation implements Serializable {
    @Autowired
    private SparkRepository sparkRepository;

    public List<TwoEightNode> getWeekData(String table) {
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset.createOrReplaceTempView(table);
        dataset = dataset.sqlContext().sql("select * from " + table + " where date >= '2007-01-19'");
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        JavaRDD<Row> resultRDD = dataset.toJavaRDD();
        JavaRDD<TwoEightNode> ds = resultRDD.map(new Function<Row, TwoEightNode>() {
            TwoEightNode previousNode = null;

            @Override
            public TwoEightNode call(Row row) throws Exception {
                String date = (String) row.getAs("date");
                if (DateUtils.dateToWeek(date) == 5) {
                    Double close = (Double) row.getAs("close");
                    TwoEightNode myself = new TwoEightNode(close, date);
                    myself.setPreviousNode(previousNode);
                    myself.calulateMoney();
                    previousNode = myself;
                    return myself;
                }
                return null;
            }
        });
        ds = ds.filter(new Function<TwoEightNode, Boolean>() {
            @Override
            public Boolean call(TwoEightNode twoEightNode) throws Exception {
                if (twoEightNode != null) {
                    return true;
                }
                return false;
            }
        });
        return ds.collect();
    }
}
