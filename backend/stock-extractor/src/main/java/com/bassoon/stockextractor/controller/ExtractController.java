package com.bassoon.stockextractor.controller;

import com.bassoon.stockextractor.component.ExportStockFromXLS;
import com.bassoon.stockextractor.component.ExportTransactionFromSohu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ExtractController {
    @Autowired
    private ExportStockFromXLS exportStockFromXLS;

    @Autowired
    private ExportTransactionFromSohu exportTransactionFromSohu;

    @RequestMapping(value = "/extract/stock/{jobName}", method = RequestMethod.GET)
    public void sendStockData(@PathVariable String jobName) throws IOException {
        if (jobName.equals("market")) {
            this.exportStockFromXLS.exportMarket();
        }
        if (jobName.equals("HS300")) {
            this.exportStockFromXLS.exportHS300Stocks();
        }
        if (jobName.equals("SZ50")) {
            this.exportStockFromXLS.exportSZ50Stocks();
        }
        if (jobName.equals("ZZ500")) {
            this.exportStockFromXLS.exportZZ500Stocks();
        }
    }

    @RequestMapping(value = "/extract/transaction/{start}/{end}", method = RequestMethod.GET)
    public void downloadTransactionData(@PathVariable String start, @PathVariable String end) {
        exportTransactionFromSohu.getTransaction("", start, end);
    }

}
