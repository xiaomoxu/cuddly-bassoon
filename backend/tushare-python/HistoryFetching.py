import tushare as ts


stock_basics = ts.get_stock_basics()
codeArray = stock_basics.index
for code in codeArray:
    history = ts.get_hist_data(code, start='2000-01-01', end='2018-02-12')
    print(history.shape[0])

# row = history.iloc[0]

# print(history.iat[1,1])