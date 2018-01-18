package com.bassoon.stockanalyzer.controller;

import com.bassoon.stockanalyzer.service.StockService;
import com.bassoon.stockanalyzer.wrapper.StockListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyzerController {
    @Autowired
    private StockService stockService;


    @RequestMapping(value = "/stocklist", method = RequestMethod.GET)
    public StockListWrapper getAllStock() {
        return stockService.getAllStock();
    }
}
