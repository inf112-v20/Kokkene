package inf112.skeleton.app;

public class Card {
    private int priority, move;
    private String name;

    public Card(int priority, String name, int move) {
        this.name = name;
        this.move = move;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
