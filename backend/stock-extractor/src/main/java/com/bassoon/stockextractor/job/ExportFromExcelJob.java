package com.bassoon.stockextractor.job;


import com.bassoon.stockextractor.model.Market;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;

@Component
public class ExportFromExcelJob {

    private static final String EXCEL_XLSX = "xlsx";
    @Autowired
    private ExtractService extractService;

    /**
     * 导出中证500的股票成分数据
     *
     * @throws IOException
     */
    public void exportZZ500Stocks() throws IOException {
        exportStockBySheet(0);
    }

    public void exportSZ50Stocks() throws IOException {
        exportStockBySheet(1);
    }

    public void exportHS300Stocks() throws IOException {
        exportStockBySheet(2);
    }

    private void exportStockBySheet(int sheetIndex) throws IOException {
        Sheet sheet = getOneSheetFromWorkboot(sheetIndex);
        int count = 0;
        for (Row row : sheet) {
            //跳过第一行
            if (count == 0) {
                count++;
                continue;
            }
            Cell cell1 = row.getCell(1);//code
            Cell cell2 = row.getCell(2);//name
            Cell cell3 = row.getCell(3);//最新价
            Cell cell4 = row.getCell(4);//所属行业
            Cell cell5 = row.getCell(5);//地区
            Cell cell6 = row.getCell(6);//权重
            Cell cell7 = row.getCell(7);//每股收益
            Cell cell8 = row.getCell(8);//每股净资产
            Cell cell9 = row.getCell(9);//净资产收益率
            Cell cell10 = row.getCell(10);//总股本
            Cell cell11 = row.getCell(11);//流通股本
            Cell cell12 = row.getCell(12);//流通市值
//            if (cell.getCellTypeEnum().equals(CellType.STRING)) {
//                System.out.print(cell.getStringCellValue() + " ");
//            }
//            if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
//                System.out.print(cell.getNumericCellValue() + " ");
//            }

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
                this.extractService.commitStockData(market);
            }
        }
    }

    public void exportRegion() throws IOException {
        int sheetIndex = 4;
        Sheet sheet = getOneSheetFromWorkboot(sheetIndex);
        int count = 0;
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
               // this.extractService.commitStockData(market);
            }
        }
    }

    private static Sheet getOneSheetFromWorkboot(int sheetIndex) throws IOException {
        FileInputStream is = new FileInputStream(ResourceUtils.getFile("classpath:cn_stock.xlsx"));
        Workbook wb = new XSSFWorkbook(is);
        int sheetCount = wb.getNumberOfSheets();
        Sheet sheet = wb.getSheetAt(sheetIndex);
        return sheet;
    }

    public static void main(String argz[]) {
        try {
            new ExportFromExcelJob().exportZZ500Stocks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
