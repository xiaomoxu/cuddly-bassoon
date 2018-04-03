import sys
import tushare as ts
import pandas as p
import datetime,time,string
from sqlalchemy import create_engine, Table, Column, Integer, String, MetaData, func, text
from sqlalchemy.orm import sessionmaker

#api_name:table_name
mapTable = {
    'get_profit_data':'stock_profit_data',
    'get_operation_data':'stock_operation_data',
    'get_growth_data':'stock_growth_data',
    'get_debtpaying_data':'stock_debtpay_data',
    'get_cashflow_data':'stock_cashflow_data',
    # table for test
    #'get_profit_data':'stock_profit_test_feng'
}

def getQuarterList(start_year):
    current = time.strftime('%Y%m',time.localtime(time.time()))
    #current = '201712'      #cannot retrieve the data of 2018, so from 201712
    end_year = current[0:4]
    end_month= current[4:6]
    quarters = []

    if start_year > end_year:
        print('start year should older than current')
    else:
        for year in range(int(start_year),int(end_year)+1):
            end_quarter = 4
            if year == int(end_year):
                end_quarter = int(int(end_month)/4 + 1)
            for quarter in range(1,end_quarter + 1):
                quarters.append(str(year)+ ('%02d'%quarter))
    return quarters

def retrieveData(api_name,table_name,quarters):
    mytable = getTableObject(table_name)
    try:
        year_last = session.query(func.max(mytable.c.year)).scalar()
        quarter_last = session.query(func.max(mytable.c.quarter)).filter(mytable.c.year == year_last).scalar()
        if year_last == None:
            year_last = 0
        if quarter_last == None:
            quarter_last = 0
    except:
        year_last = 0
        quarter_last = 0
        print('Failed to retrieve Max of year and quarter, set them to zero')

    print('Latest data in table: ' + table_name + ' is ', year_last, quarter_last)
    if year_last > 10:
        #back to 1 year make sure we can get newest data
        year_last = year_last - 1

    for quarter in quarter_list:
        year_cur    = int(quarter[0:4])
        quarter_cur = int(quarter[4:6])
        if year_cur > year_last or (year_cur == year_last and quarter_cur >= int(quarter_last)):
            print('Starting retrieve data of %s of %d %d '%(api_name, year_cur, quarter_cur))
            #continue
            try:
                exec_api = 'ts.%s(%d,%d)'%(api_name,year_cur,quarter_cur)
                print('exec Tushare API :%s'%exec_api)
                #execute the tushare API call
                report = eval(exec_api)
                if year_last != 0:
                    cnt = session.query(func.count(mytable.c.year)). \
                        filter(mytable.c.year == year_cur). \
                        filter(mytable.c.quarter == quarter_cur).scalar()
                    if cnt != len(report.index):
                        sql = 'delete from %s where year=%d and quarter=%d'%(table_name,year_cur,quarter_cur)
                        session.execute(sql)
                        session.commit()
                        print('\ndelete old data of %d %d from %s'%(year_cur,quarter_cur,table_name))
                    else:
                        #no changes, do next
                        print('\nno data is updated')
                        continue
                report['year'] = p.Series(year_cur,index=report.index)
                report['quarter'] = p.Series(quarter_cur,index=report.index)
                print(report)
                report.to_sql(table_name, engine, if_exists='append')
            except IOError:
                print('\nIOError: might no data on server')

def getTableObject(name):
    metadata = MetaData()
    table_object = Table(name,metadata,
                         Column('year',String),
                         Column('quarter',String))
    return table_object

#which year is the data retrieved from ?
quarter_list = getQuarterList('2007')
engine = create_engine('mysql://root:Liaobi()7595k@10.20.116.107/CN_BASSOON?charset=utf8')
Session = sessionmaker(bind=engine)
session = Session()

### It will clear tables if add the 'clear' to the command line
### eg:   python3.6 FetchingReport.py clear
if len(sys.argv) == 2:
    if sys.argv[1] == 'clear':
        print('remove data from all tables first')
        for api,table in mapTable.items():
            sql = 'delete from %s'%(table)
            session.execute(sql)
            session.commit()

#Retrieve the data through the definition of mapTable
for api,table in mapTable.items():
    retrieveData(api,table,quarter_list)

#ying li neng li
#ts.get_profit_data(2014,3)
#ying yun nengli
#ts.get_operation_data(2014,3)
#cheng zhang neng li
#ts.get_growth_data(2014,3)
#chang zai neng li
#ts.get_debtpaying_data(2014,3)
#xian jin liu liang
#ts.get_cashflow_data(2014,3)
