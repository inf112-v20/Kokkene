package inf112.skeleton.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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

    //Position on the board
    private int xPos, yPos;

    //Direction the robot is facing (north=0, east=1, south=2, west=3)
    private int orientation;

    //Health of the robot
    private int health, lifePoints;

    //The robot is alive if lifePoints is above 0
    private boolean alive;

    //Which tile holds the backup of this robot
    private int xBackup, yBackup;

    //The next objective the Player has to go to to score points.
    private int objective = 1;

    TextureRegion[][] tr;

    /**
     *
     * @param name  the name for this robot.
     * @param xPos  starting x-position for this robot.
     * @param yPos  starting y-position for this robot.
     * @param orientation  orientation (direction) in a 1-4 scale.
     */
    public Player(String name, int xPos, int yPos, int orientation) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.orientation = orientation;
        this.xBackup = xPos;
        this.yBackup = yPos;
        this.health = 10;
        this.lifePoints = 3;
        this.alive = true;
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
        this.orientation = Math.abs(orientation);
    }

    /**
     * Turns the robot
     * @param change positive numbers are to the right, negative turns to the left
     */
    public void turn(int change){
        setOrientation((getOrientation() + change + 4) % 4);
    }

    /**
     * Gets neighbour for the player to move to
     * @return Array of coordinates for the neighbour in the direction the player is facing
     */
    public int[] getNeighbour(){
        return getNeighbour(getOrientation());
    }

    /**
     * Finds neighbour of current player in given direction
     * @param direction to check neighbour
     * @return Array of x- and y-coordinate for the neighbour in the given direction
     */
    public int[] getNeighbour(int direction){
        return getNeighbour(direction, this.xPos, this.yPos);
    }

    /**
     * Gets neighbour in given direction from position given by x and y
     * @param direction to check neighbour
     * @param x coordinate to check neighbour of
     * @param y coordinate to check neighbour of
     * @return Array of x- and y-coordinate of the neighbour in the given direction
     */
    public int[] getNeighbour(int direction, int x, int y){
        int[] neighbour = new int[]{x, y};
        switch (direction) {
            case (0):
                neighbour[1]++;
                break;
            case (1):
                neighbour[0]++;
                break;
            case (2):
                neighbour[1]--;
                break;
            case (3):
                neighbour[0]--;
                break;
        }
        return neighbour;
    }


    /**
     *
     * @return current health total.
     */
    public int getHealth() { return health; }

    /**
     *
     * @param health  The value that health will be set to.
     */
    public void setHealth(int health) {
        if (0 <= health){
            this.health = Math.min(health, 10);
        }
    }

    /**
     * Changes the lifePoints
     * @param life is the amount of lifepoints lost/earned.
     *             (-1 = 1 less life point)
     */
    private void addLifePoints(int life) { this.lifePoints += life; }

    /**
     * @return the amount of lifePoints
     */
    int getLifePoints() { return this.lifePoints; }

    /**
     * @return true if robot is alive
     */
    public boolean isAlive() { return getLifePoints()<1; }

    public void resetPos() {
        setxPos(xBackup);
        setyPos(yBackup);
    }

    /**
     * @return the next objective to stand at to get points
     */
    public int getObjective() {
        return this.objective;
    }

    public void setObjective(int ob) {
        this.objective = ob;
    }

    /**
     * @param texture for the sprite of a player
     * @return true if robot is alive
     */
    public TextureRegion[][] setPlayerTextures (String texture) {
        Texture playerTexture = new Texture(texture);
        return  tr = TextureRegion.split(playerTexture, 300, 300);
    }

}
