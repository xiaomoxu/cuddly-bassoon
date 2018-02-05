# -*- coding: utf-8 -*-

from django.shortcuts import render, get_object_or_404
from django.http import HttpResponse

from introapi.models import Stock, Trader

import logging
logger = logging.getLogger("django")

# Create your views here.
def introapilist(request):
    all_stocks = Stock.objects.order_by('id')
    logger.info(all_stocks)
    context = {'all_stocks': all_stocks}
    return render(request, 'introapi/intro_main.html',context)
    
def stock_all(request):
    pass
    #all_stocks = Stock.objects.order_by('-pub_date')
    #context = {'all_stocks': all_stocks}
    #return render(request, 'introapi/intro_all.html', context)
