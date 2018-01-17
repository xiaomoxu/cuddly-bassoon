package com.bassoon.stockextractor.utils;

import java.util.StringTokenizer;

public class StockUtils {

    public static String getMktByCode(String code) {
        if (code.length() < 3)
            return "sh";
        String one = code.substring(0, 1);
        String three = code.substring(0, 3);
        if (one.equals("5") || one.equals("6") || one.equals("9")) {
            return "sh";
        } else {
            if (three.equals("009") || three.equals("126") || three.equals("110") || three.equals("201") || three.equals("202") || three.equals("203") || three.equals("204")) {
                return "sh";
            } else {
                return "sz";
            }
        }
    }

    public static String convertStockCodeToString(double codeFromXls) {
        String s = String.valueOf(codeFromXls);
        String newD = s.substring(0, s.indexOf("."));
        return newD;
    }

    /**
     * from historySearchHandler([
     * to    ]}
     * @return
     */
    public static String subJsonStringFromStorkURLResponse(String jsonString){
       return jsonString.substring(22 , jsonString.length() - 3);
    }

}
