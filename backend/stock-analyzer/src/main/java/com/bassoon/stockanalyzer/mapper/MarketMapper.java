package com.bassoon.stockanalyzer.mapper;

import com.bassoon.stockanalyzer.domain.Market;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MarketMapper {
    public void insertMarket(Market market);
}
