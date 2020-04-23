package inf112.skeleton.app.actor;

import inf112.skeleton.app.gameelements.Card;
import inf112.skeleton.app.gameelements.Deck;

import java.util.ArrayList;
import java.util.Arrays;

public class Hand {
    //Stores the cards the actor has in his/her hands.
    public Card[] plHand;
    public ArrayList<Card> backupHand = new ArrayList<>();

    public Hand(Player actor, Deck deck) {
        plHand = new Card[actor.getHealth() - 1];

        for(int i = 0; i < plHand.length; i++) {
            if (deck.Cards.isEmpty()){ // shuffles when empty
                deck.empty();
            }
            plHand[i] = deck.Cards.poll();
            assert plHand[i] != null;
            plHand[i].setOwner(actor);
            backupHand.add(plHand[i]);
        }
        Arrays.sort(plHand);
    }
}
