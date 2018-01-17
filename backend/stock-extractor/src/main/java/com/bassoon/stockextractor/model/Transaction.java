package com.bassoon.stockextractor.model;

import java.io.Serializable;

//"2018-01-09","70.30","71.87","1.86","2.66%","70.03","72.08","1239491","884674.06","1.14%"
//

/**
 * @author xxu
 * 股票 每天的交易信息 日K
 */
public class Transaction implements Serializable {
    private String stockCode;//股票盘编号
    private String tansDate;//交易日期
    private double openPrice;//开盘价
    private double closePrice;//收盘价
    private double changeAmount;//涨跌额
    private double change;//涨跌百分比
    private double LowPrice;//今日最低价
    private double highPrice;//今日最高价
    private int dailyTransVolume;//今日成交量
    private double dailyTransTurnover;//今日成交金额
    private double changeRate;//换手率

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getTansDate() {
        return tansDate;
    }

    public void setTansDate(String tansDate) {
        this.tansDate = tansDate;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getLowPrice() {
        return LowPrice;
    }

    public void setLowPrice(double lowPrice) {
        LowPrice = lowPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public int getDailyTransVolume() {
        return dailyTransVolume;
    }

    public void setDailyTransVolume(int dailyTransVolume) {
        this.dailyTransVolume = dailyTransVolume;
    }

    public double getDailyTransTurnover() {
        return dailyTransTurnover;
    }

    public void setDailyTransTurnover(double dailyTransTurnover) {
        this.dailyTransTurnover = dailyTransTurnover;
    }

    public double getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(double changeRate) {
        this.changeRate = changeRate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "stockCode='" + stockCode + '\'' +
                ", tansDate='" + tansDate + '\'' +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", changeAmount=" + changeAmount +
                ", change=" + change +
                ", LowPrice=" + LowPrice +
                ", highPrice=" + highPrice +
                ", dailyTransVolume=" + dailyTransVolume +
                ", dailyTransTurnover=" + dailyTransTurnover +
                ", changeRate=" + changeRate +
                '}';
    }
}
