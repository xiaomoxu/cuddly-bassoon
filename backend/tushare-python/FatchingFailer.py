from sqlalchemy import create_engine
import tushare as ts



sme = ts.get_sme_classified()  # 中小板                sme
gem = ts.get_gem_classified()  # 创业板                gem
st = ts.get_st_classified()  # ST风险警示板            st



engine = create_engine('mysql://root:Liaobi()7595k@10.20.116.107/CN_BASSOON?charset=utf8')
sme.to_sql('sme', engine, if_exists='append')
gem.to_sql('gem', engine, if_exists='append')
st.to_sql('st', engine, if_exists='append')