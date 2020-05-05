package inf112.skeleton.app.actor;

import inf112.skeleton.app.gameelements.Card;
import inf112.skeleton.app.gameelements.Deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hand {
    //Stores the cards the actor has in his/her hands.
    public Card[] plHand;
    public ArrayList<Card> backupHand = new ArrayList<>();

    private final Player owner;

    //Selected cards
    private final List<Card> selected = new ArrayList<>();

    //Locked registers
    private final ArrayList<Card> locked = new ArrayList<>();

    /**
     * The constructor for the Hand
     * @param actor The owner of this hand
     * @param deck What deck to draw from
     */
    public Hand(Player actor, Deck deck) {
        owner = actor;
        drawCards(deck);
    }

    /**
     * Gets the selected cards for this round
     * @return List<Card> of cards to play
     */
    public List<Card> getSelected(){
        return selected;
    }

    /**
     * Get the currently locked cards
     * @return ArrayList of cards
     */
    public ArrayList<Card> getLocked(){
        return locked;
    }

    /**
     * How many cards you may select in this round
     * @return # of cards to select
     */
    public int cardsToSelect(){
        return Math.min(5, owner.getHealth() - 1);
    }


    /**
     * Toggles a card and adds it to your list of selected card, also removes the card if already selected
     * @param c The card to be selected
     */
    public void toggleCard(Card c) {
        if (selected.contains(c)) {
            selected.remove(c);
            return;
        }
        selected.add(c);
    }

    /**
     * Locks the registers for the given actor
     */
    public void lockRegister(){
        int backupSize = backupHand.size(),
                selectedSize = selected.size(),
                lockedSize = locked.size();
        if (locked.size() < lockedRegisters()) {
            if(owner.playerPower) {
                locked.add(backupHand.get(backupSize - 1));
                backupHand.remove(backupSize - 1);
            }
            else {
                locked.add(selected.get(selectedSize - 1));
                selected.remove(selectedSize - 1);
            }
        } else if (locked.size() > lockedRegisters()) {
            selected.add(locked.get(lockedSize - 1));
            locked.remove(lockedSize - 1);
        }
        if (lockedSize != lockedRegisters()) {
            lockRegister();
        }
        assert lockedSize == lockedRegisters() : "Unexpected value: " + lockedSize;
    }

    /**
     * Checks how many cards in the hand, that are supposed to be locked
     * @return # of cards to lock
     */
    public int lockedRegisters(){
        return Math.max(0, 6-owner.getHealth());
    }

    /**
     * Discards the current hand, and draws a new one from the given deck
     * @param deck The deck in use
     */
    public void discardDraw(Deck deck){
        for (Card c : plHand) {
            if (locked.contains(c) && locked.indexOf(c) < lockedRegisters()) {
                continue;
            }
            deck.Discard.add(c);
            locked.remove(c);
        }

        if (!owner.playerPower) {
            selected.clear();
            drawCards(deck);
            if (owner instanceof AI) {
                ((AI) owner).aiMove();
            }
        }
    }

    /**
     * Draws a fresh hand of cards to the player
     * @param deck The deck in use
     */
    public void drawCards(Deck deck) {
        plHand = new Card[owner.getHealth() - 1];
        for(int i = 0; i < plHand.length; i++) {
            if (deck.Cards.isEmpty()){ // shuffles when empty
                deck.empty();
            }

            plHand[i] = deck.Cards.poll();
            assert plHand[i] != null;
            plHand[i].setOwner(owner);
            backupHand.add(plHand[i]);
        }
        Arrays.sort(plHand);
    }
}
