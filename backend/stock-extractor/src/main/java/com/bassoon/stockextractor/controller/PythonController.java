package com.bassoon.stockextractor.controller;

import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PythonController {

    @Autowired
    private PythonInterpreter pythonInterpreter;

    /**
     * 这个方法由初始项目构建股票历史交易数据时构建，之后均由java任务自动去完成
     *
     * @throws IOException
     */
    @RequestMapping(value = "/job/stock-k-pull", method = RequestMethod.GET)
    public void pullStockHistoryData() throws IOException {
        pythonInterpreter.exec("print('hello')");
    }
}
