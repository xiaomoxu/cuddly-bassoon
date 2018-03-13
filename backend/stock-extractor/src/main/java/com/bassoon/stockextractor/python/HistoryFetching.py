# 这个api将被抛弃，请使用k_history_fetching.py
import pika
import tushare as ts

stock_basics = ts.get_stock_basics()
codeArray = stock_basics.index
credentials = pika.PlainCredentials('user', 'password')
connection = pika.BlockingConnection(pika.ConnectionParameters('10.20.118.28', 5672, '/', credentials))
channel = connection.channel()

history = ts.get_hist_data('601318')
jsonString = history.to_json(orient='records')
# print(jsonString)
channel.basic_publish(exchange='topicExchange', routing_key='com.bassoon.queue.transaction',
                      body='601318' + '#' + jsonString)

# for code in codeArray:
#     history = ts.get_hist_data(code)
#     # engine = create_engine('mysql://root:Liaobi()7595k@10.20.116.107/CN_BASSOON?charset=utf8')
#     # history.to_sql('stock_history', engine, if_exists='append')
#     jsonString = history.to_json(orient='records', force_ascii='false')
#     channel.basic_publish(exchange='topicExchange', routing_key='com.bassoon.queue.transaction',
#                           body=code + '#' + jsonString)
connection.close()
