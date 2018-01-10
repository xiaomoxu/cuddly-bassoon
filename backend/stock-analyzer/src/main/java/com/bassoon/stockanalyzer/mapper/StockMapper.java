package com.bassoon.stockanalyzer.mapper;

import com.bassoon.stockanalyzer.domain.Stock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StockMapper {

    public List<Stock> selectAll();

}
