from __future__ import unicode_literals
from datetime import datetime

from django.db import models
from django.core.exceptions import ValidationError
from django.conf import settings
from django.utils import timezone

from yahoo_finance import Share


def valid_pct(val):
    if val.endswith("%"):
       return float(val[:-1])/100
    else:
       try:
          return float(val)
       except ValueError:
          raise ValidationError(
              _('%(value)s is not a valid pct'),
                params={'value': value},
           )


class Pool(models.Model):
    total_value = models.FloatField()


class Portfolio(models.Model):
    pool = models.ForeignKey(Pool, on_delete=models.CASCADE)
    name = models.TextField()
    investment_style = models.TextField(choices=settings.RISK_LEVELS)
    currency = models.TextField(choices=settings.CURRENCIES)
    total_investment = models.FloatField()
    total_shares = models.IntegerField()
    share_price = models.FloatField()

    def update_share_price(self):
        portfolio_etfs = ETF.objects.filter(portfolio=self)
        for etf in portfolio_etfs:
            etf.update_ETF_value()

    def add_ETF(self, etf):
        ETF.objects.create(portfolio=self,
                           name=etf['fields'].get('name'),
                           symbol=etf['fields'].get('symbol'),
                           investment_type=etf[''],
                           last_trade=etf['fields'].get('last_trade'),
                           dividend_yield=etf['fields'].get('dividend_yield'),
                           absolute_change=etf['fields'].get('absolute_name'),
                           percent_change=etf['field'].get('percent_change'))


class ETFManager(models.Manager):
    def update_ETF_values(self):
        all_etfs = self.all()
        for etf in all_etfs:
            etf.update_ETF_value()


class ETF(models.Model):
    portfolio = models.ForeignKey(Portfolio, on_delete=models.CASCADE)
    name = models.TextField(max_length=50)
    symbol =models.TextField(max_length=5)
    investment_style = models.TextField(choices=settings.RISK_LEVELS)
    last_trade = models.FloatField()
    dividend_yield = models.FloatField()
    absolute_change = models.FloatField()
    percentage_change = models.TextField(validators=[valid_pct])
    currency = models.TextField(choices=settings.CURRENCIES, default='USD')
    last_updated = models.DateTimeField(default=timezone.now)

    objects = ETFManager

    def update_ETF_value(self):
        fund = Share(self.symbol)
        self.last_trade = fund.get_price()
        self.absolute_change = fund.get_change()
        self.percentage_change = fund.get_percent_change()
        self.dividend_yield = fund.get_dividend_yield()
        self.last_updated = timezone.now()
        self.save()
