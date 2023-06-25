from django.urls import re_path,include
from . import views

urlpatterns = [
    re_path(r'^register', views.create_user),
    re_path(r'^login', views.login_user),
    re_path(r'^user', views.get_account_data),
    re_path(r'^deposit', views.deposit),
    re_path(r'^withdraw', views.withdraw),
]