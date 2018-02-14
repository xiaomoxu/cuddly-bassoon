package com.bassoon.stockanalyzer.domain;

import java.io.Serializable;
import java.util.Date;

public class StockHistory implements Serializable {
    private String stockCode;
    private Date date; //：日期
    private double open;//：开盘价
    private double high;//：最高价
    private double close;//：收盘价
    private double low;//：最低价
    private double volume;//：成交量
    private double price_change;//：价格变动
    private double p_change;//：涨跌幅
    private double ma5;//：5日均价
    private double ma10;//：10日均价
    private double ma20;//:20日均价
    private double v_ma5;//:5日均量
    private double v_ma10;//:10日均量
    private double v_ma20;//:20日均量
    private double turnover;//:换手率[注：指数无此项]

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getPrice_change() {
        return price_change;
    }

    public void setPrice_change(double price_change) {
        this.price_change = price_change;
    }

    public double getP_change() {
        return p_change;
    }

    public void setP_change(double p_change) {
        this.p_change = p_change;
    }

    public double getMa5() {
        return ma5;
    }

    public void setMa5(double ma5) {
        this.ma5 = ma5;
    }

    public double getMa10() {
        return ma10;
    }

    public void setMa10(double ma10) {
        this.ma10 = ma10;
    }

    public double getMa20() {
        return ma20;
    }

    public void setMa20(double ma20) {
        this.ma20 = ma20;
    }

    public double getV_ma5() {
        return v_ma5;
    }

    public void setV_ma5(double v_ma5) {
        this.v_ma5 = v_ma5;
    }

    public double getV_ma10() {
        return v_ma10;
    }

    public void setV_ma10(double v_ma10) {
        this.v_ma10 = v_ma10;
    }

    public double getV_ma20() {
        return v_ma20;
    }

    public void setV_ma20(double v_ma20) {
        this.v_ma20 = v_ma20;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    @Override
    public String toString() {
        return "TransactionHistory{" +
                "date=" + date +
                ", open=" + open +
                ", high=" + high +
                ", close=" + close +
                ", low=" + low +
                ", volume=" + volume +
                ", price_change=" + price_change +
                ", p_change=" + p_change +
                ", ma5=" + ma5 +
                ", ma10=" + ma10 +
                ", ma20=" + ma20 +
                ", v_ma5=" + v_ma5 +
                ", v_ma10=" + v_ma10 +
                ", v_ma20=" + v_ma20 +
                ", turnover=" + turnover +
                '}';
    }
}
