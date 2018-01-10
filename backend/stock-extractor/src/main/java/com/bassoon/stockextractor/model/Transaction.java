package com.bassoon.stockextractor.model;

import java.io.Serializable;

//"2018-01-09","70.30","71.87","1.86","2.66%","70.03","72.08","1239491","884674.06","1.14%"
//
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
    private int dailyTransTurnover;//今日成交金额
    private float changeRate;//换手率
}
