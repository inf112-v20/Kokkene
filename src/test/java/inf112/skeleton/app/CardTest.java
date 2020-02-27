package inf112.skeleton.app;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class CardTest {

    Card card;

    @Before
    public void makeCard() {
        card = new Card(150,0,1);
    }

    @Test
    public void cardHasHigherPriorityThanOtherCard() {
        Card otherCard = new Card(100, 0, 1);
        assertEquals(card.compareTo(otherCard), 1);
    }

    @Test
    public void cardHasLessPriorityThanOtherCard() {
        Card otherCard = new Card(200, 0, 1);
        assertEquals(card.compareTo(otherCard), -1);
    }

    @Test
    public void twoDifferentCardsReturnsFalse() {
        Card otherCard = new Card(400, 0, 1);
        assertFalse(card.equals(otherCard));
    }

}