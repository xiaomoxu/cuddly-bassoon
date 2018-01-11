package com.bassoon.stockextractor.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class StockExtractorJob {

    public final static long ONE_Minute = 60 * 1000;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ExportFromExcelJob exportFromExcelJob;

//   /@Scheduled(fixedDelay = ONE_Minute)
    public void fetchStocksTransactionsByDuring() {
        try {
            exportFromExcelJob.exportMarket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Scheduled(cron = "0 0 21 * * ?")
    public void fetchStocksTransactionsByDaily() {
    }

}
