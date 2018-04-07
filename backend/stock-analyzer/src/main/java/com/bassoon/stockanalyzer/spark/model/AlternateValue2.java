package com.bassoon.stockanalyzer.spark.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class AlternateValue2 implements Serializable {
    private String date;
    private double close;
    private double money = 5000;

    @JsonIgnore
    private AlternateValue2 previousNode2;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void calulateMoney() {
        if (this.previousNode2 != null) {
            double change = (double) ((this.close - previousNode2.getClose()) / previousNode2.getClose());
            this.money = Math.round(previousNode2.getMoney() * (1 + change));
        }
    }

    public void setPreviousNode(AlternateValue2 previousNode) {
        this.previousNode2 = previousNode;
    }
}
