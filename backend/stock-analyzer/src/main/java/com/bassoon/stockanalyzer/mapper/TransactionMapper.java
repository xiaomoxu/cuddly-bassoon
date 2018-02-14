package com.bassoon.stockanalyzer.mapper;

import com.bassoon.stockanalyzer.domain.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Deprecated
@Mapper
public interface TransactionMapper {

    /**
     * 保存交易数据 从extractor项目中过来的数据
     * @param transaction
     */
    @Insert("INSERT INTO transaction (stock_code,trans_date,open_price,close_price,change_amount,change_range," +
            "low_price,high_price,daily_trans_volume,daily_trans_turnover,change_rate" +
            ")" + " VALUES(#{stockCode},#{tansDate},#{openPrice},#{closePrice},#{changeAmount}," +
            "#{change},#{LowPrice},#{highPrice},#{dailyTransVolume},#{dailyTransTurnover},#{changeRate})")
    public void save(Transaction transaction);
}
