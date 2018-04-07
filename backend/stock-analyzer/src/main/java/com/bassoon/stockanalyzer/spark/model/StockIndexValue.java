package com.bassoon.stockanalyzer.spark.model;

import java.io.Serializable;

public class StockIndexValue implements Serializable {
    private String name;
    private String code;
    private double change;
    private double close;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }
}
