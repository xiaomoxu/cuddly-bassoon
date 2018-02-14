from sqlalchemy import create_engine
import tushare as ts

stock_basics = ts.get_stock_basics() #所有股票列表    stock_basic
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


engine = create_engine('mysql://root:Liaobi()7595k@10.20.116.107/CN_BASSOON?charset=utf8')
sme.to_sql('sme', engine, if_exists='append')
gem.to_sql('gem', engine, if_exists='append')
st.to_sql('st', engine, if_exists='append')
hs300s.to_sql('hs300s', engine, if_exists='append')
sz50s.to_sql('sz50s', engine, if_exists='append')
zz500s.to_sql('zz500s', engine, if_exists='append')
terminated.to_sql('terminated', engine, if_exists='append')
suspended.to_sql('suspended', engine, if_exists='append')
stock_basics.to_sql('stock_basics', engine, if_exists='append')
industry.to_sql('industry', engine, if_exists='append')
concept.to_sql('concept', engine, if_exists='append')
area.to_sql('area', engine, if_exists='append')
