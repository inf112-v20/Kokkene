package inf112.skeleton.app.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.app.objects.Card;
import inf112.skeleton.app.objects.Deck;
import inf112.skeleton.app.sound.Sound;

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

public class Player  {

    //ID of the robot
    private String name;

    //Main position and Backup Position on the board
    private int xPos, yPos, xBackup, yBackup;

    //Direction the robot is facing (north=0, east=1, south=2, west=3)
    private int orientation;

    //Health of the robot
    private int health = 10, lifePoints = 3;

    //The next objective the Player has to go to to score points.
    private int objective = 1;

    //If player makes sound.
    private boolean soundBool = true;

    //obvious sounds.
    private Sound damageSound;

    //the cards the player holds
    private Card[] cards;

    //Constant variables
    private static final int MAXHEALTH = 10;

    //Player constructor had to be like this for testing
    /**
     * @param name  the name for this robot.
     * @param xPos  starting x-position for this robot.
     * @param yPos  starting y-position for this robot.
     * @param orientation  orientation (direction) in a 0-3 scale.
     * @param playerSoundBool  true/false = ON/OFF.
     */
    public Player(String name, int xPos, int yPos, int orientation, boolean playerSoundBool) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.orientation = orientation;
        this.xBackup = xPos;
        this.yBackup = yPos;
        this.soundBool = playerSoundBool;
        if (playerSoundBool) { //Have to do this to make testing be possible
            damageSound = new Sound("assets/sound/oof_sound.mp3");
        }
    }

    /**
     * Sounds are ON by default
     */
    public Player(String name, int xPos, int yPos, int orientation) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.orientation = orientation;
        this.xBackup = xPos;
        this.yBackup = yPos;
        damageSound = new Sound("assets/sound/oof_sound.mp3");
    }

    /**
     * Set a new spawn position with an x and a y value.
     * @param xBackup  the new x-coordinate for spawn point.
     * @param yBackup  the new y-coordinate for spawn point.
     */
    public void setBackup(int xBackup, int yBackup) {
        this.xBackup = xBackup;
        this.yBackup = yBackup;
    }

    /**
     * @return respawn x-position
     */
    public int getxBackup() { return xBackup; }

    /**
     * @return respawn y-position
     */
    public int getyBackup() { return yBackup; }

    /**
     * @return name of the specific robot.
     */
    public String getName() { return name; }

    /**
     * @return the current x Position for this robot.
     */
    public int getxPos() { return xPos; }

    /**
     * Replace x position with a new one
     * @param xPos  new x position
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
     * @param change positive numbers are to the right, negative turns to the left
     */
    public void turn(int change){
        setOrientation((getOrientation() + change + 4) % 4);
    }

    public int getMaxHealth() { return MAXHEALTH; }

    /**
     *
     * @return current health total.
     */
    public int getHealth() { return health; }

    /**
     * @param health The amount to add or take away.
     */
    public void addHealth(int health) {
        setHealth(this.health+health);
        if (health<0 && soundBool) { damageSound.play(); }
    }

    /**
     * @param health  The value that health will be set to.
     */
    private void setHealth(int health) {
        this.health = Math.min(health, MAXHEALTH);
        if (this.health<=0) {
            addLifePoints(-1);
            if (getLifePoints()>0)
                this.health = MAXHEALTH;
        }
    }

    /**
     * Changes the lifePoints
     */
    private void addLifePoints(int life) {
        this.lifePoints += life;
        if (isAlive() && life < 0) resetPos();
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
     * gives cards to player based on how much health is left
     * @param deck used to get cards from
     */
    public void setHand(Deck deck) {
        //The hand of the player.
        Card[] playerHand = new Card[getHealth()];
        for(int i = 0; i < getHealth(); i++) {
            playerHand[i] = deck.Cards.poll();
            assert playerHand[i] != null;
            playerHand[i].setOwner(this);
        }
        this.cards = playerHand;
    }

    /**
     *
     * @return returns the cards the current player is holding
     */
    public Card[] getCards() {
        return this.cards;
    }

    public void muteToggle() { soundBool=!soundBool; }
}