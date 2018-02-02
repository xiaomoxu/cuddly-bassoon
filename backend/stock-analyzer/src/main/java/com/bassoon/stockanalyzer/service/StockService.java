package com.bassoon.stockanalyzer.service;

import com.bassoon.stockanalyzer.domain.Stock;
import com.bassoon.stockanalyzer.mapper.StockMapper;
import com.bassoon.stockanalyzer.model.PageResult;
import com.bassoon.stockanalyzer.wrapper.StockListWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@Service
public class StockService {
    @Resource
    private StockMapper stockMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取所有股票数据，并保存在redis中
     * 但是如果redis服务不可用，那么这个方法就不可用，
     * 也就是说这个api无法达到高可用的目的
     *
     * @return
     */
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

    /**
     * 把所有股票根据股票编号去重，因为有些股票既在50里面又在300里面，所以暴露给外面重复的股票数据不具备任何意义
     * 另外股票交易的数据也会因此而产生重复记录
     *
     * @return
     */
    public PageResult<Stock> getStocksRemoveDuplicateByCode(int page, int limit) {
        ValueOperations<String, List<Stock>> ops = this.redisTemplate.opsForValue();
        String key = page + "_get-stock-unduplicate";
        List<Stock> stocks = new LinkedList<Stock>();
        PageResult<Stock> result = new PageResult<Stock>();
        if (!this.redisTemplate.hasKey(key)) {
            PageHelper.startPage(page, limit);
            List<String> stockCodeList = stockMapper.getStockCodeAndRemoveDuplicate();
            long total = ((Page) stockCodeList).getTotal();
            result.setPage(page);
            result.setLimit(limit);
            result.setTotal(total);
            Stock stock = null;
            for (String code : stockCodeList) {
                stock = findUniqueStock(code);
                if (stock == null) continue;
                stocks.add(stock);
            }
            result.setResult(stocks);
            ops.set(key, stocks);
        } else {
            stocks = ops.get(key);
        }
        return result;
    }

    /**
     * 因为findStockByCode可能返回两条结果集 而且是一样的结果集 那么只取第一条就可以了
     * 具体原因参考他的方法注释
     *
     * @param code
     * @return
     */
    public Stock findUniqueStock(String code) {
        List<Stock> stockList = this.stockMapper.findStockByCode(code);
        if (stockList != null && stockList.size() > 0) {
            return stockList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据是股票的所属成分板块进行查询HS300 ZZ500 SZ50
     *
     * @param belongTo
     * @return
     */
    public PageResult<Stock> getStocksByBelongTo(String belongTo, int page, int limit) {
        PageResult<Stock> stockPageResult = new PageResult<Stock>();
        PageHelper.startPage(page, limit);
        List<Stock> stocks = null;
        if (belongTo.equals("union")) {
            stocks = this.stockMapper.getStocksInSZ50AndHS300();
        } else {
            stocks = this.stockMapper.findStocksByBeloneTo(belongTo);
        }
        stockPageResult.setResult(stocks);
        long total = ((Page) stocks).getTotal();
        stockPageResult.setPage(page);
        stockPageResult.setLimit(limit);
        stockPageResult.setTotal(total);
        return stockPageResult;
    }
}
