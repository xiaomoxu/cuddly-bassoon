package com.bassoon.stockanalyzer.spark.test;

import static org.apache.spark.sql.functions.expr;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.MutableAggregationBuffer;
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import scala.collection.JavaConversions;
import scala.collection.Seq;

/**
 * StockSumming
 *
 * @author lwan
 * @since Apr 8, 2018, 9:48:46 AM
 */
public class StockSumming implements Serializable {

  private String code;
  private String name;
  private double roe;
  private double cashflowratio;
  private double currentratio;
  private double epsg;
  private double turnover;
  private int year;

  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setRoe(double roe) {
    this.roe = roe;
  }

  public double getRoe() {
    return roe;
  }

  public void setCashflowratio(double cashflowratio) {
    this.cashflowratio = cashflowratio;
  }

  public double getCashflowratio() {
    return cashflowratio;
  }

  public void setCurrentratio(double currentratio) {
    this.currentratio = currentratio;
  }

  public double getCurrentratio() {
    return currentratio;
  }

  public void setEpsg(double epsg) {
    this.epsg = epsg;
  }

  public double getEpsg() {
    return epsg;
  }

  public void setTurnover(double turnover) {
    this.turnover = turnover;
  }

  public double getTurnover() {
    return turnover;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getYear() {
    return year;
  }

  public static void main(String[] args) {
    Repo.createAllTempViews();
    for (Row row : getSumming(" WHERE code < 20").collectAsList()) {
      try {
        Repo.write(row);
      } catch (SQLException sqle) {
        sqle.printStackTrace();
      }
    }
  }

  public static Dataset<Row> getSumming(String whereClause) {

    // Create initial datasets

    Dataset<Row> basics = Repo.sql("SELECT code, name FROM basics_view" + whereClause);
    Dataset<Row> roe = Repo.sql("SELECT code, roe, year, quarter FROM roe_view" + whereClause);
    Dataset<Row> cashflowration = Repo.sql("SELECT code, cashflowratio, year, quarter FROM cashflowratio_view" + whereClause);
    Dataset<Row> currentratio = Repo.sql("SELECT code, currentratio, year, quarter FROM currentratio_view" + whereClause);
    Dataset<Row> epsg = Repo.sql("SELECT code, epsg, year, quarter FROM epsg_view" + whereClause);
    Dataset<Row> turnover = Repo.sql("SELECT code, currentasset_turnover, year, quarter FROM turnover_view" + whereClause);

    // Remove duplicate rows

    roe = roe.distinct();
    cashflowration = cashflowration.distinct();
    currentratio = currentratio.distinct();
    epsg = epsg.distinct();
    turnover = turnover.distinct();

    // Joinning datasets (full outer join)

    Seq<String> column = JavaConversions.asScalaBuffer(Arrays.asList("code")).toSeq();
    Seq<String> columns = JavaConversions.asScalaBuffer(Arrays.asList("code", "year", "quarter")).toSeq();

    Dataset<Row> joined = basics.join(roe, column, "fullouter");
    joined = joined.join(cashflowration, columns, "fullouter");
    joined = joined.join(currentratio, columns, "fullouter");
    joined = joined.join(epsg, columns, "fullouter");
    joined = joined.join(turnover, columns, "fullouter");

    // Summing

    Dataset<Row> summed = joined
        .groupBy("code", "year")
        .agg(
            expr("mySum(roe)"), expr("mySum(cashflowratio)"), expr("mySum(currentratio)"),
            expr("mySum(epsg)"), expr("mySum(currentasset_turnover)")
        );

    // Return

    return summed;
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

  public static class Repo {

    private static final String URL = "jdbc:mysql://10.20.116.107:3306/CN_BASSOON?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
    private static final Properties PROPERTIES;
    private static final SparkSession SPARK;
    private static Connection connection;
    private static PreparedStatement statement;

    static {
      PROPERTIES = new Properties();
      PROPERTIES.put("driver", "com.mysql.jdbc.Driver");
      PROPERTIES.put("user", "root");
      PROPERTIES.put("password", "Liaobi()7595k");

      SPARK = SparkSession.builder().config(
          new SparkConf()
              .setAppName("turnover")
              .set("spark.cores.max", "4")
              .setMaster("spark://10.20.116.107:7077")
              .setJars(new String[] {"D:\\workspace\\cuddly-bassoon\\backend\\stock-analyzer\\target\\stock-analyzer-0.0.1-SNAPSHOT.jar.original"})
      ).getOrCreate();
      SPARK.udf().register("mySum", new MySum());

      try {
        connection = DriverManager.getConnection(URL, "root", "Liaobi()7595k");
        statement = connection.prepareStatement("INSERT INTO stock_summing VALUES(default, ?, ?, ?, ?, ?, ?, ?)");
      } catch (SQLException sqle) {
      }
    }

    public static Dataset<Row> get(String table) {
      return SPARK.read().jdbc(URL, table, PROPERTIES);
    }

    public static void createTempView(String table) {
      createTempView(table, table + "_view");
    }

    public static void createTempView(String table, String tempViewName) {
      get(table).createOrReplaceTempView(tempViewName);
    }

    public static void createAllTempViews() {
      Repo.createTempView("stock_basics", "basics_view");
      Repo.createTempView("stock_profit_data", "roe_view");
      Repo.createTempView("stock_cashflow_data", "cashflowratio_view");
      Repo.createTempView("stock_debtpay_data", "currentratio_view");
      Repo.createTempView("stock_growth_data", "epsg_view");
      Repo.createTempView("stock_operation_data", "turnover_view");
    }

    public static Dataset<Row> sql(String sql) {
      return SPARK.sql(sql);
    }

    public static void write(Row row) throws SQLException {
      String code = row.getString(0);
      long year = row.getLong(1);
      statement.setString(1, code); // code
      statement.setDouble(2, row.getDouble(2)); // roe
      statement.setDouble(3, row.getDouble(3)); // cashflowratio
      statement.setDouble(4, row.getDouble(4)); // currentratio
      statement.setDouble(5, row.getDouble(5)); // epsg
      statement.setDouble(6, row.getDouble(6)); // currentasset_turnover
      statement.setLong(7, year); // year
      statement.executeUpdate();
      System.err.println(">>>>>>>>>>>>>>>>>>>> code[" + code + "]year[" + year + "] written to database.");
    }

  }

}
