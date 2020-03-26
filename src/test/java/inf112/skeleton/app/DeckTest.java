package inf112.skeleton.app;

import inf112.skeleton.app.objects.Deck;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class DeckTest {

    private Deck deck;

    @Before
    public void makeDeck() throws IOException {
        deck = new Deck("assets/decks/deck1994.txt");
    }

    @Test
    public void deckContainsTheRightAmountOfCards() {
        if(deck.deck.equals("assets/decks/deck1994.txt")) { assertEquals(84, deck.Cards.size()); }
        else {assertEquals(9, deck.Cards.size());}
    }
}
