package com.bassoon.stockanalyzer.service;

import com.bassoon.stockanalyzer.domain.Stock;
import com.bassoon.stockanalyzer.mapper.StockMapper;
import com.bassoon.stockanalyzer.wrapper.StockListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StockService {
    @Resource
    private StockMapper stockMapper;

    @Autowired
    private RestTemplate restTemplate;

    public StockListWrapper getAllStock() {
        List<Stock> stockList = stockMapper.getAll();
        StockListWrapper stocks = new StockListWrapper();
        stocks.setStockList(stockList);
        return stocks;
    }
}
