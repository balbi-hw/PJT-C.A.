from django.urls import path
from flashcards import views


urlpatterns = [
    path('decks/', views.deck_list),
    path('decks/<int:deck_pk>/', views.deck_detail),
]
