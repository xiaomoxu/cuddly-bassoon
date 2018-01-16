package com.bassoon.stockextractor.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfig {

    @Bean
    public Queue stockQueue() {
        return new Queue("com.bassoon.queue.stock");
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue("com.bassoon.queue.transaction");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("com.bassoon.exchange");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    public Binding bindingExchangeTopicA(Queue stockQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(stockQueue).to(topicExchange).with("com.bassoon.queue.stock");
    }

    @Bean
    public Binding bindingExchangeTopicAny(Queue transactionQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(transactionQueue).to(topicExchange).with("com.bassoon.queue.transaction");
    }

}
