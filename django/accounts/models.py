from django.db import models
from django.contrib.auth.models import AbstractBaseUser
from django_extensions.db.models import TimeStampedModel


class User(AbstractBaseUser, TimeStampedModel):


    pass