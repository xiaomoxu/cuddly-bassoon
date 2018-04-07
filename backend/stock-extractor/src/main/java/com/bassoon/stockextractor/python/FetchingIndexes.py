from sqlalchemy import create_engine
import tushare as ts

df = ts.get_index()
engine = create_engine('mysql://root:Liaobi()7595k@10.20.116.107/CN_BASSOON?charset=utf8')
df.to_sql('stock_index', engine, if_exists='append')