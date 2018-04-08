/**
 * Copyright (c) 2015-2016 Ryan Li Wan. All rights reserved.
 */

package com.bassoon.stockanalyzer.spark.test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
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
//    currentasset_turnover_summing();
//    currentratio_cleanup();
    joined_summing();
  }

  public static void currentasset_turnover_summing() {
    Repo.createTempView("stock_operation_data", "turnover_view");
    Dataset<Row> turnover = Repo.sql("SELECT code, currentasset_turnover, year, quarter FROM turnover_view WHERE code = 002116");
    Dataset<Row> summed = turnover
        .groupBy("code", "year")
        .sum("currentasset_turnover");
    summed.orderBy("code", "year").show(100);
  }

  public static void joined_summing() {

    // Create temp views

    Repo.createTempView("stock_basics", "basics_view");
    Repo.createTempView("stock_profit_data", "roe_view");
    Repo.createTempView("stock_cashflow_data", "cashflowratio_view");
//    Repo.createTempView("stock_debtpay_data", "currentratio_view");
    Repo.createTempView("stock_growth_data", "epsg_view");
    Repo.createTempView("stock_operation_data", "turnover_view");

    // Create initial datasets

    Dataset<Row> basics = Repo.sql("SELECT code FROM basics_view WHERE code = 002116");
    Dataset<Row> roe = Repo.sql("SELECT code, roe, year, quarter FROM roe_view");
    Dataset<Row> cashflowration = Repo.sql("SELECT code, cashflowratio, year, quarter FROM cashflowratio_view");
//    Dataset<Row> currentratio = Repo.sql("SELECT code, currentratio, year, quarter FROM currentratio_view");
    Dataset<Row> epsg = Repo.sql("SELECT code, epsg, year, quarter FROM epsg_view");
    Dataset<Row> turnover = Repo.sql("SELECT code, currentasset_turnover, year, quarter FROM turnover_view");

    // Clean up datasets (currentratio)

//    currentratio = currentratio.map(new MapFunction<Row, Row>() {
//      @Override
//      public Row call(Row row) throws Exception {
//        String code = row.getAs("code");
//        String currentratio = row.getAs("currentratio");
//        if (currentratio.equals("--")) {
//          currentratio = "0";
//        }
//        String year = row.getAs("year");
//        String quarter = row.getAs("quarter");
//        return RowFactory.create(code, currentratio, year, quarter);
//      }
//    }, RowEncoder.apply(currentratio.schema()));

    // Joinning datasets

    Seq<String> columns = JavaConversions.asScalaBuffer(Arrays.asList("code", "year", "quarter")).toSeq();

    Dataset<Row> joined = basics.join(roe, "code");
    joined = joined.join(cashflowration, columns);
//    joined = joined.join(currentratio, columns);
    joined = joined.join(epsg, columns);
    joined = joined.join(turnover, columns);

//    joined.orderBy("code", "year", "quarter").show(100);


    // Summing

    Dataset<Row> summed = joined
            .groupBy("code", "year")
            .sum("roe", "cashflowratio", "epsg", "currentasset_turnover");

    summed.orderBy("code", "year").show(100);

    // Encoding

//    Dataset<StockSumming> stock = summed.as(Encoders.bean(StockSumming.class));
  }

  public static void currentratio_cleanup() {
    Repo.createTempView("stock_debtpay_data", "currentratio_view");
    Dataset<Row> currentratio = Repo.sql("SELECT code, currentratio, year, quarter FROM currentratio_view WHERE code = 002116");

    Dataset<Debtpay> debtpayDataset = currentratio.map(new MapFunction<Row, Debtpay>() {
      @Override
      public Debtpay call(Row row) throws Exception {
        String code = row.getAs("code");
        String currentratio = row.getAs("currentratio");
        if (currentratio.equals("--")) {
          currentratio = "0";
        }
        String year = row.getAs("year");
        String quarter = row.getAs("quarter");
        return new Debtpay(code, currentratio, year, quarter);
      }
    }, Encoders.bean(Debtpay.class));

    currentratio.orderBy("code", "year", "quarter").show(100);
  }

  public static class Debtpay implements Serializable {

    private String code;
    private String currentratio;
    private String year;
    private String quarter;

    public Debtpay(String code, String currentratio, String year, String quarter) {
      this.code = code;
      this.currentratio = currentratio;
      this.year = year;
      this.quarter = quarter;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getCode() {
      return code;
    }

    public void setCurrentratio(String currentratio) {
      this.currentratio = currentratio;
    }

    public String getCurrentratio() {
      return currentratio;
    }

    public void setYear(String year) {
      this.year = year;
    }

    public String getYear() {
      return year;
    }

    public void setQuarter(String quarter) {
      this.quarter = quarter;
    }

    public String getQuarter() {
      return quarter;
    }

  }

  public static class Repo {

    private static final String URL =
        "jdbc:mysql://10.20.116.107:3306/CN_BASSOON?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
    private static final Properties PROPERTIES;
    private static final SparkSession SPARK;

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
      ).getOrCreate();
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

    public static Dataset<Row> sql(String sql) {
      return SPARK.sql(sql);
    }

  }

}
