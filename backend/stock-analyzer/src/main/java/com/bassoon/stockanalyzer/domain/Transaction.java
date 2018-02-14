package com.bassoon.stockanalyzer.domain;

import java.io.Serializable;
import java.util.Date;

//"2018-01-09","70.30","71.87","1.86","2.66%","70.03","72.08","1239491","884674.06","1.14%"
//

/**
 * @author xxu
 * 股票 每天的交易信息 日K
 * 这个类未来将被TransactionHistory取代
 */
@Deprecated
public class Transaction implements Serializable {
    private Long id;//pk
    private String stockCode;//股票盘编号 stock_code
    private Date tansDate;//交易日期 trans_date
    private double openPrice;//开盘价 open_price
    private double closePrice;//收盘价 close_price
    private double changeAmount;//涨跌额 change_amount
    private double change;//涨跌百分比 change_range
    private double LowPrice;//今日最低价 low_price
    private double highPrice;//今日最高价 high_price
    private int dailyTransVolume;//今日成交量 daily_trans_volume
    private double dailyTransTurnover;//今日成交金额 daily_trans_turnover
    private double changeRate;//换手率 change_rate

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Date getTansDate() {
        return tansDate;
    }

    public void setTansDate(Date tansDate) {
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
