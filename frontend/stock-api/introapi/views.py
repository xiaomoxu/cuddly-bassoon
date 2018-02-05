# -*- coding: utf-8 -*-

from django.shortcuts import render, get_object_or_404
from django.http import HttpResponse,JsonResponse
from django.core import serializers
from itertools import chain

from introapi.models import TopClass,SubClass,Trader

import logging
logger = logging.getLogger("django")

# Create your views here.
def introapilist(request):
    all_top = TopClass.objects.values() 
    all_sub = SubClass.objects.order_by('id')
    logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
    logger.info(type(all_top))
    for it in all_top:
        logger.info(type(it))
        logger.info(it['class_top_id'])
    logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
    logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
    for it in all_sub:
        logger.info(it)
    logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")    
    contexttemp=chain(all_top,all_sub)
    context={'all_data':contexttemp}
    logger.info("#############################")
    for item in contexttemp:
        logger.info(item)
    logger.info("#############################")
    
    #JSON serialize
    '''
    all_top = serializers.serialize('json', all_top)
    str_symptom = str(all_top).replace('u\'','\'')  
    str_symptom.decode("unicode-escape")
    return HttpResponse(JsonResponse(context,safe=False))
    #return HttpResponse(contexttemp)
    #JSON serialize
    '''
    return render(request, 'introapi/intro_main.html',context)
    
def stock_all(request):
    pass
    '''
    sub_api = SubClass.objects.order_by('id')
    context = {'sub_api': sub_api}
    return render(request, 'introapi/intro_all.html', context)
    '''