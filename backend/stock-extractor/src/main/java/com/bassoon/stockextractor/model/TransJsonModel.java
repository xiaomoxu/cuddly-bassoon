package com.bassoon.stockextractor.model;

import java.util.List;

/**
 * @author xxu
 * 这个类对应 stock url返回的json格式
 * {"status":0,"
 * hq":[["2018-01-05","7.17","7.16","-0.07","-0.97%","7.09","7.25","1787036","128064.36","1.38%"],
 * ["2018-01-04","6.98","7.23","0.18","2.55%","6.97","7.24","2147914","152820.50","1.66%"],
 * ["2018-01-03","6.96","7.05","0.11","1.59%","6.94","7.28","2747951","196348.83","2.12%"],
 * ["2018-01-02","6.90","6.94","0.06","0.87%","6.82","6.99","1629839","112821.98","1.26%"]],"
 * code":"cn_603993","
 * stat":["累计:","2018-01-02至2018-01-05","0.28","4.07%",6.82,7.28,8312740,590055.67,"6.42%"]}
 */
public class TransJsonModel {
    private int status;
    private List<List<String>> hq;
    private String code;
    private List<String> stat;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setHq(List<List<String>> hq) {
        this.hq = hq;
    }

    public List<List<String>> getHq() {
        return hq;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setStat(List<String> stat) {
        this.stat = stat;
    }

    public List<String> getStat() {
        return stat;
    }

}

