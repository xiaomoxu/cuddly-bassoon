package com.bassoon.stockanalyzer.statistic;

import java.io.Serializable;

public class StockValue implements Serializable {
    private String key;//城市名称
    private long count;//城市下有股票的数量


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
