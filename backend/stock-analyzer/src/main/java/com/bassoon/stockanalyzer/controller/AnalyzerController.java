package com.bassoon.stockanalyzer.controller;

import com.bassoon.stockanalyzer.domain.Stock;
import com.bassoon.stockanalyzer.model.PageResult;
import com.bassoon.stockanalyzer.policy.TwoEightNode;
import com.bassoon.stockanalyzer.policy.TwoEightNode2;
import com.bassoon.stockanalyzer.policy.TwoEightRotation;
import com.bassoon.stockanalyzer.service.StockService;
import com.bassoon.stockanalyzer.wrapper.StockListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class AnalyzerController {
    @Autowired
    private StockService stockService;

    @Autowired
    private TwoEightRotation twoEightRotation;


    @RequestMapping(value = "/stocklist", method = RequestMethod.GET)
    public StockListWrapper getAllStock() {
        return stockService.getAllStock();
    }

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
    @GetMapping(value = "/stocklist/{belongTo}", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<Stock> getStocksByBelongTo(HttpServletResponse rsp, @PathVariable String belongTo, @RequestParam("_page") int _page, @RequestParam("_limit") int _limit) {
        PageResult<Stock> result = stockService.getStocksByBelongTo(belongTo, _page, _limit);
        rsp.addHeader("X-Total-Count", String.valueOf(result.getTotal()));
        return (List<Stock>) result.getResult();
    }

    @GetMapping(value = "/stocklist-spark", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public StockListWrapper getAllStock(HttpServletResponse rsp) {
        StockListWrapper result = stockService.getAllStockOnSpark();
        rsp.addHeader("X-Total-Count", String.valueOf(result.getStockList().size()));
        return result;
    }

    @GetMapping(value = "/tow-eight-rotation", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<TwoEightNode> getLineForTwoEightRotationData(HttpServletResponse rsp) {
        List<TwoEightNode> nodeList = twoEightRotation.generateTwoEightRatationData();
        rsp.addHeader("X-Total-Count", String.valueOf(nodeList.size()));
        return nodeList;
    }

    @GetMapping(value = "/tow-eight-rotation/{table}", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*", exposedHeaders = "X-Total-Count")
    public List<TwoEightNode2> getLineForTwoEightRotationData(HttpServletResponse rsp, @PathVariable String table) {
        List<TwoEightNode2> nodeList = twoEightRotation.getWeekData(table);
        rsp.addHeader("X-Total-Count", String.valueOf(nodeList.size()));
        return nodeList;
    }
}
