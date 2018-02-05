from django.conf.urls import url

from . import views

urlpatterns = [

    url(r'^$', views.index, name='index'),

	# ../trading/stock/
    #url(r'^stock/$', views.stock_all, name='stock_all'),

	# ../trading/stock/ntap/
    url(r'stock/(?P<s_name>.*)\/$', views.stock_detail, name='stock_detail'),

	# ../trading/trader/
    url(r'^trader/$', views.trader_all, name='trader_all'),

	# ../trading/trader/oswal/
    url(r'^trader/(?P<trader_name>.*)\/$', views.trader_detail, name='trader_detail'),

	# ../trading/register_trader
    url(r'^register_trader/$', views.get_trader, name='get_trader'),

	# ../trading/add_stock
    url(r'^add_stock/$', views.add_stock, name='add_stock'),
]
