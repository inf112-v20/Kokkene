package inf112.skeleton.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for the board
 */

public class BoardTest {

    /**
     * Initialize the board, and check if its values are as expected
     */
    int nrPlayers = 0;
    Board board;
    Player player;

    @Before
    public void makeBoard() {
        player = new Player("Player", 1, 1, 0, false);
        board = new Board("assets/12by12DizzyDash.tmx", nrPlayers);
    }

    /**
     * TODO
     * Checks if a player dies if it walks into a hole
     */
    @Test
    public void playerDiesIfInHole() {
        assertTrue(true);
    }

    /**
     * Check if the correct amount of players are on the board
     */

    @Test
    public void correctAmountOfPlayersOnBoard() {
        assertEquals(board.players.length, nrPlayers);
    }

    /**
     * Since we dont have the ability to add boards to eachother,
     * all the boards will have the necessity of being quadratic 1x1
     * Width should be equal to height
     */

    @Test
    public void widthEqualsHeight() {
        assertEquals(board.boardWidth, board.boardHeight);
    }

    /**
     * Checks to see if the player is out of bounds on the x-axis
     */

    @Test
    public void outOfBoundsXPos() {
        assertTrue(board.players[0].getxPos() >= 0 && board.players[0].getxPos() <= board.boardWidth);
    }

    /**
     * Checks to see if the player is out of bounds on the y-axis
     */

    @Test
    public void outOfBoundsYPos() {
        assertTrue(board.players[0].getyPos() >= 0 && board.players[0].getyPos() <= board.boardHeight);
    }
}
