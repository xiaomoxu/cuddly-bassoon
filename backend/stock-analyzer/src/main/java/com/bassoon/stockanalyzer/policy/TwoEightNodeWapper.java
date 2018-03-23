package com.bassoon.stockanalyzer.policy;

import java.util.List;

public class TwoEightNodeWapper {
    private List<TwoEightNode> hsNodeList;

    private List<TwoEightNode> zzNodeList;

    private List<TwoEightNode> rotationNodeList;

    public List<TwoEightNode> getHsNodeList() {
        return hsNodeList;
    }

    public void setHsNodeList(List<TwoEightNode> hsNodeList) {
        this.hsNodeList = hsNodeList;
    }

    public List<TwoEightNode> getZzNodeList() {
        return zzNodeList;
    }

    public void setZzNodeList(List<TwoEightNode> zzNodeList) {
        this.zzNodeList = zzNodeList;
    }

    public List<TwoEightNode> getRotationNodeList() {
        return rotationNodeList;
    }

    public void setRotationNodeList(List<TwoEightNode> rotationNodeList) {
        this.rotationNodeList = rotationNodeList;
    }
}
