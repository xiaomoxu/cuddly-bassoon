package com.bassoon.stockanalyzer.service;

import com.bassoon.stockanalyzer.domain.BaseModel;
import com.bassoon.stockanalyzer.domain.Market;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

    @Value("${queue.name}")
    public String queueName;

    @Value("${exchange.name}")
    public String exchangeName;

    public void receiveMessageFromRabbitMQ(String jsonString) {
        System.out.println(jsonString);
        BaseModel baseModel = JsonUtils.jsonToObject(jsonString, null, BaseModel.class);
        System.out.println(baseModel.identity);
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("item-queue-key");
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public SimpleMessageListenerContainer listenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(this.queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(RabbitMQService rabbitMQService) {
        return new MessageListenerAdapter(this, "receiveMessageFromRabbitMQ");
    }

}
