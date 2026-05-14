from accounts.models import User
from flashcards.models import Deck, Card

from flashcards.serializers import DeckSerializer

def get_user_decks(user_pk):
    return User.objects.get(pk=user_pk).deck_set.all()


def create_deck(user_pk, serializer):
    return serializer.save(user=User.objects.get(pk=user_pk))


def get_deck(deck_pk):
    return DeckSerializer(Deck.objects.get(pk=deck_pk))


def update_deck(request, deck_pk):
    return DeckSerializer(Deck.object.get(pk=deck_pk), data=request.data)