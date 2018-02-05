from django.shortcuts import render, get_object_or_404
from django.http import HttpResponse

import logging
logger = logging.getLogger("django")

# Create your views here.
def intro(request):
    return render(request, 'staticpages/index.html')
