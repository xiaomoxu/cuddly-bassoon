from django.contrib import admin

from introapi.models import TopClass,SubClass, Trader

import sys;
reload(sys);
sys.setdefaultencoding("utf8")

# Register your models here.
admin.site.register(TopClass)
admin.site.register(SubClass)
admin.site.register(Trader)
