package com.bassoon.stockanalyzer.policy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class TwoEightNode implements Serializable {
    private double money = 5000;//当前资产,初始投入资金5000元
    private String date;
    private Double close;
    private TwoEightNode previousNode;

    public TwoEightNode(Double close, String date) {
        this.close = close;
        this.date = date;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void calulateMoney() {
        if (this.previousNode != null) {
            double change = (double) ((this.close - previousNode.getClose()) / previousNode.getClose());
            this.money = Math.round(previousNode.getMoney() * (1 + change));
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    @JsonIgnore
    public TwoEightNode getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(TwoEightNode previousNode) {
        this.previousNode = previousNode;
    }
}
