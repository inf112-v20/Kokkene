package inf112.skeleton.app;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class PlayerState  {

    private Board board;
    private Player player;

    private Cell playerNorm;
    private Cell playerDead;
    private Cell playerWon;

    PlayerState(Player player, Board board, TextureRegion[][] tr) {
        this.player = player;
        this.board = board;

        playerNorm = new Cell();
        playerNorm.setTile(new StaticTiledMapTile(tr[0][0]));

        playerDead = new Cell();
        playerDead.setTile(new StaticTiledMapTile(tr[0][1]));

        playerWon = new Cell();
        playerWon.setTile(new StaticTiledMapTile(tr[0][2]));
    }

    /**
     * Sets player status based on where it is on the board
     * @return the player status
     */
    public Cell getPlayerStatus() {

        if(!player.isAlive()) {
            return playerDead;
        }
        else if (board.flagLayer.getCell(player.getxPos(), player.getyPos()) != null) {
            return playerWon;
        }

        return playerNorm;
    }

}
