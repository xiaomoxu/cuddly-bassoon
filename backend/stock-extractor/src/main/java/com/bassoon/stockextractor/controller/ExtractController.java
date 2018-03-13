package com.bassoon.stockextractor.controller;

import com.bassoon.stockextractor.component.ExportStockFromXLS;
import com.bassoon.stockextractor.component.ExportTransactionFromSohu;
import com.bassoon.stockextractor.component.ProducerService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@Deprecated
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
        if (jobName.equals("region")) {
            this.exportStockFromXLS.exportRegion();
        }
        if (jobName.equals("stocklist")) {
            this.exportStockFromXLS.importStockListExcelToDatabase();
        }
    }

    @GetMapping(value = "/stocklist/url", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<String> getStocksURL(HttpServletResponse rsp) {
        List<String> urlList = this.exportStockFromXLS.importStockListExcelToDatabase();
        rsp.addHeader("X-Total-Count", String.valueOf(urlList.size()));
        return urlList;
    }

    @RequestMapping(value = "/extract/transaction/{start}/{end}", method = RequestMethod.GET)
    public void sendTransactionData(@PathVariable String start, @PathVariable String end) {
        exportTransactionFromSohu.getStockDailyTransaction(start, end, false);
    }

}
