package com.bassoon.stockanalyzer.spark.model;

import java.io.Serializable;

public class AccumulateValue implements Serializable{
    private String date;
    private double value;

    public AccumulateValue(String date, double value){
        this.date = date;
        this.value = value;
    }

    public AccumulateValue() {
        this.date = "";
        this.value = 0;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
