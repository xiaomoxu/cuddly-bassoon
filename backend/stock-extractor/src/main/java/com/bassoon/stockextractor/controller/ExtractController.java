package com.bassoon.stockextractor.controller;

import com.bassoon.stockextractor.component.ExportStockFromXLS;
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

    @RequestMapping(value = "/extract/{jobName}" , method = RequestMethod.GET)
    public void sendStockData(@PathVariable String jobName) throws IOException {
        if(jobName.equals("market")){
            this.exportStockFromXLS.exportMarket();
        }
    }
}
