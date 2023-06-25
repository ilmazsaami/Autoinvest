import json

from yahoo_finance import Share

ETF_master_list = [
    'AADR',
    'AAXJ',
    'ACIM',
    'ACTX',
    'ACWF',
    'ACWI',
    'ACWV',
    'ACWX',
    'ADRA',
    'ADRD',
    'ADRE',
    'ADRU',
    'ADZ',
    'AFK',
    'AFTY',
    'AGA',
    'AGF',
    'AGG',
    'AGGY',
    'AGND',
    'AGQ',
    'AGZ',
    'AGZD',
    'AIA',
    'AIRR',
    'ALD',
    'ALFA',
    'ALFI',
    'ALTS',
    'ALTY',
    'AMJ',
    'AMLP',
    'AMU',
    'AMZA',
    'AND',
    'ANGL',
    'AOA',
    'AOK',
    'AOM',
    'AOR',
    'ARGT',
    'ARKG',
    'ARKK',
    'ARKQ',
    'ARKW',
    'ASEA',
    'ASET',
    'ASHR',
    'ASHS',
    'ASHX',
    'ATMP',
    'AUNZ',
    'AUSE',
    'AXJL',
    'AXJV',
    'AYT',
    'BAB',
    'BAL',
    'BBC',
    'BBH',
    'BBP',
    'BBRC',
    'BCM',
    'BDCL',
    'BDCS',
    'BDD',
    'BFOR',

]

etf_data = []

for index, ETF in enumerate(ETF_master_list):

    etf_dict = {
        'model': 'portfolio.ETF',
        'pk': index + 1,
        'fields': {},
    }
    
    fund = Share(ETF)
    
    fields = {
        'name': fund.get_name(),
        'symbol': ETF,
        'last_trade': fund.get_price(),
        'dividend_yield': fund.get_dividend_yield() ,
        'absolute_change': fund.get_change(),
        'percentage_change': fund.get_percent_change(),
        'year high':  fund.get_year_high(),
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

sorted_list = sorted(output_dict, key=lambda k: k['fields']['dividend_yield'], reverse = True)

print(sorted_list)
# print(output_dict)