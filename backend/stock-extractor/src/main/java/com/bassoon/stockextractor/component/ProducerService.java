package com.bassoon.stockextractor.component;

import com.bassoon.stockextractor.config.ProducerConfig;
import com.bassoon.stockextractor.job.JsonUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author xxu
 * Rabbit MQ 组件
 */
@Service
public class ProducerService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendStockMessage(String msg) {
        rabbitTemplate.convertAndSend("topicExchange", "com.bassoon.queue.stock", msg);
    }

    public void sendTransactionMessage(String msg) {
        rabbitTemplate.convertAndSend("topicExchange", "com.bassoon.queue.transaction", msg);
    }
}
