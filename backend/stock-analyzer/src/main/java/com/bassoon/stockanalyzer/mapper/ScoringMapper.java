package com.bassoon.stockanalyzer.mapper;

import com.bassoon.stockanalyzer.domain.BasicStock;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ScoringMapper {
    @Results({
            @Result(column = "roe", property = "roe"),
            @Result(column = "gross_profit_rate", property = "grossProfitRate"),
    })
    @Select("select roe , gross_profit_rate from stock_profit_data where code=#{code} and year = #{year}")
    public List<BasicStock> getRoeByYear(@Param("code") String code, @Param("year") int year);


    @Results({
            @Result(column = "epsg", property = "Double"),
    })
    @Select("select epsg from stock_growth_data where code=#{code} and year = #{year}")
    public List<Double> getEpsgByYear(@Param("code") String code, @Param("year") int year);

    @Results({
            @Result(column = "cashflowratio", property = "Double"),
    })
    @Select("select cashflowratio from stock_cashflow_data where code=#{code} and year = #{year}")
    public List<Double> getCashFlowRatioByYear(@Param("code") String code, @Param("year") int year);

    @Results({
            @Result(column = "currentratio", property = "Double"),
    })
    @Select("select currentratio from stock_debtpay_data where code=#{code} and year = #{year}")
    public List<Double> getCurrentDebtRatioByYear(@Param("code") String code, @Param("year") int year);

    @Results({
            @Result(column = "currentasset_turnover", property = "Double"),
    })
    @Select("select currentasset_turnover from stock_operation_data where code=#{code} and year = #{year}")
    public List<Double> getCurrentassetTurnoverByYear(@Param("code") String code, @Param("year") int year);
}
