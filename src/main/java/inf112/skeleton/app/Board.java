package inf112.skeleton.app;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Board {

    TiledMap map;
    TiledMapTileLayer boardLayer, playerLayer, holeLayer, flagLayer,
            wallLayer, laserLayer, pushLayer, wrenchLayer, conveyorLayer, gearLayer;

    int boardHeight, boardWidth;

    /**
     *
     * @param mapFile the file containing the map.
     */
    public Board(String mapFile) {        map = new TmxMapLoader().load(mapFile);
        boardLayer = (TiledMapTileLayer) map.getLayers().get("Board");
        wallLayer = (TiledMapTileLayer) map.getLayers().get("Wall");
        playerLayer = (TiledMapTileLayer) map.getLayers().get("Player");
        holeLayer = (TiledMapTileLayer) map.getLayers().get("Hole");
        flagLayer = (TiledMapTileLayer) map.getLayers().get("Flag");
        laserLayer = (TiledMapTileLayer) map.getLayers().get("Laser");
        pushLayer = (TiledMapTileLayer) map.getLayers().get("Push");
        wrenchLayer = (TiledMapTileLayer) map.getLayers().get("Wrench");
        conveyorLayer = (TiledMapTileLayer) map.getLayers().get("Conveyor");
        gearLayer = (TiledMapTileLayer) map.getLayers().get("Gear");

        boardHeight = boardLayer.getHeight();
        boardWidth = boardLayer.getWidth();

    }

    /**
     * Moves player in a direction
     * @param player to move
     */
    public void move(Player player) {
        //This is easier to modify, in order to make it work with cards

        //Gets the orientation from the player, in order to check which direction they should move
        int orientation = player.getOrientation();

        switch(orientation) {
            //North
            case(0):
                if (player.getyPos() >= boardHeight - 1 || isBlocked(0, player)) {
                    System.out.println("You cannot move in this direction");
                } else {
                    player.setyPos(player.getyPos() + 1);
                }
                break;
            //East
            case(1):
                if (player.getxPos() >= boardWidth - 1 || isBlocked(1, player)) {
                    System.out.println("You cannot move in this direction");
                } else {
                    player.setxPos(player.getxPos() + 1);
                }
                break;
            //South
            case(2):
                if (player.getyPos() <= 0 || isBlocked(2, player)) {
                    System.out.println("You cannot move in this direction");
                } else {
                    player.setyPos(player.getyPos() - 1);
                }
                break;
            //West
            case(3):
                if (player.getxPos() <= 0 || isBlocked(3, player)) {
                    System.out.println("You cannot move in this direction");
                } else {
                    player.setxPos(player.getxPos() - 1);
                }
                break;
        }
    }

    /**
     * Checks if you can move to given direction
     * @param direction to check
     * @param player to check direction for
     * @return True if we can move in given direction, false otherwise
     */
    private boolean isBlocked(int direction, Player player) {

        int[] coordinates = player.getNeighbour(direction);
        int wallThis = 0, wallNext = 0, x = coordinates[0], y = coordinates[1];

        if (wallLayer.getCell(player.getxPos(), player.getyPos()) != null) {
            wallThis = wallLayer.getCell(player.getxPos(), player.getyPos()).getTile().getId();
        }
        if (wallLayer.getCell(x, y) != null) {
            wallNext = wallLayer.getCell(x, y).getTile().getId();
        }

        switch (direction) {
            case 0:
                return wallThis == 28 || wallNext == 26;
            case 1:
                return wallThis == 21 || wallNext == 27;
            case 2:
                return wallThis == 26 || wallNext == 28;
            case 3:
                return wallThis == 27 || wallNext == 21;
        }
        return true;
    }

}
