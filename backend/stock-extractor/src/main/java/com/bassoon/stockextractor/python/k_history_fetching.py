import threading
import time

import tushare as ts
from sqlalchemy import create_engine

exitFlag = 0
stock_basics = ts.get_stock_basics()
codeArray = stock_basics.index
engine = create_engine('mysql://root:Liaobi()7595k@10.20.116.107/CN_BASSOON?charset=utf8')


class myThread(threading.Thread):
    my_start_time = '';
    my_end_time = '';

    def __init__(self, threadID, name, startTime, endTime):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.my_start_time = startTime;
        self.my_end_time = endTime;

    def run(self):
        print("开始线程：" + self.name)
        saveToDatabase(self.name, self.threadID, self.my_start_time, self.my_end_time)
        print("退出线程：" + self.name)


def saveToDatabase(threadName, threadID, startTime, endTime):
    for code in codeArray:
        print("%s: %s : %s" % (threadName, time.ctime(time.time()), code))
        history_hfq = ts.get_k_data(code, start=startTime, end=endTime, ktype='D', autype=threadID)
        history_hfq.to_sql(threadName, engine, if_exists='append')


thread1 = myThread("hfq", "stock_k_data_hfq", '2017-04-26', '2018-04-16')
thread2 = myThread("qfq", "stock_k_data_qfq", '2017-04-26', '2018-04-16')
thread3 = myThread("none", "stock_k_data_none", '2017-04-26', '2018-04-16')
# 开启新线程
thread1.start()
thread2.start()
thread3.start()
print("所有线程都已启动!")
