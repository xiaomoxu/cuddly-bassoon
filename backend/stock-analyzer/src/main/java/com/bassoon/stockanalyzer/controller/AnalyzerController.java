package com.bassoon.stockanalyzer.controller;

import com.bassoon.stockanalyzer.domain.Stock;
import com.bassoon.stockanalyzer.model.PageResult;
import com.bassoon.stockanalyzer.service.JsonUtils;
import com.bassoon.stockanalyzer.service.StockService;
import com.bassoon.stockanalyzer.spark.model.*;
import com.bassoon.stockanalyzer.spark.service.ScorePolicyService;
import com.bassoon.stockanalyzer.spark.service.StockSparkService;
import com.bassoon.stockanalyzer.spark.service.StockStatisticService;
import com.bassoon.stockanalyzer.spark.service.AlternatePolicyService;
import com.bassoon.stockanalyzer.spark.service.AccumulateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class AnalyzerController {
    @Autowired
    private StockService stockService;

    @Autowired
    private AlternatePolicyService alternatePolicyService;

    @Autowired
    private StockStatisticService stockStatisticService;

    @Autowired
    private ScorePolicyService scorePolicyService;

    @Autowired
    private StockSparkService stockSparkService;

    @Autowired
    private AccumulateService accumulateService;

    @Deprecated
    @RequestMapping(value = "/stocklist", method = RequestMethod.GET)
    public StockListWrapper getAllStock() {
        return stockService.getAllStock();
    }

    @Deprecated
    @GetMapping(value = "/stocklist-unique", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<Stock> getAllStockRemoveDuplication(HttpServletResponse rsp, @RequestParam("_page") int _page, @RequestParam("_limit") int _limit) {
        PageResult<Stock> result = stockService.getStocksRemoveDuplicateByCode(_page, _limit);
        rsp.addHeader("X-Total-Count", String.valueOf(result.getTotal()));
        return (List<Stock>) result.getResult();
    }

    /**
     * URL sample: http://localhost:9002/stocklist/SZ50?_page=1&_limit=10
     *
     * @param rsp
     * @param belongTo
     * @param _page
     * @param _limit
     * @return
     */
    @Deprecated
    @GetMapping(value = "/stocklist/{belongTo}", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<Stock> getStocksByBelongTo(HttpServletResponse rsp, @PathVariable String belongTo, @RequestParam("_page") int _page, @RequestParam("_limit") int _limit) {
        PageResult<Stock> result = stockService.getStocksByBelongTo(belongTo, _page, _limit);
        rsp.addHeader("X-Total-Count", String.valueOf(result.getTotal()));
        return (List<Stock>) result.getResult();
    }

    /****************************************以下是基于spark特性的API，上面的API已经作废，仅做参考*********************************************/

    @GetMapping(value = "/stocklist-spark", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public StockListWrapper getAllStock(HttpServletResponse rsp) {
        StockListWrapper result = stockSparkService.getStocks(false);
        rsp.addHeader("X-Total-Count", String.valueOf(result.getStockList().size()));
        return result;
    }

    @GetMapping(value = "/tow-eight-rotation", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<AlternateValue> getLineForTwoEightRotationData(HttpServletResponse rsp) {
        List<AlternateValue> nodeList = alternatePolicyService.generateAlternatePolicyData(false);
        rsp.addHeader("X-Total-Count", String.valueOf(nodeList.size()));
        return nodeList;
    }

    @GetMapping(value = "/stock-static/{key}", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<StockStatisticsValue> getDataByStockWithCity(HttpServletResponse rsp, @PathVariable String key) {
        List<StockStatisticsValue> nodeList = stockStatisticService.staticStockByKey(key, false);
        return nodeList;
    }

    @GetMapping(value = "/tow-eight-rotation/{table}", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<AlternateValue2> getLineForTwoEightRotationData(HttpServletResponse rsp, @PathVariable String table) {
        List<AlternateValue2> nodeList = alternatePolicyService.getWeekData(table);
        rsp.addHeader("X-Total-Count", String.valueOf(nodeList.size()));
        return nodeList;
    }

    @GetMapping(value = "/stock-selector/{year}", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<StockScoreValue> getSelectorStockList(HttpServletResponse rsp, @PathVariable int year) {
        List<StockScoreValue> nodeList = scorePolicyService.scoring(year);
        return nodeList;
    }

    @GetMapping(value = "/stock-index/{code}", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<StockIndexValue> getStockIndexByCodeToday(HttpServletResponse rsp, @PathVariable String code) {
        String[] codeArray = code.split(",");
        List<StockIndexValue> indexList = stockSparkService.getStockIndexValueToday(false, codeArray);
        rsp.addHeader("X-Total-Count", String.valueOf(indexList.size()));
        return indexList;
    }

    @GetMapping(value = "/stock-news", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<StockNewsValue> getStockNews(HttpServletResponse rsp) {
        List<StockNewsValue> indexList = stockSparkService.getStockNews(false);
        rsp.addHeader("X-Total-Count", String.valueOf(indexList.size()));
        return indexList;
    }

    @GetMapping(value = "/accum/{year}", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<AccumulateValue> getAccumulateData(HttpServletResponse rsp,@PathVariable String year) {
        List<AccumulateValue> result = accumulateService.getAccumulatedData(year,false);
        rsp.addHeader("X-Total-Count", String.valueOf(result.size()));
        return result;
    }

    @GetMapping(value = "/scope/{year}", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<StockScoreValue> getQualityData(HttpServletResponse rsp,@PathVariable String year) {
        List<StockScoreValue> result = accumulateService.getQualityStocks(year,false);
        rsp.addHeader("X-Total-Count", String.valueOf(result.size()));
        return result;
    }
}
