package inf112.skeleton.app;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PlayerTests {

    Player player;
    int xPos, yPos, orientation = 1;

    @Before
    public void MakePlayer() {
        player = new Player("player", xPos, yPos, orientation);
    }

    @Test
    public void PlayerHasTenHealthAfterInitiation() {
        assertEquals(player.getHealth(), 10);
    }

    @Test
    public void PlayerHasThreeLivesAfterInitiation() {
        assertEquals(player.getLifePoints(),3);
    }

    public void PlayerHasOrientationOneAfterInitiation() {
        assertEquals(player.getOrientation(), orientation);
    }
}
