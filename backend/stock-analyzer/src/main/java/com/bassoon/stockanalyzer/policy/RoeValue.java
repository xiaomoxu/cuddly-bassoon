package com.bassoon.stockanalyzer.policy;

import java.io.Serializable;

public class RoeValue implements Serializable {
    private double roe;
    private int year;
    private int quarter;

    public double getRoe() {
        return roe;
    }

    public void setRoe(double roe) {
        this.roe = roe;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }
}
