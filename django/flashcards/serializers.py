from rest_framework import serializers

from flashcards.models import Deck, Card


class DeckSerializer(serializers.ModelSerializer):

    class Meta:
        model = Deck
        fields = ['name',]
        read_only_fields = ['user',]