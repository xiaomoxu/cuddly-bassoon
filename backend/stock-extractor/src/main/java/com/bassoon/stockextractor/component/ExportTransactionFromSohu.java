package com.bassoon.stockextractor.component;

import com.bassoon.stockextractor.job.JsonUtils;
import com.bassoon.stockextractor.model.Stock;
import com.bassoon.stockextractor.model.StockListWrapper;
import com.bassoon.stockextractor.model.TransJsonModel;
import com.bassoon.stockextractor.model.Transaction;
import com.bassoon.stockextractor.utils.StockUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ExportTransactionFromSohu {
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
    private void getTransaction(String code, String start, String end) throws Exception {
        String url = Q_STOCK_URL.replace("aaaa", start).replace("bbbb", end).replace("zzzz", code.trim());
        HttpPost httpPost = new HttpPost(url);
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String pureJson = StockUtils.subJsonStringFromStorkURLResponse(EntityUtils.toString(entity));
        TransJsonModel transJsonModel = JsonUtils.jsonToObject(pureJson, null, TransJsonModel.class);
        if (transJsonModel == null) {
            throw new Exception("JsonMappingException");
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
                transaction.setChangeRate(Double.valueOf(elements.get(9).substring(0, (elements.get(9)).length() - 1)));
                producerService.sendTransactionMessage(JsonUtils.objectToJson(transaction));
                //System.out.println(transaction.toString());
            }
        }
    }

    public void getStockDailyTransaction(String start, String end) {
        StockListWrapper stockListWrapper = restTemplate.getForObject(restAnalyzerHost + "/stocklist", StockListWrapper.class);
        for (Stock stock : stockListWrapper.getStockList()) {
            try {
                this.getTransaction(StockUtils.fullStockCode(stock.getCode()), start, end);
            } catch (Exception e) {
                System.err.println("error... code is " + stock.getCode());
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
