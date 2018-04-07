package com.bassoon.stockanalyzer.mapper;

import com.bassoon.stockanalyzer.domain.Market;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Deprecated
@Mapper
public interface MarketMapper {

    @Insert("INSERT INTO market(name) VALUES(#{name})")
    public void save(Market market);
}
