package com.bassoon.stockextractor.component;


import com.bassoon.stockextractor.job.JsonUtils;
import com.bassoon.stockextractor.model.Market;
import com.bassoon.stockextractor.model.Region;
import com.bassoon.stockextractor.model.Stock;
import com.bassoon.stockextractor.utils.StockUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author xxu
 * 这个类负责把股票的基础数据 从excel文件中导出，通过Rabbit MQ 发送给 stock-analyzer项目，并存储到数据库中
 */
@Component
public class ExportStockFromXLS {

    private static final String EXCEL_XLSX = "xlsx";

    @Autowired
    private ProducerService producerService;

    @Autowired


    /**
     * 导出中证500的股票成分数据
     *
     * @throws IOException
     */
    public void exportZZ500Stocks() throws IOException {
        exportStockBySheet(0, "ZZ500");
    }

    public void exportSZ50Stocks() throws IOException {
        exportStockBySheet(1, "SZ50");
    }

    public void exportHS300Stocks() throws IOException {
        exportStockBySheet(2, "HS300");
    }

    private void exportStockBySheet(int sheetIndex, String group) throws IOException {
        Sheet sheet = getOneSheetFromWorkboot(sheetIndex);
        int count = 0;
        Stock stock = null;
        for (Row row : sheet) {
            //跳过第一行
            if (count == 0) {
                count++;
                continue;
            }
            stock = new Stock();
            stock.setBelongTo(group);
            Cell cell1 = row.getCell(1);//code
            stock.setCode(StockUtils.convertStockCodeToString(cell1.getNumericCellValue()));
//            if (cell1.getCellTypeEnum().equals(CellType.STRING)) {
//                System.out.print(cell1.getStringCellValue() + " string");
//            }
//            if (cell1.getCellTypeEnum().equals(CellType.NUMERIC)) {
//                System.out.print(cell1.getNumericCellValue() + " number");
//            }
            Cell cell2 = row.getCell(2);//name
            stock.setName(cell2.getStringCellValue());

            Cell cell3 = row.getCell(3);//最新价
            stock.setCurrentPrice(cell3.getNumericCellValue());

            Cell cell4 = row.getCell(4);//所属行业
            stock.setMarket(cell4.getStringCellValue());

            Cell cell5 = row.getCell(5);//地区
            stock.setRegion(cell5.getStringCellValue());

            Cell cell6 = row.getCell(6);//权重
            stock.setWeight(cell6.getNumericCellValue());

            Cell cell7 = row.getCell(7);//每股收益
            stock.setEps(cell7.getNumericCellValue());

            Cell cell8 = row.getCell(8);//每股净资产
            stock.setBvps(cell8.getNumericCellValue());

            Cell cell9 = row.getCell(9);//净资产收益率
            stock.setRoe(cell9.getNumericCellValue());

            Cell cell10 = row.getCell(10);//总股本
            stock.setTotalStock(cell10.getNumericCellValue());

            Cell cell11 = row.getCell(11);//流通股本
            stock.setLiqui(cell11.getNumericCellValue());

            Cell cell12 = row.getCell(12);//流通市值
            stock.setLtsz(cell12.getNumericCellValue());

            String jsonStr = JsonUtils.objectToJson(stock);
            producerService.sendStockMessage("stock" + jsonStr);
        }
    }

    public void exportMarket() throws IOException {
        int sheetIndex = 3;
        Sheet sheet = getOneSheetFromWorkboot(sheetIndex);
        int count = 0;
        Market market = null;
        for (Row row : sheet) {
            //跳过第一行
            if (count == 0) {
                count++;
                continue;
            }
            for (Cell cell : row) {
                if (cell.toString() == null) {
                    continue;
                }
                String cellValue = cell.getStringCellValue();
                market = new Market();
                market.name = cellValue.trim();
                market.identity = 1;
                String jsonStr = JsonUtils.objectToJson(market);
                producerService.sendStockMessage("market" + jsonStr);
            }
        }
    }

    public void exportRegion() throws IOException {
        int sheetIndex = 4;
        Sheet sheet = getOneSheetFromWorkboot(sheetIndex);
        int count = 0;
        Region region = null;
        for (Row row : sheet) {
            //跳过第一行
            if (count == 0) {
                count++;
                continue;
            }
            for (Cell cell : row) {
                if (cell.toString() == null) {
                    continue;
                }
                String cellValue = cell.getStringCellValue();
                region = new Region();
                region.name = cellValue.trim();
                region.identity = 1;
                String jsonStr = JsonUtils.objectToJson(region);
                producerService.sendStockMessage("region" + jsonStr);
                // this.extractService.commitStockData(market);
            }
        }
    }

    private static Sheet getOneSheetFromWorkboot(int sheetIndex) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("cn_stock.xlsx");
//        FileInputStream is = new FileInputStream(ResourceUtils.getFile("classpath:cn_stock.xlsx"));
        Workbook wb = new XSSFWorkbook(classPathResource.getInputStream());
        int sheetCount = wb.getNumberOfSheets();
        Sheet sheet = wb.getSheetAt(sheetIndex);
        return sheet;
    }

    public static void main(String argz[]) {
        try {
            new ExportStockFromXLS().exportZZ500Stocks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
