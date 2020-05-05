package inf112.skeleton.app.actor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.gameelements.Board;

public class PlayerState {

    private final Board board;
    private final Player player;

    private final Cell playerNorm;
    private final Cell playerDead;
    private final Cell playerWon;

    private final Cell life1;
    private final Cell life2;
    private final Cell life3;

    public PlayerState(Player player, Board board, TextureRegion[][] tr) {
        this.player = player;
        this.board = board;
        int id = player.getId();

        playerNorm = new Cell();
        playerNorm.setTile(new StaticTiledMapTile(tr[0][0]));
        playerNorm.getTile().setId(id);

        playerDead = new Cell();
        playerDead.setTile(new StaticTiledMapTile(tr[0][1]));
        playerDead.getTile().setId(id);

        playerWon = new Cell();
        playerWon.setTile(new StaticTiledMapTile(tr[0][2]));
        playerWon.getTile().setId(id);

        life1 = playerNorm; //setting readable names for health bars
        life2 = playerDead;
        life3 = playerWon;
    }

    /**
     * Sets player status based on where it is on the board
     * @return the player status
     */
    public Cell getPlayerStatus() {
        if (!player.isAlive()) {
            return playerDead;
        } else if (board.objectives.size() == player.getObjective() - 1) {
            return playerWon;
        }
        return playerNorm;
    }

    /**
     * @return player health bar cell for board
     */
    public Cell getPlayerHealthBar() {
        switch (player.getLifePoints()) {
            case (3):
                return life3; //3 health
            case (2):
                return life2; //2 health
            case (1):
                return life1; //1 health
            default:
                return null; // no health bar
        }
    }
}
