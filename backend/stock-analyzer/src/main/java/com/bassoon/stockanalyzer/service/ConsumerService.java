package com.bassoon.stockanalyzer.service;

import com.bassoon.stockanalyzer.domain.*;
import com.bassoon.stockanalyzer.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsumerService {

    protected static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);
    @Autowired
    public MarketMapper marketMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private StockHistoryMapper stockHistoryMapper;

    /**
     * 从队里里面获取股票的基础数据
     *
     * @param message
     */
    @Deprecated
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

        if (message.startsWith("region")) {
            message = message.substring(6);
            Region region = JsonUtils.jsonToObject(message, null, Region.class);
            regionMapper.save(region);
        }
    }

    /**
     * 从队列里面获取股票的每日交易数据
     *
     * @param message
     */
    @RabbitListener(queues = "com.bassoon.queue.transaction")
    public void processTransaction(String message) {
        try {
            StringBuffer sbu = new StringBuffer();
            String[] chars = message.split(",");
            for (int i = 0; i < chars.length; i++) {
                sbu.append((char) Integer.parseInt(chars[i]));
            }
            String value = sbu.toString();
            String code = value.substring(0, 6);
            String jsonBody = value.substring(7, value.length());
            System.out.println(code);
            System.out.println(jsonBody);
            sbu = null;
            List<StockHistory> historyList = JsonUtils.jsonToObject(jsonBody, ArrayList.class, StockHistory.class);
            for (StockHistory history : historyList) {
//            System.out.println(history.getVolume());
                history.setStockCode(code);
                this.stockHistoryMapper.save(history);
//            logger.debug(history.getVolume() + "");
            }
//        Transaction transaction = JsonUtils.jsonToObject(message, null, Transaction.class);
//        transactionService.importTransaction(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
