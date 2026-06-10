package anki.hw.service;

import anki.hw.domain.Card;
import anki.hw.domain.Deck;
import anki.hw.repository.CardRepository;
import anki.hw.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final DeckRepository deckRepository;

    @Transactional
    public Long createCard(
            Long deckId,
            String word,
            String meaning,
            String imageUrl,
            String imageThumbnailUrl,
            String imageSource,
            String imageAuthor,
            String imageSourceUrl
    ) {
        Deck deck = deckRepository.findOne(deckId);

        Card card = Card.createCard(word, meaning);

        if (imageUrl != null && !imageUrl.isBlank()) {
            card.changeImage(
                    imageUrl,
                    imageThumbnailUrl,
                    imageSource,
                    imageAuthor,
                    imageSourceUrl
            );
        }

        deck.addCard(card);
        cardRepository.save(card);

        return card.getId();
    }

    @Transactional
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    public List<Card> findCards() {
        return cardRepository.findAll();
    }

    public Card findOne(Long cardId) {
        return cardRepository.findOne(cardId);
    }

    @Transactional
    public void updateCard(Long cardId, String word, String meaning) {
        Card card = cardRepository.findOne(cardId);
        card.changeContent(word, meaning);
    }

    @Transactional
    public void deleteCard(Long cardId) {
        Card card = cardRepository.findOne(cardId);
        Deck deck = card.getDeck();
        if (deck != null) {
            deck.removeCard(card);
        }
        cardRepository.delete(card);
    }

    public List<Card> findCardsByDeck(Long deckId) {
        return cardRepository.findByDeckId(deckId);
    }

}
