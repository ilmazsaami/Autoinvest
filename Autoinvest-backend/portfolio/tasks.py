import json

from yahoo_finance import Share
from django.conf import settings
from portfolio.models import ETF, Portfolio
import datetime
from django.utils import timezone


def set_ETF_data():
    etf_data = []

    for index, etf_symbol in enumerate(settings.ETF_MASTER_LIST):
        etf_dict = {
            'model': 'portfolio.ETF',
            'pk': index + 1,
            'fields': {},
        }

        fund = Share(etf_symbol)

        fields = {
            'name': fund.get_name(),
            'symbol': etf_symbol,
            'last_trade': fund.get_price(),
            'dividend_yield': fund.get_dividend_yield(),
            'absolute_change': fund.get_change(),
            'percentage_change': fund.get_percent_change(),
            'year high': fund.get_year_high(),
            'year low': fund.get_year_low(),
            '50 day moving average': fund.get_50day_moving_avg(),
            '200 day moving average': fund.get_200day_moving_avg(),
            'average_daily_volume': fund.get_avg_daily_volume()
        }

        etf_dict['fields'] = fields
        etf_data.append(etf_dict)
    json_data = json.dumps(etf_data)

    # print(json_data)

    output_dict = [y for y in etf_data if y['fields']['dividend_yield'] > 1]

    output_dict = [x for x in output_dict if x['fields']['average_daily_volume'] > 100000]

    output_dict = [z for z in output_dict if z['fields']['200 day moving average'] < z['fields']['last_trade']]

    sorted_list = sorted(output_dict, key=lambda k: k['fields']['dividend_yield'], reverse=True)

    for etf in sorted_list[:5]:
        ETF.objects.create(portfolio=Portfolio.objects.get(pk=1),
                           name=etf['fields']['name'],
                           symbol=etf['fields']['symbol'],
                           investment_style=1,
                           last_trade=etf['fields']['last_trade'],
                           dividend_yield=etf['fields']['dividend_yield'],
                           absolute_change=etf['fields']['absolute_change'],
                           percentage_change=etf['fields']['percentage_change'],
                           currency='USD',
                           last_updated=timezone.now())
