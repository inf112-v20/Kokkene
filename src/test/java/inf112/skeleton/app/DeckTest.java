package inf112.skeleton.app;

import inf112.skeleton.app.objects.Deck;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class DeckTest {

    Deck deck;

    @Before
    public void makeDeck() throws IOException {
        deck = new Deck();
    }

    @Test
    public void deckContainsTheRightAmountOfCards() {
        assertEquals(84, deck.Cards.size());
    }
}