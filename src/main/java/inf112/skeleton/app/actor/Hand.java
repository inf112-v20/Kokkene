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

    public Hand(Player actor, Deck deck) {
        owner = actor;
        drawCards(deck);
    }

    //Constructor for AI's
    public Hand(AI actor, Deck deck) {
        owner = actor;
        drawCards(deck);
    }

    /**
     * How many cards you may select in this round
     * @return # of cards to select
     */
    public int cardsToSelect(){
        return Math.min(5, owner.getHealth() - 1);
    }

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

    public int lockedRegisters(){
        return Math.max(0, 6-owner.getHealth());
    }

    public List<Card> getSelected(){
        return selected;
    }

    public ArrayList<Card> getLocked(){
        return locked;
    }

    public void toggleCard(Card c) {
        if (selected.contains(c)) {
            selected.remove(c);
            return;
        }
        selected.add(c);
    }

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
