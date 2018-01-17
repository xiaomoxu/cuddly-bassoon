package com.bassoon.stockanalyzer.service;

import com.bassoon.stockanalyzer.domain.Market;
import com.bassoon.stockanalyzer.domain.Stock;
import com.bassoon.stockanalyzer.mapper.MarketMapper;
import com.bassoon.stockanalyzer.mapper.StockMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @Autowired
    private MarketMapper marketMapper;

    @Autowired
    private StockMapper stockMapper;

    /**
     * 从队里里面获取股票的基础数据
     *
     * @param message
     */
    @RabbitListener(queues = "com.bassoon.queue.stock")
    public void processStock(String message) {
        if (message.startsWith("market")) {
            message = message.substring(6);
            Market market = JsonUtils.jsonToObject(message, null, Market.class);
            marketMapper.save(market);
        }

        if (message.startsWith("stock")) {
            message = message.substring(5);
            Stock stock = JsonUtils.jsonToObject(message, null, Stock.class);
//            System.out.println(stock.getCode());
            stockMapper.save(stock);
        }

    }

    /**
     * 从队列里面获取股票的每日交易数据
     *
     * @param message
     */
    @RabbitListener(queues = "com.bassoon.queue.transaction")
    public void processStransaction(String message) {
//        Stock stock = JsonUtils.jsonToObject(message, null, Stock.class);
//        stockMapper.save(stock);
    }
}
