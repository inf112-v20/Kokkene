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
            players[i] = new Player("Player " + (i + 1), 1, i, 0);
        }

    }

    /**
     * Moves player in a direction
     * @param player to move
     * @param move how many spaces to move from current position
     */
    public void forwardMove(Player player, int move) {
        //This is easier to modify, in order to make it work with cards
        TiledMapTileLayer.Cell getHole = holeLayer.getCell(player.getxPos(), player.getyPos());
        playerLayer.setCell(player.getxPos(), player.getyPos(), null);
        //Gets the orientation from the player, in order to check which direction they should move
        int orientation = player.getOrientation();

        if(move > 0) {
            switch (orientation) {
                //North
                case (0):
                    if (isBlocked(player, 0)) {
                        System.out.println("You cannot move in this direction");
                    } else if (player.getyPos() >= boardHeight || getHole != null) {
                        player.resetPos();
                    } else {
                        player.setyPos(player.getyPos() + 1);
                    }
                    break;
                //East
                case (1):
                    if (isBlocked(player, 1)) {
                        System.out.println("You cannot move in this direction");
                    } else if (player.getxPos() >= boardWidth || getHole != null) {
                        player.resetPos();
                    } else {
                        player.setxPos(player.getxPos() + 1);
                    }
                    break;
                //South
                case (2):
                    if (isBlocked(player, 2)) {
                        System.out.println("You cannot move in this direction");
                    } else if (player.getyPos() < 0 || getHole != null) {
                        player.resetPos();
                    } else {
                        player.setyPos(player.getyPos() - 1);
                    }
                    break;
                //West
                case (3):
                    if (isBlocked(player, 3)) {
                        System.out.println("You cannot move in this direction");
                    } else if (player.getxPos() < 0 || getHole != null) {
                        player.resetPos();
                    } else {
                        player.setxPos(player.getxPos() - 1);
                    }
                    break;
            }
            forwardMove(player, move-1);
        }
        else {
            //Checks afterturn after every move for now, to make sure it works.
            afterTurn(player);
        }
    }

    /*

    public void backwardMove(Player player, Card card) {

        TiledMapTileLayer.Cell getHole = holeLayer.getCell(player.getxPos(), player.getyPos());

        playerLayer.setCell(player.getxPos(), player.getyPos(), null);

        int orientation = player.getOrientation();

        switch (orientation) {
            //North
            case (0):
                if (isBlocked(player, 2)) {
                    System.out.println("You cannot move in this direction");
                } else if (player.getyPos() + card.getMove() <= 0 || getHole != null) {
                    player.resetPos();
                } else {
                    player.setyPos(player.getyPos() + card.getMove());
                }
                break;
            //East
            case (1):
                if (isBlocked(player, 3)) {
                    System.out.println("You cannot move in this direction");
                } else if (player.getxPos() + card.getMove() <= 0 || getHole != null) {
                    player.resetPos();
                } else {
                    player.setxPos(player.getxPos() + card.getMove());
                }
                break;
            //South
            case (2):
                if (isBlocked(player, 0)) {
                    System.out.println("You cannot move in this direction");
                }
                else if (player.getyPos() - card.getMove() > boardHeight || getHole != null) {
                    player.resetPos();
                }
                else {
                    player.setyPos(player.getyPos() - card.getMove());
                }
                break;
            //West
            case(3):
                if(isBlocked(player, 1)){
                    System.out.println("You cannot move in this direction");
                }
                else if (player.getxPos() - card.getMove() > boardWidth || getHole != null) {
                    player.resetPos();
                }
                else {
                    player.setxPos(player.getxPos() - card.getMove());
                }
                break;
        }
        afterMove(player);
    }

     */

    /**
     * Checking the rest of the layers after turn
     * @param player  the player to be affected.
     */
    private void afterTurn(Player player) {
        int x = player.getxPos(), y = player.getyPos();
        TiledMapTileLayer.Cell laser = laserLayer.getCell(x,y);

        if (laser != null) {
            player.addHealth(-1);
        }
        checkObjective(player);
    }

    /**
     * Checks if the player has reached the objective, and updates the current objective.
     * @param player to be checked.
     */
    private void checkObjective(Player player) {
        int x = player.getxPos(), y = player.getyPos();
        TiledMapTileLayer.Cell flag = flagLayer.getCell(x, y);
        if (flag != null) {
            switch (flag.getTile().getId()) {
                case 49:
                    player.setObjective(2);
                    if (player.getObjective() == 2)
                        player.setBackup(player.getxPos(),player.getyPos());
                    break;
                case 56:
                    player.setObjective(3);
                    if (player.getObjective() == 3)
                        player.setBackup(player.getxPos(),player.getyPos());
                    break;
                case 63:
                    player.setObjective(4);
                    if (player.getObjective() == 4)
                        player.setBackup(player.getxPos(),player.getyPos());
                    break;
                case 70: player.setObjective(5); break;//temporary solution
            }

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
     * Gets the neighbouring coordinates of currentPLayer in given direction
     * @param dir to find neighbour
     * @return Array of coordinates in given direction of the currentPlayer
     */
    public int[] getNeighbour(int dir){
        return getNeighbour(currentPlayer, dir);
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
    public int[] getNeighbour(int x, int y, int direction){
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

    /**
     * Checks if path is blocked in given direction
     * @param x coordinate to check from
     * @param y coordinate to check in given direction of
     * @param dir direction to check if it's blocked
     * @return true if given direction is blocked
     */
    private boolean isBlocked(int x, int y, int dir){

        int[] nb = getNeighbour(x, y, dir);

        //The following line should be removed before code being delivered.
        //if (playerLayer.getCell(nb[0], nb[1]) != null){ return isBlocked(nb[0], nb[1], dir); }

        int wallThis = 0, wallNext = 0;
        if (wallLayer.getCell(x, y) != null) {
            wallThis = wallLayer.getCell(x, y).getTile().getId();
        }
        if (wallLayer.getCell(nb[0], nb[1]) != null) {
            wallNext = wallLayer.getCell(nb[0], nb[1]).getTile().getId();
        }
        switch (dir) {
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

    /**
     * Checks if given direction is blocked for given player
     * @param direction to move in
     * @param player to be moved
     * @return true if the path is blocked
     */
    private boolean isBlocked(Player player, int direction) {
        return isBlocked(player.getxPos(), player.getyPos(), direction);
    }

    private boolean laser(int x, int y, int dir){
        if (playerLayer.getCell(x, y).getTile().getId() != 0){
            return players[playerLayer.getCell(x, y).getTile().getId()].takeDamage();
        }
        else if (!isBlocked(x, y, dir)){
            int[] nb = getNeighbour(x, y, dir);
            return laser(nb[0], nb[1], dir);
        }
        return true;
    }

}
