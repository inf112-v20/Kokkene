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
    private int health;

    //The robot is alive if health is above 0
    private boolean alive;

    //Which tile holds the backup of this robot
    private int xBackup, yBackup;

    public Player(String name, int xPos, int yPos, int orientation) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.orientation = orientation;
        this.xBackup = xPos;
        this.yBackup = yPos;
        this.health = 10;
        this.alive = true;
    }

    public void setBackup(int xBackup, int yBackup) {
        this.xBackup = xBackup;
        this.yBackup = yBackup;
    }

    public int getxBackup() {
        return xBackup;
    }

    public int getyBackup() {
        return yBackup;
    }

    public String getName() {
        return name;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    //If orientation is somehow bigger than 4, still returns in the interval [1-4]
    public int getOrientation() {
        if(orientation % 4 == 0) {
            return 4;
        }

        return orientation % 4;

    }

    //Forces positive number when a orientation is set
    public void setOrientation(int orientation) {
        this.orientation = Math.abs(orientation);
    }

    public int getHealth() {
        return health;
    }

    //Health can never be set above 10
    public void setHealth(int health) {
        if (0 < health){
            this.health = Math.min(health, 10);
        }
    }

    public boolean isAlive() {
        return getHealth()>0;
    }
}
