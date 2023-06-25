from django.http import HttpResponse
from django.contrib.auth import get_user_model
from django.contrib.auth import authenticate
from rest_framework import status
from rest_framework.decorators import api_view, authentication_classes, permission_classes
from rest_framework.response import Response
from rest_framework.authtoken.models import Token
from rest_framework.authentication import TokenAuthentication
from rest_framework.permissions import IsAuthenticated
import requests
from django.conf import settings
from portfolio.models import Portfolio

import json
import datetime

User = get_user_model()

@api_view(['POST'])
def create_user(request):
    """
    Retrieve, update or delete a snippet instance.
    """
    if request.method == 'POST':
        data = json.loads(request.body)
        if data.get('username') and data.get('password'):
            headers = {'Content-Type': 'application/json'}
            try:
                user = User.objects.get_by_natural_key(data['username'])
            except User.DoesNotExist:
                user = User.objects.create_user(username=data['username'],
                                                password=data['password'],
                                                portfolio=Portfolio.objects.get(pk=1))
                create_customer_data = {
                    'first_name': 'Josh',
                    'last_name': 'Kwok',
                    'address': {
                        'street_number': '231',
                        'street_name': 'Pinewood',
                        'city': 'Thornhill',
                        'state': 'NJ',
                        'zip': '11144'
                    }
                }
                r = requests.post('http://api.reimaginebanking.com/customers?key={0}'.format(settings.NESSIE_API_KEY),
                                  headers=headers,
                                  data=json.dumps(create_customer_data))
                customer_data = json.loads(r.text)
                customer_id = customer_data.get('objectCreated', {}).get('_id')
                nessie_data = {
                    'type': 'Checking',
                    'nickname': 'joshkwok',
                    'rewards': 0,
                    'balance': 1000,
                }
                r = requests.post('http://api.reimaginebanking.com/customers/{0}/accounts?key={1}'.format(customer_id,
                                                                                                          settings.NESSIE_API_KEY),
                                  headers=headers,
                                  data=json.dumps(nessie_data))
                account_data = json.loads(r.text)
                account_id = account_data.get('objectCreated', {}).get('_id')
                user.customer_id = customer_id
                user.account_id = account_id
                user.save()
                response_data = {
                    'message': 'Success',
                }
                response_status = status.HTTP_201_CREATED
            else:
                response_data = {
                    'message': 'Username {0} is taken.'.format(data['username'])
                }
                response_status = status.HTTP_409_CONFLICT
            return Response(data=response_data, status=response_status)
        return Response(data={'message': 'error'}, status=status.HTTP_404_NOT_FOUND)
    else:
        return Response(data={'message': 'error'}, status=status.HTTP_404_NOT_FOUND)


@api_view(['POST'])
def login_user(request):
    if request.method == 'POST':
        data = json.loads(request.body)
        user = authenticate(username=data.get('username'), password=data.get('password'))
        if user is not None:
            token, created = Token.objects.get_or_create(user=user)
            if not created:
                token.delete()
                token = Token.objects.create(user=user)
            response_data = {
                'message': 'Success',
                'token': token.key,
            }
            return Response(data=response_data, status=status.HTTP_202_ACCEPTED)
        return Response(data={'message': 'error'}, status=status.HTTP_404_NOT_FOUND)
    else:
        return Response(data={'message': 'error'}, status=status.HTTP_404_NOT_FOUND)


@api_view(['GET'])
@authentication_classes ((TokenAuthentication,))
@permission_classes ((IsAuthenticated,))
def get_account_data(request):
    user = User.objects.get(username=request.user.username)
    user.update_investment_value()
    account_id = user.account_id
    r = requests.get('http://api.reimaginebanking.com/accounts/{0}?key={1}'.format(account_id, settings.NESSIE_API_KEY))
    account_data = json.loads(r.text)
    percent_change = 0
    if user.book_value:
        percent_change = (user.investment_value - user.book_value)/user.book_value
    response_data = [
        {
            'name':'Account balance:',
            'value': account_data['balance']
        },
        {
            'name': 'Portfolio value:',
            'value': user.investment_value,
        },
        {
            'name': 'Book value',
            'value': user.book_value,
        },
        {
            'name': 'Change in dollars:',
            'value': user.investment_value - user.book_value,
        },
        {
            'name': 'Percent change:',
            'value': percent_change,
        }
    ]
    return Response(data=response_data, status=status.HTTP_200_OK)


@api_view(['POST'])
@authentication_classes ((TokenAuthentication,))
@permission_classes ((IsAuthenticated,))
def deposit(request):
    headers = {'Content-Type': 'application/json'}
    data = json.loads(request.body)
    user = User.objects.get(username=request.user.username)
    value = abs(data.get('value'))
    nessie_data = {
        'medium': 'balance',
        'payee_id': settings.NESSIE_INVESTMENT_ACCOUNT,
        'amount': value,
        'transaction_date': datetime.datetime.now().strftime('%Y-%m-%d'),
        'description': 'deposit'
    }
    r = requests.post('http://api.reimaginebanking.com/accounts/{0}/transfers?key={1}'.format(user.account_id, settings.NESSIE_API_KEY),
                      headers=headers,
                      data=json.dumps(nessie_data))
    if value and str(r.status_code).startswith('2'):
        user.deposit(value)
        response_data = {
            'message': 'Success'
        }
        return Response(data=response_data, status=status.HTTP_202_ACCEPTED)
    return Response(data={'message': 'error'}, status=status.HTTP_404_NOT_FOUND)


@api_view(['POST'])
@authentication_classes ((TokenAuthentication,))
@permission_classes ((IsAuthenticated,))
def withdraw(request):
    headers = {'Content-Type': 'application/json'}
    data = json.loads(request.body)
    user = User.objects.get(username=request.user.username)
    value = abs(data.get('value'))
    nessie_data = {
        'medium': 'balance',
        'payee_id': user.account_id,
        'amount': value,
        'transaction_date': datetime.datetime.now().strftime('%Y-%m-%d'),
        'description': 'deposit'
    }
    r = requests.post('http://api.reimaginebanking.com/accounts/{0}/transfers?key={1}'.format(settings.NESSIE_INVESTMENT_ACCOUNT, settings.NESSIE_API_KEY),
                      headers=headers,
                      data=json.dumps(nessie_data))
    if value and str(r.status_code).startswith('2'):
        user.withdraw(value)
        response_data = {
            'message': 'Success'
        }
        return Response(data=response_data, status=status.HTTP_202_ACCEPTED)
    return Response(data={'message': 'error'}, status=status.HTTP_404_NOT_FOUND)