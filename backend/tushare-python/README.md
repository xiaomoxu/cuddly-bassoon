 Tushare是一个免费、开源的python财经数据接口包。
 主要实现对股票等金融数据从数据采集、清洗加工 到 数据存储的过程，能够为金融分析人员提供快速、整洁、和多样的便于分析的数据，为他们在数据获取方面极大地减轻工作量，使他们更加专注于策略和模型的研究与实现上。
 考虑到Python pandas包在金融量化分析中体现出的优势，Tushare返回的绝大部分的数据格式都是pandas DataFrame类型，非常便于用pandas/NumPy/Matplotlib进行数据分析和可视化。
 当然，如果您习惯了用Excel或者关系型数据库做分析，您也可以通过Tushare的数据存储功能，将数据全部保存到本地后进行分析。
 应一些用户的请求，从0.2.5版本开始，Tushare同时兼容Python 2.x和Python 3.x，对部分代码进行了重构，并优化了一些算法，确保数据获取的高效和稳定。



 pip3.6 install lxml
 pip3.6 install pandas
 pip3.6 install requests
 pip3.6 install bs4
 pip3.6 install tushare
 pip3.6 install flask-SQLAlchemy
 pip3.6 install mysqlclient

 导入的时候可能会遇到的问题:
 1.mysql Unknow error 1170 python tushare可能会根据某个字段建立索引，这个时候如果这个字段是text类型的，那么可能会遇到1170error，解决办法是把这个字段改成varchar 255长度，因为第一次执行的时候报错，但是表已经给建好了，只是建立索引的时候失败，
 直接修改column，然后根据错误提示重新建立索引就可以了。
 2.1049, 'Unknown error 1049' 这个问题是建数据库的时候字节编码不正确造成的，建议使用UTF-8编码，用navicat建，我用mysqladmin建的，但是一样报这个错，不知道为什么，可能是方言不一样导致的。
 3.'[date] not in index' 修改一下cons.py中的 FOR_CLASSIFY_B_COLS = ['date','code','name'],把date去掉就可以了
