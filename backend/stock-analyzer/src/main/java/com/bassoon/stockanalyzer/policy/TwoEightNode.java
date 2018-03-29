package com.bassoon.stockanalyzer.policy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class TwoEightNode implements Serializable {
    private String date;
    private double zzclose;
    private double hsclose;
    private double hsMoney = 5000;
    private double zzMoney = 5000;
    private double rotationMoney = 5000;
    private double rotationMoeny2 = 5000;
    private int hold1 = -1;//HS=0 ZZ=1 空仓=-1
    private int hold2 = -1;//HS=0 ZZ=1 空仓=-1

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

    public void calulateBasicMoney() {
        if (this.previousNode != null) {
            double change = (double) ((this.zzclose - previousNode.getZzclose()) / previousNode.getZzclose());
            this.zzMoney = Math.round(previousNode.getZzMoney() * (1 + change));
            change = (double) ((this.hsclose - previousNode.getHsclose()) / previousNode.getHsclose());
            this.hsMoney = Math.round(previousNode.getHsMoney() * (1 + change));
        }
    }

    //计算空仓市值
    public void calculateAdvancedMoney2(TwoEightNode compareNode) {
        //从第一个时间周期开始计算
        double zz_change = (double) ((this.getZzclose() - compareNode.getZzclose()) / compareNode.getZzclose());
        double hs_change = (double) ((this.getHsclose() - compareNode.getHsclose()) / compareNode.getHsclose());
//                this.zzMoney = Math.round(previousNode.getZzMoney() * (1 + change));\
        if (zz_change > hs_change) {//如果中证4周的涨幅 大于 沪深4周涨幅，
            if (zz_change < 0) {//虽然ZZ涨幅高，但是还是下跌，那么选择空仓
                this.setHold2(-1);
            } else {
                this.setHold2(1);//那么肯定要持有中证
            }
            switch (previousNode.getHold2()) {
                case -1://前三周不计算，从第四周开始算;如果上周选择空仓，那么本周不进行利润计算
                    this.setRotationMoeny2(previousNode.getRotationMoeny2());
                    break;
                case 0:
                    double hschange = (double) ((this.getHsclose() - previousNode.getHsclose()) / previousNode.getHsclose());
                    this.setRotationMoeny2(Math.round(previousNode.getRotationMoeny2() * (1 + hschange)));
                    break;
                case 1://继续持有
                    double zzchange = (double) ((this.getZzclose() - previousNode.getZzclose()) / previousNode.getZzclose());
                    this.setRotationMoeny2(Math.round(previousNode.getRotationMoeny2() * (1 + zzchange)));
                    break;
            }
        }
        if (hs_change > zz_change) {
            if (hs_change < 0) {
                this.setHold2(-1);
            } else {
                this.setHold2(0);//那么肯定要持有沪深
            }
            switch (previousNode.getHold2()) {
                case -1:
                    this.setRotationMoeny2(previousNode.getRotationMoeny2());
                    break;
                case 0:
                    double hschange = (double) ((this.getHsclose() - previousNode.getHsclose()) / previousNode.getHsclose());
                    this.setRotationMoeny2(Math.round(previousNode.getRotationMoeny2() * (1 + hschange)));
                    break;
                case 1:
                    double zzchange = (double) ((this.getZzclose() - previousNode.getZzclose()) / previousNode.getZzclose());
                    this.setRotationMoeny2(Math.round(previousNode.getRotationMoeny2() * (1 + zzchange)));
                    break;
            }
        }
    }

    public void calculateAdvancedMoney(TwoEightNode compareNode) {
        //从第一个时间周期开始计算
        double zz_change = (double) ((this.getZzclose() - compareNode.getZzclose()) / compareNode.getZzclose());
        double hs_change = (double) ((this.getHsclose() - compareNode.getHsclose()) / compareNode.getHsclose());
//                this.zzMoney = Math.round(previousNode.getZzMoney() * (1 + change));\
        if (zz_change > hs_change) {//如果中证4周的涨幅 大于 沪深4周涨幅，
            this.setHold1(1);//那么肯定要持有中证
            switch (previousNode.getHold1()) {
                case -1://前三周不计算，从第四周开始算
                    break;
                case 0:
                    double hschange = (double) ((this.getHsclose() - previousNode.getHsclose()) / previousNode.getHsclose());
                    this.setRotationMoney(Math.round(previousNode.getRotationMoney() * (1 + hschange)));
                    break;
                case 1://继续持有
                    double zzchange = (double) ((this.getZzclose() - previousNode.getZzclose()) / previousNode.getZzclose());
                    this.setRotationMoney(Math.round(previousNode.getRotationMoney() * (1 + zzchange)));
                    break;
            }
        }
        if (hs_change > zz_change) {
            this.setHold1(0);
            switch (previousNode.getHold1()) {
                case -1:
                    this.setRotationMoney(5000);
                    break;
                case 0:
                    double hschange = (double) ((this.getHsclose() - previousNode.getHsclose()) / previousNode.getHsclose());
                    this.setRotationMoney(Math.round(previousNode.getRotationMoney() * (1 + hschange)));
                    break;
                case 1:
                    double zzchange = (double) ((this.getZzclose() - previousNode.getZzclose()) / previousNode.getZzclose());
                    this.setRotationMoney(Math.round(previousNode.getRotationMoney() * (1 + zzchange)));
                    break;
            }
        }
    }

    public void setPreviousNode(TwoEightNode previousNode) {
        this.previousNode = previousNode;
    }

    public double getRotationMoney() {
        return rotationMoney;
    }

    public void setRotationMoney(double rotationMoney) {
        this.rotationMoney = rotationMoney;
    }

    public int getHold1() {
        return hold1;
    }

    public void setHold1(int hold) {
        this.hold1 = hold;
    }

    public double getRotationMoeny2() {
        return rotationMoeny2;
    }

    public void setRotationMoeny2(double rotationMoeny2) {
        this.rotationMoeny2 = rotationMoeny2;
    }

    public int getHold2() {
        return hold2;
    }

    public void setHold2(int hold2) {
        this.hold2 = hold2;
    }
}
