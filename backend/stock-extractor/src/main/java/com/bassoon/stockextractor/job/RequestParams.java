package com.bassoon.stockextractor.job;

/**
 * @author xxu
 */
public class RequestParams {
    private String stockCode;
    private String startData;
    private String endData;
    private int stat = 1;
    private String order = "D";
    private String piorid = "d";
    private String callback = "historySearchHandler";
    private String responseTemplate = "jsonp";
}
