package com.bassoon.stockanalyzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class BaseModel {
    @JsonIgnore
    public int identity;
}
