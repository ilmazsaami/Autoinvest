from __future__ import unicode_literals

from django.db import models
from django.contrib.auth.models import AbstractUser, UserManager

from portfolio.models import Portfolio


class CustomUser(AbstractUser):
    customer_id = models.TextField(max_length=30, blank=True, default='')
    account_id = models.TextField(max_length=30, blank=True, default='')
    book_value = models.FloatField(default=0.0)
    portfolio = models.ForeignKey(Portfolio, null=True, default=None,on_delete=models.CASCADE)
    investment_value = models.FloatField(default=0.0)
    share_amount = models.IntegerField(default=0)

    def update_investment_value(self):
        if self.portfolio:
            self.investment_value = self.portfolio.share_price * self.share_amount
            self.save()

    def deposit(self, value):
        self.update_investment_value()
        shares_bought = value / self.portfolio.share_price
        self.book_value += value
        self.investment_value += value
        self.share_amount += shares_bought
        self.save()

    def withdraw(self, value):
        self.update_investment_value()
        shares_sold = value / self.portfolio.share_price
        if shares_sold < self.share_amount:
            self.book_value = (self.investment_value - value) / (self.investment_value) * self.book_value
            self.investment_value -= value
            self.share_amount -= shares_sold
            self.save()