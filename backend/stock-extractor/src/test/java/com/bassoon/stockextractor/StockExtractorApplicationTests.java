package com.bassoon.stockextractor;

import com.bassoon.stockextractor.component.ExportTransactionFromSohu;
import com.bassoon.stockextractor.component.ProducerService;
import com.bassoon.stockextractor.model.Stock;
import com.bassoon.stockextractor.utils.StockUtils;
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
        try {
            exportTransactionFromSohu.getTransaction(StockUtils.fullStockCode("2345"), "20180101", "20180115", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testTransactionJson() {

    }
}
