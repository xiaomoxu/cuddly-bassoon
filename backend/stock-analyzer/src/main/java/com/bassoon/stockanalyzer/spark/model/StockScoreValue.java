package com.bassoon.stockanalyzer.spark.model;

import java.io.Serializable;

public class StockScoreValue implements Serializable {
    private String code;
    private String name;
    private int score = 0;
    private FilterCondition filterCondition = new FilterCondition();//当前年份指标，跟查询年份相同
    private FilterCondition previousFilterCondition = new FilterCondition();//去年的的指标
    private double change;//当年的涨幅，需要根据这个排序

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = this.score + score;
    }

    public FilterCondition getFilterCondition() {
        return filterCondition;
    }

    public void setFilterCondition(FilterCondition filterCondition) {
        this.filterCondition = filterCondition;
    }

    public FilterCondition getPreviousFilterCondition() {
        return previousFilterCondition;
    }

    public void setPreviousFilterCondition(FilterCondition previousFilterCondition) {
        this.previousFilterCondition = previousFilterCondition;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}
