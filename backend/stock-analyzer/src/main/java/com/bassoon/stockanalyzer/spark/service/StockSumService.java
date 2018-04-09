package com.bassoon.stockanalyzer.spark.service;

import static org.apache.spark.sql.functions.expr;
import static scala.collection.JavaConversions.asScalaBuffer;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.bassoon.stockanalyzer.spark.config.SparkRepository;
import com.google.common.collect.Lists;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.MutableAggregationBuffer;
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import org.springframework.beans.factory.annotation.Autowired;
import scala.collection.JavaConversions;
import scala.collection.Seq;

/**
 * StockSumService
 *
 * @author lwan
 * @since Apr 8, 2018, 9:48:46 AM
 */
public class StockSumService implements Serializable {

  @Autowired
  private SparkRepository sparkRepository;

  public void doSum() {
    doSum("");
  }

  public void doSum(int startYear, int endYear) {
    doSum(" WHERE year >= " + startYear + " AND year <= " + endYear);
  }

  private void doSum(String whereClause) {

    // Create temp views

    Dataset<Row> dsBasics = sparkRepository.getDatasetByTable("stock_basics");
    dsBasics.createOrReplaceTempView("basics_view");
    Dataset<Row> dsRoe = sparkRepository.getDatasetByTable("stock_profit_data");
    dsRoe.createOrReplaceTempView("roe_view");
    Dataset<Row> dsCashflowRatio = sparkRepository.getDatasetByTable("stock_cashflow_data");
    dsCashflowRatio.createOrReplaceTempView("cashflowratio_view");
    Dataset<Row> dsCurrentRatio = sparkRepository.getDatasetByTable("stock_debtpay_data");
    dsCurrentRatio.createOrReplaceTempView("currentratio_view");
    Dataset<Row> dsEpsg = sparkRepository.getDatasetByTable("stock_growth_data");
    dsEpsg.createOrReplaceTempView("epsg_view");
    Dataset<Row> dsTurnover = sparkRepository.getDatasetByTable("stock_operation_data");
    dsTurnover.createOrReplaceTempView("turnover_view");

    // Create initial datasets

    dsBasics = dsBasics
        .sqlContext().sql("SELECT code, name FROM basics_view" + whereClause);
    dsRoe = dsRoe
        .sqlContext().sql("SELECT code, roe, year, quarter FROM roe_view" + whereClause);
    dsCashflowRatio = dsCashflowRatio
        .sqlContext().sql("SELECT code, cashflowratio, year, quarter FROM cashflowratio_view" + whereClause);
    dsCurrentRatio = dsCurrentRatio
        .sqlContext().sql("SELECT code, currentratio, year, quarter FROM currentratio_view" + whereClause);
    dsEpsg = dsEpsg
        .sqlContext().sql("SELECT code, epsg, year, quarter FROM epsg_view" + whereClause);
    dsTurnover = dsTurnover
        .sqlContext().sql("SELECT code, currentasset_turnover, year, quarter FROM turnover_view" + whereClause);

    // Remove duplicate rows

    dsRoe = dsRoe.distinct();
    dsCashflowRatio = dsCashflowRatio.distinct();
    dsCurrentRatio = dsCurrentRatio.distinct();
    dsEpsg = dsEpsg.distinct();
    dsTurnover = dsTurnover.distinct();

    // Joinning datasets (full outer join)

    Seq<String> colCode = asScalaBuffer(Arrays.asList("code")).toSeq();
    Seq<String> colCodeYearQuarter = asScalaBuffer(Arrays.asList("code", "year", "quarter")).toSeq();

    Dataset<Row> joined = dsBasics.join(dsRoe, colCode, "fullouter");
    joined = joined.join(dsCashflowRatio, colCodeYearQuarter, "fullouter");
    joined = joined.join(dsCurrentRatio, colCodeYearQuarter, "fullouter");
    joined = joined.join(dsEpsg, colCodeYearQuarter, "fullouter");
    joined = joined.join(dsTurnover, colCodeYearQuarter, "fullouter");

    // Summing

    Dataset<Row> summed = joined
            .groupBy("code", "year")
            .agg(
                    expr("mySum(roe)"), expr("mySum(cashflowratio)"), expr("mySum(currentratio)"),
                    expr("mySum(epsg)"), expr("mySum(currentasset_turnover)")
            );

    // Return

    writeToDatabase(summed);
  }

  private void writeToDatabase(Dataset<Row> dataset) {
    throw new UnsupportedOperationException("To be implemented.");
  }

  public static class MySum extends UserDefinedAggregateFunction {

    private StructType inputSchema;
    private StructType bufferSchema;
    private DataType dataType;
    private boolean deterministic;

    public MySum() {
      inputSchema = DataTypes.createStructType(Arrays.asList(
          DataTypes.createStructField("inputColumn", DataTypes.DoubleType, true)
      ));
      bufferSchema = DataTypes.createStructType(Arrays.asList(
          DataTypes.createStructField("sum", DataTypes.DoubleType, true),
          DataTypes.createStructField("count", DataTypes.LongType, true)
      ));
      dataType = DataTypes.DoubleType;
      deterministic = true;
    }

    @Override
    public StructType inputSchema() {
      return inputSchema;
    }

    @Override
    public StructType bufferSchema() {
      return bufferSchema;
    }

    @Override
    public DataType dataType() {
      return dataType;
    }

    @Override
    public boolean deterministic() {
      return deterministic;
    }

    // initialize -> update -> merge -> evaluate

    @Override
    public void initialize(MutableAggregationBuffer buffer) {
      buffer.update(0, 0D);
      buffer.update(1, 0L);
    }

    @Override
    public void update(MutableAggregationBuffer buffer, Row input) {
      if (!input.isNullAt(0)) {
        double updatedSum = buffer.getDouble(0) + input.getDouble(0);
        long updatedCount = buffer.getLong(1) + 1;
        buffer.update(0, updatedSum);
        buffer.update(1, updatedCount);
      }
    }

    @Override
    public void merge(MutableAggregationBuffer buffer1, Row buffer2) {
      double mergedSum = buffer1.getDouble(0) + buffer2.getDouble(0);
      long mergedCount = buffer1.getLong(1) + buffer2.getLong(1);
      buffer1.update(0, mergedSum);
      buffer1.update(1, mergedCount);
    }

    @Override
    public Double evaluate(Row buffer) {
      double sum = buffer.getDouble(0);
      long count = buffer.getLong(1);
      if (count < 4) {
        return 0D;
      } else {
        return sum;
      }
    }

  }

}
