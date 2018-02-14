from sqlalchemy import create_engine
import tushare as ts

stock_basics = ts.get_stock_basics() #所有股票列表    stock_basic
print(stock_basics)