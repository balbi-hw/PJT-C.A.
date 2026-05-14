from django.db import models
from django_extensions.db.models import TimeStampedModel

from accounts.models import User


class Deck(TimeStampedModel):
    user = models.ForeignKey(User, on_delete=models.CASCADE)

    name = models.CharField(max_length=50)
    size = models.IntegerField(default=0)


class Card(TimeStampedModel):
    deck = models.ForeignKey(Deck, on_delete=models.CASCADE)

    word = models.CharField(max_length=50)
    meaning = models.TextField()
    example = models.TextField()
