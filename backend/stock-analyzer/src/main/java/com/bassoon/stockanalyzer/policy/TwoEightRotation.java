package com.bassoon.stockanalyzer.policy;

import com.bassoon.stockanalyzer.spark.SparkRepository;
import com.bassoon.stockanalyzer.utils.DateUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.storage.StorageLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.reflect.ClassTag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TwoEightRotation implements Serializable {
    @Autowired
    private SparkRepository sparkRepository;

    //stock_zz_k_data
    //stock_hs_k_data

    public List<TwoEightNode2> getWeekData(String table) {
        Dataset<Row> dataset = sparkRepository.getDatasetByTable(table);
        dataset.createOrReplaceTempView(table);
        dataset = dataset.sqlContext().sql("select * from " + table + " where date >= '2007-01-19'");
        dataset = dataset.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<TwoEightNode2> ds = dataset.map(new MapFunction<Row, TwoEightNode2>() {
            TwoEightNode2 previousNode = null;

            @Override
            public TwoEightNode2 call(Row row) throws Exception {
                String date = (String) row.getAs("date");
                if (DateUtils.dateToWeek(date) == 5) {
                    Double close = (Double) row.getAs("close");
                    TwoEightNode2 myself = new TwoEightNode2();
                    myself.setClose(close);
                    myself.setDate(date);
                    myself.setPreviousNode(previousNode);
                    myself.calulateMoney();
                    previousNode = myself;
                    return myself;
                }
                return null;
            }
        }, Encoders.bean(TwoEightNode2.class));
        ds = ds.filter(new FilterFunction<TwoEightNode2>() {
            @Override
            public boolean call(TwoEightNode2 twoEightNode) throws Exception {
                if (twoEightNode != null) {
                    return true;
                }
                return false;
            }
        });
        return ds.collectAsList();
    }

    private TwoEightNode previousNode = null;

    public List<TwoEightNode> generateTwoEightRatationData() {
        String[] tables = new String[]{"stock_zz_k_data", "stock_hs_k_data"};
        List<Dataset<Row>> dss = new ArrayList<Dataset<Row>>();
        for (String table : tables) {
            Dataset<Row> ds = sparkRepository.getDatasetByTable(table);
            ds.createOrReplaceTempView(table);
            ds = ds.sqlContext().sql("select * from " + table + " where date >= '2007-01-19'");
            dss.add(ds);
        }
        Column[] columns_0 = new Column[]{dss.get(0).col("close").as("zzclose"), dss.get(0).col("date")};
        Column[] columns_1 = new Column[]{dss.get(1).col("close").as("hsclose"), dss.get(1).col("date")};
        Dataset<Row> ds = dss.get(0).select(columns_0).join(dss.get(1).select(columns_1), "date");
        ds = ds.sort("date");
        ds = ds.persist(StorageLevel.MEMORY_AND_DISK());
        Dataset<TwoEightNode> _ds = ds.map(new MapFunction<Row, TwoEightNode>() {
            TwoEightNode previousNode = null;

            @Override
            public TwoEightNode call(Row row) throws Exception {
                String date = (String) row.getAs("date");
                if (DateUtils.dateToWeek(date) == 5) {
                    TwoEightNode node = new TwoEightNode();
                    Double zzclose = (Double) row.getAs("zzclose");
                    Double hsclose = (Double) row.getAs("hsclose");
                    node.setDate(date);
                    node.setHsclose(hsclose);
                    node.setZzclose(zzclose);
                    node.setPreviousNode(previousNode);
                    node.calulateMoney();
                    System.out.println(date + "||" + node.getHsMoney() + "||" + node.getZzMoney() + "||" + previousNode);
                    previousNode = node;
                    System.out.println(date + "|| aaa" + node.getHsMoney() + "||" + node.getZzMoney() + "||" + previousNode);
                    return node;
                }
                return null;
            }
        }, Encoders.bean(TwoEightNode.class));
        _ds = _ds.filter(new FilterFunction<TwoEightNode>() {
            @Override
            public boolean call(TwoEightNode twoEightNode) throws Exception {
                if (twoEightNode != null) {
                    return true;
                }
                return false;
            }
        });
        return _ds.collectAsList();
    }
}
