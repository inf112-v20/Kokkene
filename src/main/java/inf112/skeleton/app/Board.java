package inf112.skeleton.app;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Board {

    TiledMap map;
    TiledMapTileLayer boardLayer, playerLayer, holeLayer, flagLayer,
            wallLayer, laserLayer, pushLayer, wrenchLayer, conveyorLayer, gearLayer;

    int boardHeight, boardWidth;

    Player[] players;
    Player currentPlayer;

    /**
     *
     * @param mapFile the file containing the map.
     */
    public Board(String mapFile, int nrPlayers) {
        map = new TmxMapLoader().load(mapFile);
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

        players = new Player[nrPlayers];
        for (int i = 0; i < nrPlayers; i++){
            players[i] = new Player("Player " + i, 1, i, 0);

        }

    }

    /**
     * Moves player in a direction
     * @param player to move
     */
    public void move(Player player) {
        //This is easier to modify, in order to make it work with cards
        TiledMapTileLayer.Cell getHole = holeLayer.getCell(player.getxPos(), player.getyPos());
        //Gets the orientation from the player, in order to check which direction they should move
        int orientation = player.getOrientation();

        switch(orientation) {
            //North
            case(0):
                if(isBlocked(0, player)) {
                    System.out.println("You cannot move in this direction");
                }
                else if (player.getyPos() >= boardHeight - 1 || getHole != null) {
                    player.resetPos();
                }
                else {
                    player.setyPos(player.getyPos() + 1);
                }
                break;
            //East
            case(1):
                if(isBlocked(1, player)){
                    System.out.println("You cannot move in this direction");
                }
                else if (player.getxPos() >= boardWidth - 1 || getHole != null) {
                    player.resetPos();
                }
                else {
                    player.setxPos(player.getxPos() + 1);
                }
                break;
            //South
            case(2):
                if (isBlocked(2, player)){
                    System.out.println("You cannot move in this direction");
                }
                else if (player.getyPos() <= 0 || getHole != null) {
                    player.resetPos();
                }
                else {
                    player.setyPos(player.getyPos() - 1);
                }
                break;
            //West
            case(3):
                if(isBlocked(3, player)){
                    System.out.println("You cannot move in this direction");
                }
                else if (player.getxPos() <= 0 || getHole != null) {
                    player.resetPos();
                }
                else {
                    player.setxPos(player.getxPos() - 1);
                }
                break;
        }
    }

    /**
     * Gets neighbour of the current player
     * @return Array of coordinates in the direction the current player is facing
     */
    public int[] getNeighbour(){
        return getNeighbour(currentPlayer);
    }

    /**
     * Gets neighbour for the player to move to
     * @return Array of coordinates for the neighbour in the direction the player is facing
     */
    public int[] getNeighbour(Player pl){
        return getNeighbour(pl, pl.getOrientation());
    }

    /**
     * Finds neighbour of current player in given direction
     * @param direction to check neighbour
     * @return Array of x- and y-coordinate for the neighbour in the given direction
     */
    public int[] getNeighbour(Player pl, int direction){
        return getNeighbour(direction, pl.getxPos(), pl.getyPos());
    }

    /**
     * Gets neighbour in given direction from position given by x and y
     * @param direction to check neighbour
     * @param x coordinate to check neighbour of
     * @param y coordinate to check neighbour of
     * @return Array of x- and y-coordinate of the neighbour in the given direction
     */
    public int[] getNeighbour(int direction, int x, int y){
        int[] neighbour = new int[]{x, y};
        switch (direction) {
            case (0):
                neighbour[1]++;
                break;
            case (1):
                neighbour[0]++;
                break;
            case (2):
                neighbour[1]--;
                break;
            case (3):
                neighbour[0]--;
                break;
        }
        return neighbour;
    }

    private boolean isBlocked(int x, int y, int dir){
        // TODO
        return true;
    }

    /**
     * Checks if you can move to given direction
     * @param direction to check
     * @param player who check direction for
     * @return True if we can move in given direction, false otherwise
     */
    private boolean isBlocked(int direction, Player player) {

        int[] coordinates = getNeighbour(player, direction);
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
