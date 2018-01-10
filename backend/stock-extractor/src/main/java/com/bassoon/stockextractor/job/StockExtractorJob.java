package com.bassoon.stockextractor.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StockExtractorJob {

    public final static long ONE_Minute = 60 * 1000;

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(fixedRate = ONE_Minute)
    public void fetchStocksTransactionsByDuring() {
        System.out.println("test one minutes!");
    }

    @Scheduled(cron = "0 0 21 * * ?")
    public void fetchStocksTransactionsByDaily() {
;
    }
}
