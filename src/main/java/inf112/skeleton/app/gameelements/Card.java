package inf112.skeleton.app.gameelements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import inf112.skeleton.app.actor.Player;
import org.lwjgl.opengl.Display;

public class Card implements Comparable<Card>{
    private final int priority,
            move,
            type;
    //We need this to print out the type of the card
    private static final String[] types = {"Forward Card", "Backward Card", "Turn Card"};

    private Player owner;

    private Sprite sprite;

    public Card(int priority, int type, int move) {
        this.type = type;
        this.move = move;
        this.priority = priority;
        this.setOwner(null);
    }

    /**
     * Gives you a string version of the card.
     * @return String of the form: "Priority: | Type:  |  Move: "
     */
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
        return Integer.compare(that.priority, this.priority);
    }

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

    /**
     * Sets the given actor as the cards owner
     * @param owner actor to become this cards owner
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Gives the card its designated sprite
     */
    private void giveSprite() {
        Pixmap card;
        Pixmap resizedCard;
        Texture texture;

        switch (getType()) {
            case (0):
                card = new Pixmap(Gdx.files.internal("pictures/Cards/Move" + getMove() + ".png"));
                break;
            case (1):
                card = new Pixmap(Gdx.files.internal("pictures/Cards/BackUp.png"));
                break;
            case (2):
                card = new Pixmap(Gdx.files.internal("pictures/Cards/Turn" + getMove() + ".png"));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + toString());
        }
        resizedCard = new Pixmap(Display.getWidth() / 10, 350, card.getFormat());
        resizedCard.drawPixmap(card,
                0, 0, card.getWidth(), card.getHeight(),
                0, 0, resizedCard.getWidth(), resizedCard.getHeight());
        texture = new Texture(resizedCard);
        sprite = new Sprite(texture);

        card.dispose();
        resizedCard.dispose();

    }

    /**
     * Allocates a sprite to the card, and returns the sprite it was given
     * @return Sprite of the card
     */
    public Sprite allocateSprite() {
        giveSprite();
        return sprite;
    }

}
