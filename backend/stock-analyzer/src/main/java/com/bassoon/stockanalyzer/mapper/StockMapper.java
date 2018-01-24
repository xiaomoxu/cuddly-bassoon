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
            @Result(column = "code", property = "String"),
    })
    @Select("select code from stock group by code")
    public List<String> getStockCodeAndRemoveDuplicate();

    /**
     * 获得既是沪深300又是上证50的股票 应该是100只这样的股票
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


    /**
     * 根据股票code获得股票对象 注意因为stock表中存在即是50 又是300的股票 也就是说明同一个code有可能有两条记录
     * 所以这个方法返回的结果不一定是唯一的 所以返回集合
     *
     * @param code
     * @return
     */
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "total_stock", property = "totalStock"),
            @Result(column = "current_price", property = "currentPrice"),
            @Result(column = "belong_to", property = "belongTo")
    })
    @Select("SELECT * FROM stock where code = #{code}")
    public List<Stock> findStockByCode(@Param("code") String code);

    /**
     * 根据股票板块获取股票数据
     * HS300/SZ50/ZZ500
     * @param belongTo
     * @return
     */
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "total_stock", property = "totalStock"),
            @Result(column = "current_price", property = "currentPrice"),
            @Result(column = "belong_to", property = "belongTo")
    })
    @Select("SELECT * FROM stock where belong_to = #{belongTo}")
    public List<Stock> findStocksByBeloneTo(@Param("belongTo") String belongTo);
}
