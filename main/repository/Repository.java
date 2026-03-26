package repository;

import model.WordCard;

import java.util.ArrayList;

public class Repository {
    private final ArrayList<WordCard> cards = new ArrayList<>();

    public void add(WordCard wordCard) {
        cards.add(wordCard);
    }

    public ArrayList<WordCard> findAll() {
        return cards;
    }

    public WordCard findByWord(String searchWord) {
        for (WordCard card : cards) {
            if (card.getWord().equals(searchWord)) {
                return card;
            }
        }

        return null;
    }

    public Boolean deleteByWord(String target) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getWord().equals(target)) {
                cards.remove(i);
                return true;
            }
        }
        return false;
    }

    public void updateMeaning(WordCard card, String newMean) {
        card.setMeaning(newMean);
    }

    public void updateExample(WordCard card, String newExample) {
        card.setExample(newExample);
    }
}
