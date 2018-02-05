# -*- coding: utf-8 -*-

from django.conf.urls import include, url
from introapi import views

import logging
logger = logging.getLogger("django")
logger.info("log->introapi.url.py")


urlpatterns = [
    url(r'^$', views.introapilist, name='introapilist'),
    url(r'^stock/$', views.stock_all, name='stock_all'),
]
