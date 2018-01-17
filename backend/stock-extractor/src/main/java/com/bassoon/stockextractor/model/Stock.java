package com.bassoon.stockextractor.model;

import com.bassoon.stockextractor.utils.StockUtils;

import java.io.Serializable;

public class Stock implements Serializable {
    private Long id;//主键key
    private String code;//股票编号
    private String name;//股票名称
    private String market;//行业
    private String region;//地区
    private String belongTo;//中证50/上证500//沪深300
    private double weight;//所占当前板块的权重
    private double currentPrice;//最新价格
    private double eps;//每股收益(元)
    private double bvps;//每股净资产(元)
    private double roe;//净资产收益率(%)
    private double totalStock;//总股本(亿股)
    private double liqui;//流通股本(亿股)
    private double ltsz;//流通市值(亿元)
    private String information_url = "http://f10.eastmoney.com/f10_v2/CompanySurvey.aspx?code=";
    ;//档案链接,可以继续抓相关的信息

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        this.information_url = information_url + StockUtils.getMktByCode(this.code) + code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public double getBvps() {
        return bvps;
    }

    public void setBvps(double bvps) {
        this.bvps = bvps;
    }

    public double getRoe() {
        return roe;
    }

    public void setRoe(double roe) {
        this.roe = roe;
    }

    public double getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(double totalStock) {
        this.totalStock = totalStock;
    }

    public double getLiqui() {
        return liqui;
    }

    public void setLiqui(double liqui) {
        this.liqui = liqui;
    }

    public double getLtsz() {
        return ltsz;
    }

    public void setLtsz(double ltsz) {
        this.ltsz = ltsz;
    }

    public String getInformation_url() {
        return information_url;
    }

    public void setInformation_url(String information_url) {
        this.information_url = information_url;
    }


    @Override
    public String toString() {
        return "Stock{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", market='" + market + '\'' +
                ", region='" + region + '\'' +
                ", belongTo='" + belongTo + '\'' +
                ", weight=" + weight +
                ", currentPrice=" + currentPrice +
                ", eps=" + eps +
                ", bvps=" + bvps +
                ", roe=" + roe +
                ", totalStock=" + totalStock +
                ", liqui=" + liqui +
                ", ltsz=" + ltsz +
                ", information_url='" + information_url + '\'' +
                '}';
    }
}
