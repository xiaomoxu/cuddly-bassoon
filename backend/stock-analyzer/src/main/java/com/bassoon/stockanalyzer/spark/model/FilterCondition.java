package com.bassoon.stockanalyzer.spark.model;

public class FilterCondition {
    private double roe;//净资产收益率 来源于 stock_profit_data  roe > 0  按照4个季度总和计算
    private double grossProfitRatio;//毛利率 stock_profit_data  roe  current > last year 按照4个季度中和
    private double cashFlowRatio;//现金流量比率  来源于stock_cashflow_data  cashflowratio (Operating Cash Flow Ratio)  > 0 按照4个季度总和
    private double debtCurrentRatio;//* current > last year 4个季度总和 stock_debtpay_data
    private double epsg;//每股收益增长率 current > last year > 0 4季度总和    stock_growth_data
    private double currentAssetTurnover; //资产周转率 stock_operation_data  current > last year 按照4个季度总和

    public double getRoe() {
        return roe;
    }

    public void setRoe(double roe) {
        this.roe = roe;
    }

    public double getCashFlowRatio() {
        return cashFlowRatio;
    }

    public void setCashFlowRatio(double cashFlowRatio) {
        this.cashFlowRatio = cashFlowRatio;
    }

    public double getDebtCurrentRatio() {
        return debtCurrentRatio;
    }

    public void setDebtCurrentRatio(double debtCurrentRatio) {
        this.debtCurrentRatio = debtCurrentRatio;
    }

    public double getGrossProfitRatio() {
        return grossProfitRatio;
    }

    public void setGrossProfitRatio(double grossProfitRatio) {
        this.grossProfitRatio = grossProfitRatio;
    }

    public double getCurrentAssetTurnover() {
        return currentAssetTurnover;
    }

    public void setCurrentAssetTurnover(double currentAssetTurnover) {
        this.currentAssetTurnover = currentAssetTurnover;
    }

    public double getEpsg() {
        return epsg;
    }

    public void setEpsg(double epsg) {
        this.epsg = epsg;
    }
}
