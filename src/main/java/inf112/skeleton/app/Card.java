package inf112.skeleton.app;

public class Card {
    private int priority, move;
    private String name;

    public Card(String name, int priority, int move) {
        this.name = name;
        this.move = move;
        this.priority = priority;
    }
}
