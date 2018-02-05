# -*- coding: utf-8 -*-
from __future__ import unicode_literals

# Create your models here.
from django.db import models



import logging
logger = logging.getLogger("django")


class Trader(models.Model):

    logger.info("log->Model.Trader")
    # ...
    def __str__(self):
        logger.info("where am i ")
        return self.name
    
    name = models.CharField(max_length=200)
    org_name = models.CharField(max_length=100)
    mobile = models.IntegerField(default=0)
    e_mail = models.EmailField(max_length=100)
    points = models.IntegerField(default=0)

class Stock(models.Model):
    logger.info("log->Model.Stock")
    # ...
    def __str__(self):
	    return self.class_top_name    
    
    trader = models.ForeignKey(Trader)
    class_top_name = models.CharField(max_length=200)
    class_top_id=models.IntegerField(default=0)