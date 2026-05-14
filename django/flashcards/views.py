from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status

from flashcards.serializers import DeckSerializer

from accounts.models import User
from flashcards.models import Deck

from flashcards.services import (
    get_user_decks,
    create_deck,
)




@api_view(['GET', 'POST'])
def deck_list(request, user_pk):

    if request.method == 'GET':
        decks = get_user_decks(user_pk)
        serializer = DeckSerializer(decks, many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        serializer = DeckSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        deck = create_deck(
            user_pk=user_pk, 
            serializer=serializer
        )
        
        return Response(
            DeckSerializer(deck.data),
            status=status.HTTP_201_CREATED,
        )
    

@api_view(['GET', 'POST', 'DELETE'])
def deck_detail(request, deck_pk):
    deck = Deck.objects.get(pk=deck_pk)

    if request.method == 'GET':
        serializer = DeckSerializer(deck)
        return Response(serializer.data)
    
    elif request.method == 'POST':
        serializer = DeckSerializer(data=request.data)
        if serializer.is_valid(raise_exception=400):
            serializer.save()
            return Response(status=status.HTTP_200_OK)
        
    elif request.method == 'DELETE':
        deck.delete()
        return Response(status=status.HTTP_200_OK)