package com.bassoon.stockanalyzer.service;

import com.bassoon.stockanalyzer.domain.Stock;
import com.bassoon.stockanalyzer.mapper.StockMapper;
import com.bassoon.stockanalyzer.wrapper.StockListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StockService {
    @Resource
    private StockMapper stockMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    public StockListWrapper getAllStock() {
        ValueOperations<String, StockListWrapper> ops = this.redisTemplate.opsForValue();
        String key = "get-all-stock";
        StockListWrapper stocks = null;
        if (!this.redisTemplate.hasKey(key)) {
            List<Stock> stockList = stockMapper.getAll();
            stocks = new StockListWrapper();
            stocks.setStockList(stockList);
            ops.set(key, stocks);
        } else {
            stocks = ops.get(key);
        }
        return stocks;
    }
}
