package inf112.skeleton.app;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PlayerTests {

    Player player;
    int xPos, yPos = 1;
    int upValue = 0, rightValue = 1, downValue = 2, leftValue = 3;
    int maxHealth = 10, maxLifePoints = 3;

    @Before
    public void MakePlayer() {
        player = new Player("player", xPos, yPos, upValue,false);
    }

    @Test
    public void PlayerHasTenHealthAfterInitiation() {
        assertEquals(10, player.getHealth());
    }

    @Test
    public void PlayerHasThreeLivesAfterInitiation() {
        assertEquals(3, player.getLifePoints());
    }

    @Test
    public void PlayerHasOrientationOneAfterInitiation() {
        assertEquals(upValue, player.getOrientation());
    }

    @Test
    public void PlayerXPositionIsOneAfterInitiation() {
        assertEquals(player.getxPos(),xPos);
    }

    @Test
    public void PlayerYPositionIsOneAfterInitiation() {
        assertEquals(player.getyPos(),yPos);
    }

    @Test
    public void PlayerXBackupIsOneAfterMoving() {
        player.setxPos(7);
        assertEquals(xPos, player.getxBackup());
    }

    @Test
    public void PlayerYBackupIsOneAfterMoving() {
        player.setyPos(7);
        assertEquals(yPos, player.getyBackup());
    }

    @Test
    public void PlayerTakesOneDamageOnCall() {
        int damage = -1;
        player.addHealth(damage);
        assertEquals(maxHealth + damage, player.getHealth());
    }

    @Test
    public void PlayerLoseOneLifePointWhenTakingTenDamage() {
        int damage = -10;
        player.addHealth(damage);
        assertEquals(maxLifePoints-1, player.getLifePoints());
    }

    @Test
    public void PlayerIsDeadAfterLosingTenHealthThreeTimes() {
        int damage = -10;
        player.addHealth(damage); player.addHealth(damage); player.addHealth(damage);
        assertFalse(player.isAlive());
    }

    @Test
    public void PlayerCanTurnRight() {
        int turnRight = 1;
        player.turn(turnRight);
        assertEquals(rightValue, player.getOrientation());
    }

    @Test
    public void PlayerCanTurnLeft() {
        int turnLeft = -1;
        player.turn(turnLeft);
        assertEquals(leftValue, player.getOrientation());
    }

    @Test
    public void PlayerCanTurn180degrees() {
        int turnAround = 2;
        player.turn(turnAround);
        assertEquals(downValue, player.getOrientation());
    }

    @Test
    public void PlayerObjectiveCanOnlyChangeToOneMore() {
        int initialObjective = 1, newObjective = 3; //Objective is initially 1
        player.setObjective(newObjective);
        assertEquals(initialObjective, player.getObjective());
    }
}
