package com.bassoon.stockextractor;

import com.bassoon.stockextractor.component.ExportTransactionFromSohu;
import com.bassoon.stockextractor.component.ProducerService;
import com.bassoon.stockextractor.model.Stock;
import com.netflix.discovery.converters.Auto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockExtractorApplicationTests {

    @Autowired
    private ExportTransactionFromSohu exportTransactionFromSohu;

    @Test
    public void getStockTransaction() {
        exportTransactionFromSohu.getStockDailyTransaction("20180115", "20180115");
    }

    public void testTransactionJson(){

    }
}
