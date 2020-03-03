package inf112.skeleton.app;

public class Card implements Comparable<Card>{
    private final int priority, move, name;
    //We need this to print out the name of the card type
    private static final String[] names = {"Forward Card", "Backward Card", "Turn Card"};

    private Player owner;

    public Card(int priority, int name, int move) {
        this.name = name;
        this.move = move;
        this.priority = priority;
    }

    //Method for testing/checking
    public String toString() {
        return "Priority: " + this.priority + " | Type:  " + names[this.name] + " | Move: " + this.move;
    }

    /**
     * @param that for the card to be compared
     * @return true if it is equal
     */
    public boolean equals(Card that) {
        return this.priority == that.priority
                && this.name == that.name
                && this.move == that.move;
    }

    @Override
    public int compareTo(Card that) {
        return Integer.compare(this.priority, that.priority);
    }

    //Getters might be redundant
    public int getPriority() { return priority; }
    public int getName() { return name; }
    public int getMove() { return move; }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
