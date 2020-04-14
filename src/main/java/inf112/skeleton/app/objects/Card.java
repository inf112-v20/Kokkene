package inf112.skeleton.app.objects;

import inf112.skeleton.app.actor.Player;

public class Card implements Comparable<Card>{
    private final int priority,
            move,
            type;
    //We need this to print out the type of the card
    private static final String[] types = {"Forward Card", "Backward Card", "Turn Card"};

    private Player owner;

    public Card(int priority, int type, int move) {
        this.type = type;
        this.move = move;
        this.priority = priority;
        this.setOwner(null);
    }

    //Method for testing/checking
    public String toString() {
        return "Priority: " + this.priority + " | Type:  " + types[this.type] + " | Move: " + this.move;
    }

    /**
     * @param that for the card to be compared
     * @return true if it is equal
     */
    public boolean equals(Card that) {
        return this.priority == that.priority
                && this.type == that.type
                && this.move == that.move;
    }

    @Override
    public int compareTo(Card that) {
        return Integer.compare(this.priority, that.priority);
    }

    //Getters might be redundant
    public int getPriority() { return priority; }
    public int getType() { return type; }
    public int getMove() { return move; }

    /**
     * Gets the owner of the card
     * @return Player that owns the card
     */
    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
