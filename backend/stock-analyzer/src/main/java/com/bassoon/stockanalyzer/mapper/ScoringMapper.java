package com.bassoon.stockanalyzer.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ScoringMapper {
    @Results({
            @Result(column = "roe", property = "Double"),
    })
    @Select("select roe , gross_profit_rate from stock_profit_data where code=#{code} and year = #{year}")
    public List<Double> getRoeInProfitByYear(@Param("code") String code,@Param("year") int year);
}
