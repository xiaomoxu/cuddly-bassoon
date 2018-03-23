package com.bassoon.stockanalyzer.policy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class TwoEightNode implements Serializable {
    private String date;
    private double zzclose;
    private double hsclose;
    private double hsMoney = 5000;
    private double zzMoney = 5000;

    @JsonIgnore
    private TwoEightNode previousNode = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setZzclose(double zzclose) {
        this.zzclose = zzclose;
    }

    public void setHsclose(double hsclose) {
        this.hsclose = hsclose;
    }

    public double getHsMoney() {
        return hsMoney;
    }

    public void setHsMoney(double hsMoney) {
        this.hsMoney = hsMoney;
    }

    public double getZzMoney() {
        return zzMoney;
    }

    public void setZzMoney(double zzMoney) {
        this.zzMoney = zzMoney;
    }

    public double getZzclose() {
        return zzclose;
    }

    public double getHsclose() {
        return hsclose;
    }

    public void calulateMoney() {
        if (this.previousNode != null) {
            double change = (double) ((this.zzclose - previousNode.getZzclose()) / previousNode.getZzclose());
            this.zzMoney = Math.round(previousNode.getZzMoney() * (1 + change));
            change = (double) ((this.hsclose - previousNode.getHsclose()) / previousNode.getHsclose());
            this.hsMoney = Math.round(previousNode.getHsMoney() * (1 + change));
        }
    }

    public void setPreviousNode(TwoEightNode previousNode) {
        this.previousNode = previousNode;
    }
}
