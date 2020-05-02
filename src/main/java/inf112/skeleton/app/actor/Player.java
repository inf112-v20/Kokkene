package inf112.skeleton.app.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.app.gameelements.Card;
import inf112.skeleton.app.gameelements.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * The robot start with a name, xPos, yPos and orientation on the board.
 * When created health is 10, and while health is above 0 its alive.
 * The robot has a backup on the map according to if it stood on a flag last turn.
 * The backup is at first the initial spawn.
 * When spawning at backup, keep the orientation.
 *
 * @author Martin Johnsen
 *
 */

public class Player implements IActor {

    //The cards the player holds
    public Hand hand;

    //ID of the robot
    private final String name;
    private final int id;

    private PlayerState ps;
    private PlayerState hb;

    //Main position and Backup Position on the board
    private int xPos,
            yPos,
            xBackup,
            yBackup;

    //Direction the robot is facing (north=0, west=1, south=2, east=3)
    private int orientation;

    //Health of the robot
    private int health = 10,
            lifePoints = 3;

    //The next objective the Player has to go to to score points.
    private int objective = 1;

    //Ready to play selected cards
    private boolean ready;
    public boolean announcePowerDown = false;
    public boolean playerPower = false;

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

    //Getters and setters
    public PlayerState getPlayerState() {
        return ps;
    }

    public void setPlayerState(PlayerState ps) {
        this.ps = ps;
    }

    public PlayerState getHealthBars() {
        return hb;
    }

    public void setHealthBars(PlayerState hb) {
        this.hb = hb;
    }

    public boolean getReady() {
        return this.ready;
    }

    public void setReady(boolean b) {
        this.ready = b;
    }

    public int getxBackup() { return xBackup; }

    public int getyBackup() {
        return yBackup;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) { this.xPos = xPos; }

    public int getyPos() { return yPos; }

    public void setyPos(int yPos) { this.yPos = yPos; }

    public int getOrientation() {
        return orientation % 4;
    }

    public void setOrientation(int orientation) {
        this.orientation = Math.abs(orientation)%4;
    }

    public int getMaxHealth() { return MAXHEALTH; }

    public int getHealth() { return health; }

    public int getLifePoints() { return this.lifePoints; }

    public int getObjective() {
        return this.objective;
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }

    public void setAnnouncer() {
        this.announcePowerDown = !this.announcePowerDown;
    }

    public void setHand(Deck deck) {
        hand = new Hand(this, deck);
    }

    public List<Card> getSelected(){
        return hand.getSelected();
    }

    public ArrayList<Card> getLocked(){
        return hand.getLocked();
    }

    public boolean toggleReady() {
        this.ready = !this.ready;
        return this.ready;
    }

    public void newBackup() {
        this.xBackup = this.xPos;
        this.yBackup = this.yPos;
    }

    public void turn(int change){
        setOrientation((getOrientation() + change) % 4);
    }

    public void addHealth(int health) {
        setHealth(this.health+health);
    }

    public boolean isAlive() { return getLifePoints()>0; }

    public void resetPos() {
        setxPos(getxBackup());
        setyPos(getyBackup());
    }

    public void checkObjective(int ob) {
        if (ob==this.objective) {
            newBackup();
            this.objective += 1;
        }
    }

    public TextureRegion[][] setPlayerTextures (String texture) {
        Texture playerTexture = new Texture(texture);
        return TextureRegion.split(playerTexture, 300, 300);
    }

    public void respawn() {
        if (getHealth() <= 0 && isAlive()){
            removeLifePoint();
            this.health = MAXHEALTH;
        }
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
    }

    /**
     * Subtracts one life point from the player and sets the position to the backup.
     */
    private void removeLifePoint() {
        this.lifePoints -= 1;
        if (isAlive()) resetPos();
    }

    public void invinicible() {
        this.lifePoints = 3;
        this.setHealth(6);
    }
}
