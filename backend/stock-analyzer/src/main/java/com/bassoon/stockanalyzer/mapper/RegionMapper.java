package com.bassoon.stockanalyzer.mapper;

import com.bassoon.stockanalyzer.domain.Market;
import com.bassoon.stockanalyzer.domain.Region;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegionMapper {
    @Insert("INSERT INTO region(name) VALUES(#{name})")
    public void save(Region region);
}
