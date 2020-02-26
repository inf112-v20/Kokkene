package inf112.skeleton.app;

public class Card {
    private final int priority, move, name;
    private static final String[] names = {"Forward Card", "Backward Card", "Turn Card" , "Again Card"};

    public Card(int priority, int name, int move) {
        this.name = name;
        this.move = move;
        this.priority = priority;
    }

    //Method for testing/checking
    public String toString() {
        return "Priority: " + this.priority + " | Type:  " + names[this.name] + " | Move: " + this.move;
    }

    //Method for testing
    public boolean equals(Card that) {
        return this.priority == that.priority
                && this.name == that.name
                && this.move == that.move;
    }

    /**
     * Checks if our card's priority is greater than the other
     * @param that the card we are going to compare with.
     * @return Returns -1 if the card is less, 1 if it is greater and 0 if they are equal.
     */
    public int compareTo(Card that) {
        return Integer.compare(this.priority, that.priority);
    }

    //Getters might be redundant
    public int getPriority() { return priority; }
    public int getMove() { return move; }
    public int getName() { return name; }
}
