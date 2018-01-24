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


    public void importTransaction(Transaction transaction) {
        transactionMapper.save(transaction);
    }

}
