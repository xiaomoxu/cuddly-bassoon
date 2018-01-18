package com.bassoon.stockanalyzer.wrapper;

import com.bassoon.stockanalyzer.domain.Stock;

import java.util.List;

public class StockListWrapper {
    private List<Stock> stockList;

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }
}
