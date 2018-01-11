package com.bassoon.stockextractor.job;

import com.bassoon.stockextractor.model.Market;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.rabbitmq.tools.json.JSONUtil;

@Service
public class ExtractService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${queue.name}")
    public String queueName;

    @Value("${exchange.name}")
    public String exchangeName;

    @Bean
    Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    public void commitStockData(Market market){
        String jsonStr = JsonUtils.objectToJson(market);
        rabbitTemplate.convertAndSend(this.queueName, jsonStr);
    }

}
