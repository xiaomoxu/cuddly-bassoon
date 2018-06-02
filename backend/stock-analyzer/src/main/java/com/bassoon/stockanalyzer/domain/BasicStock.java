package com.bassoon.stockanalyzer.domain;

import java.io.Serializable;

public class BasicStock implements Serializable {
    private double roe;
    private double grossProfitRate;

    public double getRoe() {
        return roe;
    }

    public void setRoe(double roe) {
        this.roe = roe;
    }

    public double getGrossProfitRate() {
        return grossProfitRate;
    }

    public void setGrossProfitRate(double grossProfitRate) {
        this.grossProfitRate = grossProfitRate;
    }
}
