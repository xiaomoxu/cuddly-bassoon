# -*- coding:utf-8 -*-
"""
股票数据接口
测试
"""
from sqlalchemy import create_engine
import tushare as ts

stock_basics = ts.get_stock_basics()  # 所有股票列表    stock_basic
industry = ts.get_industry_classified()  # 行业分类                industry
concept = ts.get_concept_classified()  # 概念分类                concept
area = ts.get_area_classified()  # 地域分类                 area
sme = ts.get_sme_classified()  # 中小板                sme
gem = ts.get_gem_classified()  # 创业板                    gem
st = ts.get_st_classified()  # ST风险警示板            st
hs300s = ts.get_hs300s()  # 沪深300               hs300s
sz50s = ts.get_sz50s()  # 上证50                        sz50s
zz500s = ts.get_zz500s()  # 中证500                zz500s
terminated = ts.get_terminated()  # 终止上市    terminated
suspended = ts.get_suspended()  # 暂停上市    suspended

print(stock_basics);
