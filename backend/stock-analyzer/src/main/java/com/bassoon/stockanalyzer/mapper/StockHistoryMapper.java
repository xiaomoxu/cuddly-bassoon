package com.bassoon.stockanalyzer.mapper;


import com.bassoon.stockanalyzer.domain.StockHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockHistoryMapper {

    @Insert("INSERT INTO stock_history (stock_code,stock_date,price_open,price_high,price_close,price_low," +
            "volume,price_change,p_change,ma5,ma10,ma20,v_ma5,v_ma10,v_ma20,turnover" +
            ")" + " VALUES(#{stockCode},#{date},#{open},#{high},#{close}," +
            "#{low},#{volume},#{price_change},#{p_change},#{ma5},#{ma10},#{ma20},#{v_ma5},#{v_ma10},#{v_ma20},#{turnover})")
    public void save(StockHistory stockHistory);
}
