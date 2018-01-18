package com.bassoon.stockextractor.component;

import com.bassoon.stockextractor.model.Stock;
import com.bassoon.stockextractor.model.StockListWrapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author xxu
 * Rabbit MQ 组件
 */
@Service
public class ProducerService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExportTransactionFromSohu exportTransactionFromSohu;

    public void sendStockMessage(String msg) {
        rabbitTemplate.convertAndSend("topicExchange", "com.bassoon.queue.stock", msg);
    }

    public void sendTransactionMessage(String msg) {
        rabbitTemplate.convertAndSend("topicExchange", "com.bassoon.queue.transaction", msg);
    }

}
