package com.bassoon.stockextractor.component;

import com.bassoon.stockextractor.job.JsonUtils;
import com.bassoon.stockextractor.model.Stock;
import com.bassoon.stockextractor.model.StockListWrapper;
import com.bassoon.stockextractor.model.TransJsonModel;
import com.bassoon.stockextractor.model.Transaction;
import com.bassoon.stockextractor.utils.StockUtils;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Deprecated
public class ExportTransactionFromSohu {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static String Q_STOCK_URL = "http://q.stock.sohu.com/hisHq?code=cn_zzzz&start=aaaa&end=bbbb&" +
            "stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp";


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpClient httpClient;

    @Value("${stock.analyzer.host}")
    private String restAnalyzerHost;

    @Autowired
    private ProducerService producerService;

    /**
     * @param code  这个值应该是从analyzer去数据库读取
     * @param start
     * @param end   日期格式YYYYMMdd 如20180105
     */
    public void getTransaction(String code, String start, String end, boolean sendToQueue) throws Exception {
        String url = Q_STOCK_URL.replace("aaaa", start).replace("bbbb", end).replace("zzzz", code.trim());
        logger.debug("pull stock transaction URL: " + url);
        HttpPost httpPost = new HttpPost(url);
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String pureJson = StockUtils.subJsonStringFromStorkURLResponse(EntityUtils.toString(entity));
        TransJsonModel transJsonModel = JsonUtils.jsonToObject(pureJson, null, TransJsonModel.class);
        if (transJsonModel == null) {
            throw new Exception("com.fasterxml.jackson.databind.JsonMappingException: No content to map due to end-of-input");
        }
        if (transJsonModel != null && transJsonModel.getStatus() == 0) {
            List<List<String>> list = transJsonModel.getHq();
            for (List<String> elements : list) {
                Transaction transaction = new Transaction();
                transaction.setStockCode(code);
                transaction.setTansDate(StockUtils.stringConvertToDate(elements.get(0)));
                transaction.setOpenPrice(Double.valueOf(elements.get(1)));
                transaction.setClosePrice(Double.valueOf(elements.get(2)));
                transaction.setChangeAmount(Double.valueOf(elements.get(3)));
                transaction.setChange(Double.valueOf(elements.get(4).substring(0, (elements.get(4)).length() - 1)));
                transaction.setLowPrice(Double.valueOf(elements.get(5)));
                transaction.setHighPrice(Double.valueOf(elements.get(6)));
                transaction.setDailyTransVolume(Integer.valueOf(elements.get(7)));
                transaction.setDailyTransTurnover(Double.valueOf(elements.get(8)));
                try {
                    transaction.setChangeRate(Double.valueOf(elements.get(9).substring(0, (elements.get(9)).length() - 1)));
                } catch (Exception e) {
                    transaction.setChangeRate(0);
                }

                if (sendToQueue)
                    producerService.sendTransactionMessage(JsonUtils.objectToJson(transaction));
                //System.out.println(transaction.toString());
            }
        }
    }

    public void getStockDailyTransaction(String start, String end, boolean sendToQueue) {
        StockListWrapper stockListWrapper = restTemplate.getForObject(restAnalyzerHost + "/stocklist-unique", StockListWrapper.class);
        for (Stock stock : stockListWrapper.getStockList()) {
            try {
                this.getTransaction(StockUtils.fullStockCode(stock.getCode()), start, end, sendToQueue);
            } catch (Exception e) {
//                System.err.println("error... code is " + stock.getCode());
                logger.error(e.getMessage() + "| stock code is " + stock.getCode());
//                e.printStackTrace();
                continue;
            } finally {
                try {
                    Thread.sleep(500);//別那麼囂張
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
