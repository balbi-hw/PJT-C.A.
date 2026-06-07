package anki.hw.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "cards")
@Getter @Setter
public class Card {

    @Id @GeneratedValue
    @Column(name = "card_id")
    private Long id;

    private String word;
    private String meaning;

    @Column(length = 1000)
    private String imageUrl;

    @Column(length = 1000)
    private String imageThumbnailUrl;

    private String imageSource;

    private String imageAuthor;

    @Column(length = 1000)
    private String imageSourceUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "deck_id")
    private Deck deck;

    public static Card createCard(String word, String meaning) {
        Card card = new Card();
        card.word = word;
        card.meaning = meaning;
        return card;
    }

    void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void changeContent(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public void changeImage(
            String imageUrl,
            String imageThumbnailUrl,
            String imageSource,
            String imageAuthor,
            String imageSourceUrl
    ) {
        this.imageUrl = imageUrl;
        this.imageThumbnailUrl = imageThumbnailUrl;
        this.imageSource = imageSource;
        this.imageAuthor = imageAuthor;
        this.imageSourceUrl = imageSourceUrl;
    }

    public void removeImage() {
        this.imageUrl = null;
        this.imageThumbnailUrl = null;
        this.imageSource = null;
        this.imageAuthor = null;
        this.imageSourceUrl = null;
    }

}
