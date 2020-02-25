package inf112.skeleton.app;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals(player.getHealth(), 10);
    }

    @Test
    public void PlayerHasThreeLivesAfterInitiation() {
        assertEquals(player.getLifePoints(),3);
    }

    @Test
    public void PlayerHasOrientationOneAfterInitiation() {
        assertEquals(player.getOrientation(), orientation);
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
}
