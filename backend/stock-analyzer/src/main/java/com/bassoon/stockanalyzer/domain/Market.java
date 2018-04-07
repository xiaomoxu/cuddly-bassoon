package com.bassoon.stockanalyzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author xxu
 * 该股票所属的行业领域
 */
@Deprecated
public class Market extends BaseModel{
    public String name;
}
