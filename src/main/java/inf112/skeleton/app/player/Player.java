package inf112.skeleton.app.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.app.objects.Card;
import inf112.skeleton.app.objects.Deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * The robot start with a name, xPos, yPos and orientation on the board.
 * When created health is 10, and while health is above 0 its alive.
 * The robot has a backup on the map according to if it stood on a flag last turn.
 * The backup is at first the initial spawn.
 * When spawning at backup, keep the orientation.
 *
 * Martin Johnsen
 *
 */

public class Player {

    //ID of the robot
    private String name;
    private int id;

    private PlayerState ps;
    private PlayerState hb;

    //Main position and Backup Position on the board
    private int xPos,
            yPos,
            xBackup,
            yBackup;

    //Direction the robot is facing (north=0, east=1, south=2, west=3)
    private int orientation;

    //Health of the robot
    private int health = 10,
            lifePoints = 3;

    //The next objective the Player has to go to to score points.
    private int objective = 1;

    //the cards the player holds
    private Card[] cards;

    //Selected cards
    private List<Card> selected;

    //Locked registers
    private ArrayList<Card> locked = new ArrayList<>();

    //Ready to play selected cards
    private boolean ready;

    //Constant variables
    private final int MAXHEALTH = 10;

    public boolean playerPower = false;

    //Player constructor had to be like this for testing

    /**
     * @param name  the name for this robot.
     * @param xPos  starting x-position for this robot.
     * @param yPos  starting y-position for this robot.
     * @param orientation  orientation (direction) in a 0-3 scale.
     * @param id numeric ID of the player
     */
    public Player(String name, int xPos, int yPos, int orientation, int id) {
        this.name = name;
        this.id = id;
        this.xPos = xPos;
        this.yPos = yPos;
        this.orientation = orientation;
        this.xBackup = xPos;
        this.yBackup = yPos;
        this.ready = false;
    }

    /**
     * Get the PlayerState containing the various textures for the player
     *
     * @return PlayerState of this player
     */
    public PlayerState getPlayerState() {
        return ps;
    }

    /**
     * Set the PlayeState of this player
     *
     * @param ps PlayerState to store in a field in this player
     */
    public void setPlayerState(PlayerState ps) {
        this.ps = ps;
    }

    /**
     * Get the PlayerState containing the textures of the healthbars
     *
     * @return PlayerState of HealthBars of this player
     */
    public PlayerState getHealthBars() {
        return hb;
    }

    /**
     * Set the PlayerState containing the textures of the healthbars
     *
     * @param hb PlayerState of HealthBars to store in a field in this player
     */
    public void setHealthBars(PlayerState hb) {
        this.hb = hb;
    }

    /**
     * Get whether the player is done selecting cards and thus is ready to play
     *
     * @return whether player is ready
     */
    public boolean getReady() {
        return this.ready;
    }

    /**
     * Set the readiness of the player to given boolean value b
     *
     * @param b boolean value to set the ready field to
     */
    public void setReady(boolean b) {
        this.ready = b;
    }

    /**
     * Toggles whether ready or not
     */
    public boolean toggleReady() {
        this.ready = !this.ready;
        return this.ready;
    }

    /**
     * The current position is the new backup
     */
    public void newBackup() {
        this.xBackup = this.xPos;
        this.yBackup = this.yPos;
    }

    /**
     * @return respawn x-position
     */
    public int getxBackup() { return xBackup; }

    /**
     * @return respawn y-position
     */
    public int getyBackup() {
        return yBackup;
    }

    /**
     * @return name of the specific robot.
     */
    public String getName() {
        return name;
    }

    /**
     * @return ID of specific robot
     */
    public int getId() {
        return id;
    }

    /**
     * @return the current x Position for this robot.
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * Replace x position with a new one
     *
     * @param xPos new x position
     */
    public void setxPos(int xPos) { this.xPos = xPos; }

    /**
     * @return the current y position for this robot.
     */
    public int getyPos() { return yPos; }

    /**
     * @param yPos  the y-value this robot should have.
     */
    public void setyPos(int yPos) { this.yPos = yPos; }

    /**
     * @return current rotation in a 0-3 scale.
     */
    public int getOrientation() {
        return orientation % 4;
    }

    /**
     * Sets the direction
     * @param orientation  orientation is the direction in a 0-3 scale.
     */
    public void setOrientation(int orientation) {
        this.orientation = Math.abs(orientation)%4;
    }

    /**
     * Turns the robot
     * @param change positive numbers are to the left, negative turns to the right
     */
    public void turn(int change){
        change += getOrientation();

        while (change<0) change +=4;
        //change will never be less than 0;
        setOrientation(change % 4);
    }

    public int getMaxHealth() { return MAXHEALTH; }

    /**
     *
     * @return current health total.
     */
    public int getHealth() { return health; }

    /**
     * @param health The amount
     */
    public void addHealth(int health) {
        setHealth(this.health+health);
    }

    /**
     * @param health  The value that health will be set to.
     */
    private void setHealth(int health) {

        if (health <= 0) {
            this.health = 0;
        }
        else {
            this.health = Math.min(health, MAXHEALTH);
        }

        if (this.health <= 0) {
            setyPos(200 + id);
        }
    }

    /**
     * Changes the lifePoints
     */
    private void removeLifePoint() {
        this.lifePoints -= 1;
        if (isAlive()) resetPos();
    }

    /**
     * @return the amount of lifePoints
     */
    public int getLifePoints() { return this.lifePoints; }

    /**
     * @return true if robot is alive
     */
    public boolean isAlive() { return getLifePoints()>0; }

    public void resetPos() {
        setxPos(getxBackup());
        setyPos(getyBackup());
    }

    /**
     * @return the next objective to stand at to get points
     */
    public int getObjective() {
        return this.objective;
    }

    /**
     * @param ob  new objective
     */
    public void checkObjective(int ob) {
        if (ob==this.objective) {
            newBackup();
            this.objective += 1;
        }
    }

    /**
     * @param texture for the sprite of a player
     * @return true if robot is alive
     */
    public TextureRegion[][] setPlayerTextures (String texture) {
        Texture playerTexture = new Texture(texture);
        return TextureRegion.split(playerTexture, 300, 300);
    }

    /**
     * How many cards you may select in this round
     * @return # of cards to select
     */
    public int cardsToSelect(){
        return Math.min(5, getHealth() - 1);
    }

    /**
     * How many registers are locked such that one cannot change the cards
     * @return # of locked registers
     */
    public int lockedRegisters(){
        return Math.max(0, 6-getHealth());
    }

    /**
     * Lock the last selected card if damaged such that the register must lock
     */
    public void lockRegister(){
        if (locked.size() < lockedRegisters()) {
            locked.add(selected.get(selected.size() - 1));
            selected.remove(selected.size() - 1);
        } else if (locked.size() > lockedRegisters()) {
            selected.add(locked.get(locked.size() - 1));
            locked.remove(locked.size() - 1);
        }
        if (locked.size() != lockedRegisters()) {
            lockRegister(); //TODO get rid of infinite loop?
        }
        assert locked.size() == lockedRegisters() : "Unexpected value: " + locked.size();
    }

    /**
     * gives cards to player based on how much health is left
     * @param deck used to get cards from
     */
    public void setHand(Deck deck) {
        //The hand of the player.
        Card[] playerHand = new Card[getHealth() - 1];
        selected = new ArrayList<>();

        for(int i = 0; i < playerHand.length; i++) {
            if (deck.Cards.isEmpty()){ // shuffles when empty
                deck.empty();
            }
            playerHand[i] = deck.Cards.poll();
            assert playerHand[i] != null;
            playerHand[i].setOwner(this);
        }
        Arrays.sort(playerHand);
        this.cards = playerHand;
    }

    /**
     *
     * @return returns the cards the current player is holding
     */
    public Card[] getCards() {
        return this.cards;
    }

    /**
     * Toggles whether a card is selected or not
     *
     * @param c is the card we toggle
     */
    public void toggleCard(Card c) {
        if (selected.contains(c)) {
            selected.remove(c);
            return;
        }
        selected.add(c);
    }

    /**
     * Get the list of currently selected cards
     * @return list of cards
     */
    public List<Card> getSelected(){
        return selected;
    }

    /**
     * Get the list of currently locked cards, this list will be in reverse order of execution
     * @return list of locked cards
     */
    public ArrayList<Card> getLocked(){
        return locked;
    }

    /**
     * Discards all current cards except the ones locked, and draw a new hand
     * @param deck deck to draw from and return the cards to
     */
    public void discardDraw(Deck deck){
        for (Card c : cards) {
            if (locked.contains(c) && locked.indexOf(c) < lockedRegisters()) {
                continue;
            }
            deck.Discard.add(c);
            locked.remove(c);
        }
        setHand(deck);
    }

    /**
     * Respawn function to be called after each round, will set position to backup coords and remove 1 life
     */
    public void respawn() {
        if (getHealth() <= 0 && isAlive()){
            removeLifePoint();
            this.health = MAXHEALTH;
        }
    }
}
