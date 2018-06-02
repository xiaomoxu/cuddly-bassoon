import threading
import time

import tushare as ts
from sqlalchemy import create_engine

# history_scale = ts.get_k_data('399905', index=True, start='2000-01-01', end='2018-03-07')  # 上证
# print(history_scale)

codeArray = []
codeArray.append(['stock_hs_k_data', '399300'])
codeArray.append(['stock_zz_k_data', '399905'])
codeArray.append(['stock_cyb_k_data', '399006'])
codeArray.append(['stock_zxb_k_data', '399005'])
codeArray.append(['stock_sz_k_data', '000001'])

engine = create_engine('mysql://root:Liaobi()7595k@10.20.116.107/CN_BASSOON?charset=utf8')


class myThread(threading.Thread):
    my_start_time = '';
    my_end_time = '';

    def __init__(self, startTime, endTime):
        threading.Thread.__init__(self)
        self.my_start_time = startTime;
        self.my_end_time = endTime;

    def run(self):
        print("开始线程：" + self.name)
        saveToDatabase(self.my_start_time, self.my_end_time)
        print("退出线程：" + self.name)


def saveToDatabase(startTime, endTime):
    for code in codeArray:
        print("%s: %s : %s" % (code[0], time.ctime(time.time()), code[1]))
        # history_hfq = ts.get_k_data(code, start=startTime, end=endTime, ktype='D', autype=threadID)
        history_scale = ts.get_k_data(code[1], index=True, start=startTime, end=endTime)  # 上证
        history_scale.to_sql(code[0], engine, if_exists='append')


thread1 = myThread('2018-03-28', '2018-04-15')
# 开启新线程
thread1.start()
print("所有线程都已启动!")
