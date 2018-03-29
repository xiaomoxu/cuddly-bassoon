import tushare as ts
import pandas as p
import datetime,time,string
from sqlalchemy import create_engine


def getQuarterList(start_year):
    #current = time.strftime('%Y%m',time.localtime(time.time()))
    current = '201712'      #cannot retrieve the data of 2018, so from 201712
    end_year = current[0:4]
    end_month= current[4:6]
    quarters = []

    if start_year > end_year:
        print 'start year should older than current'
    else:
        for year in range(int(start_year),int(end_year)+1):
            end_quarter = 4
            if year == int(end_year):
                end_quarter = int(end_month)/4 + 1
            for quarter in range(1,end_quarter + 1):
                quarters.append(str(year)+ ('%02d'%quarter))
    return quarters

def retrieveData(api_name,table_name,quarters):
    for quarter in quarter_list:
        year_int    = int(quarter[0:4])
        quarter_int = int(quarter[4:6])
        print 'Starting retrieve data of ' + api_name + ' of ',year_int, quarter_int
        #continue
        if api_name == 'profit':
            report = ts.get_profit_data(year_int,quarter_int)
        elif api_name == 'operation':
            report = ts.get_operation_data(year_int,quarter_int)
        elif api_name == 'growth':
            report = ts.get_growth_data(year_int,quarter_int)
        elif api_name == 'debtpaying':
            report = ts.get_debtpaying_data(year_int,quarter_int)
        elif api_name == 'cashflow':
            report = ts.get_cashflow_data(year_int,quarter_int)
        else:
            print 'Tushare API:' + api_name + ' wrong'
            break
        report['year'] = p.Series(year_int,index=report.index)
        report['quarter'] = p.Series(quarter_int,index=report.index)
        report.to_sql(table_name, engine, if_exists='append')
        print report

#which year is the data retrieved from ?
quarter_list = getQuarterList('2007')
engine = create_engine('mysql://root:Liaobi()7595k@10.20.116.107/CN_BASSOON?charset=utf8')

#ying li neng li
#ts.get_profit_data(2014,3)
retrieveData('profit','stock_profit_data',quarter_list)
#ying yun nengli
#ts.get_operation_data(2014,3)
retrieveData('operation','stock_operation_data',quarter_list)

#cheng zhang neng li
#ts.get_growth_data(2014,3)
retrieveData('growth','stock_growth_data',quarter_list)

#chang zai neng li
#ts.get_debtpaying_data(2014,3)
retrieveData('operation','stock_debtpay_data',quarter_list)

#xian jin liu liang
#ts.get_cashflow_data(2014,3)
retrieveData('cashflow','stock_cashflow_data',quarter_list)
