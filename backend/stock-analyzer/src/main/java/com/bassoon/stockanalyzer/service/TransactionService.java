package com.bassoon.stockanalyzer.service;

import com.bassoon.stockanalyzer.domain.Transaction;
import com.bassoon.stockanalyzer.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private StockService stockService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TransactionMapper transactionMapper;

    private List<String> haveDoneCodeList = new ArrayList<String>();

    public void importTransaction(Transaction transaction) {
        if (!this.isHaveDone(transaction.getStockCode())) {
            transactionMapper.save(transaction);
        }
    }

    private boolean isHaveDone(String stockCode) {
        if (haveDoneCodeList.contains(stockCode)) {
            System.err.println(stockCode + " has already done! It is a duplicate record!");
            return true;
        }
        this.haveDoneCodeList.add(stockCode);
        return false;
    }
}
