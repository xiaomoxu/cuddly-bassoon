from django import forms
from django.core.exceptions import ValidationError
from .models import Trader, Stock, Viewer
from django.utils import timezone

import logging
logger = logging.getLogger("django")

class StockForm(forms.Form):
    logger.info("log->forms.StockForm")
    all_traders = Trader.objects.all()
    trader = forms.ChoiceField(label='Trader Name', required='True', choices=[(x, x.name) for x in all_traders])
    stock = forms.CharField(label='Stock Name', max_length=100)
    cur_price = forms.FloatField(label='Current Price')
    pre_price = forms.FloatField(label='Predicted Price')
    rating = forms.IntegerField(label='Rating')
    pub_date = forms.DateTimeField(label='Add Date')

class TraderForm(forms.Form):
    logger.info("log->forms.TraderForm")
    name = forms.CharField(label='Your Name', max_length=100)
    org_name = forms.CharField(label='Org Name', max_length=100)
    e_mail = forms.EmailField(label='E-mail')
    phone = forms.IntegerField(label='phone')
