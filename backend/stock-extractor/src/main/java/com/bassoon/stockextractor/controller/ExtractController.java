package com.bassoon.stockextractor.controller;

import com.bassoon.stockextractor.job.ExportFromExcelJob;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ExtractController {
    @Autowired
    private ExportFromExcelJob exportFromExcelJob;

    @RequestMapping(value = "/extract/{jobName}" , method = RequestMethod.GET)
    public void sendStockData(@PathVariable String jobName) throws IOException {
        if(jobName.equals("market")){
            this.exportFromExcelJob.exportMarket();
        }
    }
}
