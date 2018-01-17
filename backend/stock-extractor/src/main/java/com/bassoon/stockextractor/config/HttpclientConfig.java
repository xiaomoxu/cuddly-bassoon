package com.bassoon.stockextractor.config;


import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpclientConfig {
    @Bean
    public HttpClient httpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

        // Get the poolMaxTotal value from our application[-?].yml or default to 10 if not explicitly set
        connectionManager.setMaxTotal(10);
        return HttpClientBuilder
                .create()
                .setConnectionManager(connectionManager)
                .build();
    }
}
