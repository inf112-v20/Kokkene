package inf112.skeleton.app;

import inf112.skeleton.app.actor.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTests {

    private Player player;

    private int xPos = 1;
    private int yPos = 1;
    private int upValue = 0;
    private int maxHealth = 10,
            maxLifePoints = 3;

    @Before
    public void makePlayer() {
        player = new Player("player", xPos, yPos, upValue, 1);
    }
    int damage = -10;

    @Test
    public void playerHasTenHealthAfterInitiation() {
        assertEquals(10, player.getHealth());
    }

    @Test
    public void playerHasThreeLivesAfterInitiation() {
        assertEquals(3, player.getLifePoints());
    }

    @Test
    public void playerHasOrientationOneAfterInitiation() {
        assertEquals(upValue, player.getOrientation());
    }

    @Test
    public void playerXPositionIsOneAfterInitiation() {
        assertEquals(player.getxPos(),xPos);
    }

    @Test
    public void playerYPositionIsOneAfterInitiation() {
        assertEquals(player.getyPos(),yPos);
    }

    @Test
    public void playerXBackupIsOneAfterMoving() {
        player.setxPos(7);
        assertEquals(xPos, player.getxBackup());
    }

    @Test
    public void playerYBackupIsOneAfterMoving() {
        player.setyPos(7);
        assertEquals(yPos, player.getyBackup());
    }

    @Test
    public void playerTakesOneDamageOnCall() {
        damage = -1;
        player.addHealth(damage);
        assertEquals(maxHealth + damage, player.getHealth());
    }

    @Test
    public void playerIsOffTheBoardWhileDead(){
        player.addHealth(damage);
        assertTrue(player.getyPos() > 200);
    }

    @Test
    public void playerLoseOneLifePointWhenRespawningWithNegativeHealth() {
        player.addHealth(damage); player.respawn();
        assertEquals(maxLifePoints-1, player.getLifePoints());
    }

    @Test
    public void playerWillRespawnAtBackupLocation(){
        player.setxPos(player.getxBackup()+1); //Moves away from backup location
        player.setyPos(player.getyBackup()+1);

        player.addHealth(damage);
        player.respawn();

        assertEquals(player.getxBackup(), player.getxPos());
        assertEquals(player.getyBackup(), player.getyPos());
    }

    @Test
    public void playerIsDeadAfterRespawningThreeTimesWithNegativeHealth() {

        for (int i = 0; i < 3; i++) {
            player.addHealth(damage);
            player.respawn();
        }
        assertFalse(player.isAlive());
    }

    @Test
    public void playerCanTurnRight() {
        int turnRight = -1;
        player.turn(turnRight);
        int rightValue = 3;
        assertEquals(rightValue, player.getOrientation());
    }

    @Test
    public void playerCanTurnLeft() {
        int turnLeft = 1;
        player.turn(turnLeft);
        int leftValue = 1;
        assertEquals(leftValue, player.getOrientation());
    }

    @Test
    public void playerCanTurn180degrees() {
        int turnAround = 2;
        player.turn(turnAround);
        int downValue = 2;
        assertEquals(downValue, player.getOrientation());
    }

    @Test
    public void playerObjectiveCanOnlyChangeToOneMore() {
        int initialObjective = 1,
                newObjective = 2; //Objective is initially 1
        player.checkObjective(newObjective);
        assertEquals(initialObjective, player.getObjective());
    }
}
