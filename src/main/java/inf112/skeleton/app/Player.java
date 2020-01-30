package inf112.skeleton.app;

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

    //Position on the board
    private int xPos, yPos;

    //Direction the robot is facing (north=1, west=2, east=3, south=4)
    private int orientation;

    //Health of the robot
    private int health, lifePoints;

    //The robot is alive if lifePoints is above 0
    private boolean alive;

    //Which tile holds the backup of this robot
    private int xBackup, yBackup;

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
    public int getxBackup() {
        return xBackup;
    }

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
     * @return the current x Position for this robot.
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * Replace x position with a new one
     * @param xPos  new x position
     */
    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    /**
     * @return the current y position for this robot.
     */
    public int getyPos() {
        return yPos;
    }

    /**
     * @param yPos  the y-value this robot should have.
     */
    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    /**
     * @return current rotation in a 1-4 scale.
     */
    public int getOrientation() {
        if(orientation % 4 == 0) {
            return 4;
        }
        return orientation % 4;
    }

    /**
     * Sets the direction
     * @param orientation  orientation is the direction in a 1-4 scale.
     */
    public void setOrientation(int orientation) {
        this.orientation = Math.abs(orientation);
    }

    /**
     *
     * @return current health total.
     */
    public int getHealth() {
        return health;
    }

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
    private void addLifePoints(int life) {
        this.lifePoints += life;
    }

    /**
     * @return the amount of lifePoints
     */
    int getLifePoints() {
        return this.lifePoints;
    }

    /**
     *
     * @return true if robot is alive
     */
    public boolean isAlive() {
        return getLifePoints()<1;
    }
}
