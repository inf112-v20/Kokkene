package inf112.skeleton.app.actor;

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

public class Player implements IActor {

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

    //Direction the robot is facing (north=0, west=1, south=2, east=3)
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

    public boolean playerPower = false;

    //Used to lock cards, if damaged while powered down
    private ArrayList<Card> backupHand = new ArrayList<>();

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

    public boolean toggleReady() {
        this.ready = !this.ready;
        return this.ready;
    }

    public void newBackup() {
        this.xBackup = this.xPos;
        this.yBackup = this.yPos;
    }

    public void turn(int change){
        change += getOrientation();

        while (change<0) change +=4;
        //change will never be less than 0;
        setOrientation(change % 4);
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
    //all these hand methods could maybe be moved to a seperate "hand" class

    public int cardsToSelect(){
        return Math.min(5, getHealth() - 1);
    }

    public int lockedRegisters(){
        return Math.max(0, 6-getHealth());
    }

    public void lockRegister(){
        if (locked.size() < lockedRegisters()) {
            if(playerPower) {
                locked.add(backupHand.get(backupHand.size() - 1));
                backupHand.remove(backupHand.size() - 1);
            }
            else {
                locked.add(selected.get(selected.size() - 1));
                selected.remove(selected.size() - 1);
            }
        } else if (locked.size() > lockedRegisters()) {
            selected.add(locked.get(locked.size() - 1));
            locked.remove(locked.size() - 1);
        }
        if (locked.size() != lockedRegisters()) {
            lockRegister(); //TODO get rid of infinite loop?
        }
        assert locked.size() == lockedRegisters() : "Unexpected value: " + locked.size();
    }

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
            backupHand.add(playerHand[i]);
        }
        Arrays.sort(playerHand);
        this.cards = playerHand;
    }

    public Card[] getCards() {
        return this.cards;
    }

    public void toggleCard(Card c) {
        if (selected.contains(c)) {
            selected.remove(c);
            return;
        }
        selected.add(c);
    }

    public List<Card> getSelected(){
        return selected;
    }

    public ArrayList<Card> getLocked(){
        return locked;
    }

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

        if (this.health <= 0) {
            setyPos(200 + id);
        }
    }

    /**
     * Subtracts one life point from the player
     */
    private void removeLifePoint() {
        this.lifePoints -= 1;
        if (isAlive()) resetPos();
    }
}
