package inf112.skeleton.app;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PlayerTests {

    Player player;
    int xPos, yPos, orientation = 1;
    int maxHealth = 10, maxLifePoints = 3;

    @Before
    public void MakePlayer() {
        player = new Player("player", xPos, yPos, orientation,false);
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
        assertEquals(orientation, player.getOrientation());
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
    public void PlayerTakesOneDamageOnCall() {
        int damage = -1;
        player.addHealth(damage);
        assertEquals(player.getHealth(),maxHealth + damage);
    }

    @Test
    public void PlayerLoseOneLifePointWhenTakingTenDamage() {
        int damage = -10;
        player.addHealth(damage);
        assertEquals(player.getLifePoints(), maxLifePoints-1);
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
        assertEquals(player.getOrientation(),orientation+turnRight);
    }

    @Test
    public void PlayerCanTurnLeft() {
        int turnLeft = -1;
        player.turn(turnLeft);
        assertEquals(player.getOrientation(),orientation+turnLeft);
    }

    @Test
    public void PlayerObjectiveCanOnlyChangeToOneMore() {
        int initialObjective = 1, newObjective = 3; //Objective is initially 1
        player.setObjective(newObjective);
        assertEquals(initialObjective, player.getObjective());
    }
}
