import tushare as ts
from sqlalchemy import create_engine


report = ts.get_report_data(2015,3)
print(report)

# engine = create_engine('mysql://root:Liaobi()7595k@10.20.116.107/CN_BASSOON?charset=utf8')
# report.to_sql('hs300s', engine, if_exists='append')