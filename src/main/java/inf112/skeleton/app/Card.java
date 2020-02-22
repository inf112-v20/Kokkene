package inf112.skeleton.app;

public class Card {
    private final int priority, move, name;
    private static final String[] names = {"Movement Card", "Rotation Card", "Skip Card"};

    public Card(int priority, int name, int move) {
        this.name = name;
        this.move = move;
        this.priority = priority;
    }

    //Method for testing/checking
    public String toString() {
        String s = "Priority: " + this.priority + " | Type:  " + names[this.name] + " | Move: " + this.move;
        return s;
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
        if (this.priority < that.priority) {
            return -1;
        }
        if (this.priority > that.priority) {
            return 1;
        }

        return 0;
    }

    //Getters might be redundant
    public int getPriority() {
        return priority;
    }
    public int getMove() {
        return move;
    }
    public int getName() {
        return name;
    }
}
