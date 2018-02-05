from django.shortcuts import render, get_object_or_404
from django.http import HttpResponse
from .models import Stock, Trader, Viewer
from .forms import TraderForm, StockForm

# Create your views here.

def index(request):
    return render(request, 'trading/main_site.html')

def stock_detail(request, s_name):
    stock = get_object_or_404(Stock, stock_name=s_name)
    return render(request, 'trading/stock_detail.html', {'stock': stock})

def trader_detail(request, trader_name):
    trader = get_object_or_404(Trader, name=trader_name)
    return render(request, 'trading/trader_detail.html', {'trader': trader})

def stock_all(request):
    all_stocks = Stock.objects.order_by('-pub_date')
    context = {'all_stocks': all_stocks}
    return render(request, 'trading/stock_all.html', context)

def trader_all(request):
    all_traders = Trader.objects.all()
    context = {'all_traders': all_traders}
    return render(request, 'trading/trader_all.html', context)

def get_trader(request):
    if request.method == 'POST':
    	form = TraderForm(request.POST)

    	if form.is_valid():
			t = Trader.objects.create(name=form.cleaned_data['name'], org_name=form.cleaned_data['org_name'], e_mail=form.cleaned_data['e_mail'], mobile=form.cleaned_data['phone'], points=10)
			return render(request, 'trading/thank-you.html')

    else:
		form = TraderForm()

    return render(request, 'trading/trader_register.html', {'form': form})

def add_stock(request):
	if request.method == 'POST':
		form = StockForm(request.POST)
		if form.is_valid():
			tr = Trader.objects.get(name = form.cleaned_data['trader'])
			t = Stock.objects.create(trader=tr, stock_name=form.cleaned_data['stock'], cur_price=form.cleaned_data['cur_price'], predict_price=form.cleaned_data['pre_price'], Rating=form.cleaned_data['rating'], pub_date = form.cleaned_data['pub_date'])
			return render(request, 'trading/thank-you.html')
	else:
		form = StockForm()
		
	return render(request, 'trading/stock_add.html', {'form': form})
