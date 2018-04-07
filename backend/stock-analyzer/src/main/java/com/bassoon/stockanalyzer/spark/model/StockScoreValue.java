package com.bassoon.stockanalyzer.spark.model;

import java.io.Serializable;

public class StockScoreValue implements Serializable {
    private String code;
    private String name;
    private int score = 0;

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
}
