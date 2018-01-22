package com.bassoon.stockanalyzer.mapper;

import com.bassoon.stockanalyzer.domain.Stock;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StockMapper {

    /**
     * 保存股票基础数据
     *
     * @param stock
     */
    @Insert("INSERT INTO stock(code,name,market,region,belong_to,weight,current_price,eps,bvps,roe,total_stock,liqui," +
            "ltsz,information_url) VALUES(#{code},#{name},#{market},#{region},#{belongTo},#{weight},#{currentPrice},#{eps}," +
            "#{bvps},#{roe},#{totalStock},#{liqui},#{ltsz},#{information_url})")
    public void save(Stock stock);

    /**
     * 获取850只股票基础数据
     *
     * @return
     */
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "total_stock", property = "totalStock"),
            @Result(column = "current_price", property = "currentPrice"),
            @Result(column = "belong_to", property = "belongTo")
    })
    @Select("SELECT * FROM stock")
    public List<Stock> getAll();

    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "total_stock", property = "totalStock"),
            @Result(column = "current_price", property = "currentPrice"),
            @Result(column = "belong_to", property = "belongTo")
    })
    @Select("select DISTINCT code,id,total_stock, from stock")
    public List<Stock> getAllAndRemoveDuplicate();

    /**
     * 获得既是沪深300又是上证50的股票
     *
     * @return
     */
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "total_stock", property = "totalStock"),
            @Result(column = "current_price", property = "currentPrice"),
            @Result(column = "belong_to", property = "belongTo")
    })
    @Select("select * from stock a where code in  (select code from stock group by code  having count(*) > 1)")
    public List<Stock> getStocksInSZ50AndHS300();
}
