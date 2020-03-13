package inf112.skeleton.app;

import inf112.skeleton.app.player.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PlayerTests {

    private Player player;
    private int xPos = 1;
    private int yPos = 1;
    private int upValue = 0;
    int maxHealth = 10,
            maxLifePoints = 3;

    @Before
    public void makePlayer() {
        player = new Player("player", xPos, yPos, upValue,false);
    }

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
        int damage = -1;
        player.addHealth(damage);
        assertEquals(maxHealth + damage, player.getHealth());
    }

    @Test
    public void playerLoseOneLifePointWhenTakingTenDamage() {
        int damage = -10;
        player.addHealth(damage);
        assertEquals(maxLifePoints-1, player.getLifePoints());
    }

    @Test
    public void playerIsDeadAfterLosingTenHealthThreeTimes() {
        int damage = -10;
        player.addHealth(damage); player.addHealth(damage); player.addHealth(damage);
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
