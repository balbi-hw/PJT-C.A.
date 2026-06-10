package anki.hw.controller;

import anki.hw.service.CardService;
import anki.hw.service.DeckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final DeckService deckService;

    @GetMapping("/cards")
    public String list(@RequestParam(required = false) Long deckId, Model model) {
        if (deckId == null) {
            model.addAttribute("cards", cardService.findCards());
            model.addAttribute("deck", null);
        } else {
            model.addAttribute("cards", cardService.findCardsByDeck(deckId));
            model.addAttribute("deck", deckService.findOne(deckId));
        }
        return "cards/cardList";
    }

    @GetMapping("/cards/new")
    public String createForm(@RequestParam(required = false) Long deckId, Model model) {
        model.addAttribute("decks", deckService.findDecks());
        model.addAttribute("selectedDeckId", deckId);
        return "cards/createCardForm";
    }

    @PostMapping("/cards/new")
    public String create(@RequestParam Long deckId,
                         @RequestParam String word,
                         @RequestParam String meaning,
                         @RequestParam(required = false) String imageUrl,
                         @RequestParam(required = false) String imageThumbnailUrl,
                         @RequestParam(required = false) String imageSource,
                         @RequestParam(required = false) String imageAuthor,
                         @RequestParam(required = false) String imageSourceUrl) {

        cardService.createCard(
                deckId,
                word,
                meaning,
                imageUrl,
                imageThumbnailUrl,
                imageSource,
                imageAuthor,
                imageSourceUrl
        );

        return "redirect:/cards?deckId=" + deckId;
    }
}
