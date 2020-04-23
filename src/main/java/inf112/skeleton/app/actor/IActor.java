package inf112.skeleton.app.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.app.gameelements.Card;
import inf112.skeleton.app.gameelements.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * An actor is a Robot on the board that can interact with the board elements, and is either controlled by the
 * computer (AI) or the user (Player).
 */
interface IActor {

    //Constant Fields
    int MAXHEALTH = 10;

    /**
     * Get the PlayerState containing the various textures for the actor
     *
     * @return PlayerState of this actor
     */
    PlayerState getPlayerState();

    /**
     * Set the PlayerState of this actor
     *
     * @param ps PlayerState to store in a field in this actor
     */
    void setPlayerState(PlayerState ps);

    /**
     * Get the PlayerState containing the textures of the healthbars
     *
     * @return PlayerState of HealthBars of this actor
     */
    PlayerState getHealthBars();

    /**
     * Set the PlayerState containing the textures of the healthbars
     *
     * @param hb PlayerState of HealthBars to store in a field in this actor
     */
    void setHealthBars(PlayerState hb);

    /**
     * Get whether the actor is done selecting cards and thus is ready to play
     *
     * @return whether actor is ready
     */
    boolean getReady();

    /**
     * Set the readiness of the actor to given boolean value b
     *
     * @param b boolean value to set the ready field to
     */
    void setReady(boolean b);

    /**
     * Toggles whether ready or not
     */
    boolean toggleReady();

    /**
     * The current position is the new backup
     */
    void newBackup();

    /**
     * @return respawn x-position
     */
    int getxBackup();

    /**
     * @return respawn y-position
     */
    int getyBackup();

    /**
     * @return name of the specific actor.
     */
    String getName();

    /**
     * @return ID of specific actor
     */
    int getId();

    /**
     * @return the current x Position for this actor.
     */
    int getxPos();

    /**
     * Replace x position with a new one
     *
     * @param xPos new x position
     */
    void setxPos(int xPos);

    /**
     * @return the current y position for this actor.
     */
    int getyPos();

    /**
     * @param yPos  the y-value this actor should have.
     */
    void setyPos(int yPos);

    /**
     * @return current rotation in a 0-3 scale.
     */
    int getOrientation();

    /**
     * Sets the direction
     * @param orientation  orientation is the direction in a 0-3 scale.
     */
    void setOrientation(int orientation);

    /**
     * Turns the actor
     * @param change positive numbers are to the left, negative turns to the right
     */
    void turn(int change);

    /**
     *
     * @return max health of the actor
     */
    int getMaxHealth();

    /**
     *
     * @return current health total.
     */
    int getHealth();

    /**
     * @param health The amount
     */
    void addHealth(int health);

    /**
     * @return the amount of lifePoints
     */
    int getLifePoints();

    /**
     * @return true if the actor is alive
     */
    boolean isAlive();

    /**
     * Resets the actors position to the current backup
     */
    void resetPos();

    /**
     * @return the next objective to stand at to get points
     */
    int getObjective();

    /**
     * @param ob  new objective
     */
    void checkObjective(int ob);

    /**
     * @param texture for the sprite of a actor
     * @return true if the actor is alive
     */
    TextureRegion[][] setPlayerTextures (String texture);

    /**
     * How many cards you may select in this round
     * @return # of cards to select
     */
    int cardsToSelect();

    /**
     * How many registers are locked such that one cannot change the cards
     * @return # of locked registers
     */
    int lockedRegisters();

    /**
     * Lock the last selected card if damaged such that the register must lock
     */
    void lockRegister();

    /**
     * gives cards to actor based on how much health is left
     * @param deck used to get cards from
     */
    void setHand(Deck deck);

    /**
     *
     * @return returns the cards the current actor is holding
     */
    Card[] getCards();

    /**
     * Toggles whether a card is selected or not
     *
     * @param c is the card we toggle
     */
    void toggleCard(Card c);

    /**
     * Get the list of currently selected cards
     * @return list of cards
     */
    List<Card> getSelected();

    /**
     * Get the list of currently locked cards, this list will be in reverse order of execution
     * @return list of locked cards
     */
    ArrayList<Card> getLocked();

    /**
     * Discards all current cards except the ones locked, and draw a new hand
     * @param deck deck to draw from and return the cards to
     */
    void discardDraw(Deck deck);

    /**
     * Respawn function to be called after each round, will set position to backup coords and remove 1 life
     */
    void respawn();

    /**
     * Gets the color of the AI.
     * @return a color of type badlogic.gdx.graphics.color
     */
    Color getColor();
}
