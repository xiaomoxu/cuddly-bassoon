package com.bassoon.stockanalyzer.mapper;

import com.bassoon.stockanalyzer.domain.Stock;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StockMapper {

    @Insert("INSERT INTO stock(code,name,market,region,belong_to,weight,current_price,eps,bvps,roe,total_stock,liqui," +
            "ltsz,information_url) VALUES(#{code},#{name},#{market},#{region},#{belongTo},#{weight},#{currentPrice},#{eps}," +
            "#{bvps},#{roe},#{totalStock},#{liqui},#{ltsz},#{information_url})")
    public void save(Stock stock);

    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "total_stock", property = "totalStock"),
            @Result(column = "current_price", property = "currentPrice"),
            @Result(column = "belong_to", property = "belongTo")
    })
    @Select("SELECT * FROM stock")
    public List<Stock> getAll();
}
