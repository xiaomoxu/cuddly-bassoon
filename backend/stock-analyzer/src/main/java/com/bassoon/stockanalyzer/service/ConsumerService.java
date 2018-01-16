package com.bassoon.stockanalyzer.service;

import com.bassoon.stockanalyzer.domain.Market;
import com.bassoon.stockanalyzer.mapper.MarketMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @Autowired
    private MarketMapper marketMapper;

    @RabbitListener(queues = "com.bassoon.queue.stock")
    public void process(String message) {
        Market market = JsonUtils.jsonToObject(message, null, Market.class);
        marketMapper.insertMarket(market);
    }
}
