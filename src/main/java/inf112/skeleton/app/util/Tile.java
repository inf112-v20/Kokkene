package inf112.skeleton.app.util;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.gameelements.Board;
import java.util.ArrayList;
import java.util.Objects;

public class Tile {

    public static Tiles getTile(int id) {
        for (Tiles t : Tiles.values()) {
            if (id == t.id)
                return t;
        }
        return null;
    }

    /*
    public static boolean isStart(TiledMapTileLayer boardLayer, int x, int y) {
        return (getTile(boardLayer.getCell(x, y).getTile().getId()) == Tiles.START);
    }*/

    /**
     * Gets list of all ids in given group
     *
     * @param group Group containing the board elements you want the ids of
     * @return List containing all the ids
     */
    public static ArrayList<Integer> groupIDs(Tiles.Group group) {
        ArrayList<Integer> idList = new ArrayList<>();
        for (Tiles t : Tiles.values()) {
            if (t.inGroup(group)) {
                idList.add(t.id);
            }
        }
        return idList;
    }

    /**
     * Finds all members of the group on the given layer
     *
     * @param layer layer to check for members of the group
     * @param group Group to search for
     * @return List of the coordinates of every member of the group
     */
    public static ArrayList<int[]> findGroupMembers(TiledMapTileLayer layer, Tiles.Group group) {
        return scanLayer(layer, groupIDs(group));
    }

    /**
     * Gets the coordinates of all the ids in idList on the provided layer
     *
     * @param layer  layer to search
     * @param idList List of integer ids to match
     * @return Array of coordinates
     */
    public static ArrayList<int[]> scanLayer(TiledMapTileLayer layer, ArrayList<Integer> idList) {
        ArrayList<int[]> found = new ArrayList<>();
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                if (Board.hasTile(layer, x, y) && idList.contains(layer.getCell(x, y).getTile().getId())) {
                    int[] coords = new int[2];
                    coords[0] = x;
                    coords[1] = y;
                    found.add(coords);
                }
            }
        }
        return found;
    }

    /**
     * @return amount of damage
     */
    public int laserValue(TiledMapTileLayer laserLayer, int x, int y) {
        return Objects.requireNonNull(getTile(laserLayer.getCell(x, y).getTile().getId())).val;
    }

    /**
     * @return values 0-3 (0=North, 1=West)
     */
    public int laserDirection(TiledMapTileLayer laserLayer, int x, int y) {
        return Objects.requireNonNull(getTile(laserLayer.getCell(x, y).getTile().getId())).dir;
    }

    /**
     * Checks whether given tile is a laser emitter
     *
     * @param laserLayer layer of lasers to check
     * @param x          coordinate to check
     * @param y          coordinate to check
     * @return true if it's an emitter, false if laser light
     */
    public boolean isLaserShooter(TiledMapTileLayer laserLayer, int x, int y) {
        switch (Objects.requireNonNull(getTile(laserLayer.getCell(x, y).getTile().getId()))) {
            case LASERH1:
            case LASERH2:
            case LASERV1:
            case LASERV2:
                return false;
            default:
                return true;
        }
    }

    /**
     * @return direction of the wall
     */
    public int wallSide(TiledMapTileLayer wallLayer, int x, int y) {
        return Objects.requireNonNull(getTile(wallLayer.getCell(x, y).getTile().getId())).dir;
    }

    /**
     * @return value of wrench
     */
    public int wrenchValue(TiledMapTileLayer wrenchLayer, int x, int y) {
        return Objects.requireNonNull(getTile(wrenchLayer.getCell(x, y).getTile().getId())).val;
    }

    /**
     * @return direction values (0 = North)
     */
    public int pushDirection(TiledMapTileLayer pushLayer, int x, int y) {
        return Objects.requireNonNull(getTile(pushLayer.getCell(x, y).getTile().getId())).dir;
    }

    /**
     * @return amount to move
     */
    public int conveyorValue(TiledMapTileLayer conveyorLayer, int x, int y) {
        return Objects.requireNonNull(getTile(conveyorLayer.getCell(x, y).getTile().getId())).val;
    }

    /**
     * @return direction of conveyor
     */
    public int conveyorDirection(TiledMapTileLayer conveyorLayer, int x, int y) {
        return Objects.requireNonNull(getTile(conveyorLayer.getCell(x, y).getTile().getId())).dir;
    }

    /**
     * @param conveyorLayer conveyorLayer is the map of cells where conveyors are stored
     * @param x             of new position
     * @param y             of new position
     * @param lastDirection direction of the last conveyor
     * @return true if player will turn
     */
    public boolean conveyorWillTurn(TiledMapTileLayer conveyorLayer, int x, int y, int lastDirection) {
        switch (Objects.requireNonNull(getTile(conveyorLayer.getCell(x, y).getTile().getId()))) {
            case CONDOWN21: //all these cases turn if you come from the right
            case CONUP22:
            case CONDOWN11:
            case CONUP12:
            case CONDOWN13:
            case CONUP15:
            case CONDOWN23:
            case CONUP24:
                if (lastDirection == 1)
                    return true;
                break;
            case CONLEFT21: //all these cases turn if you come from the below
            case CONRIGHT21:
            case CONLEFT11:
            case CONRIGHT11:
            case CONLEFT13:
            case CONRIGHT15:
            case CONLEFT23:
            case CONRIGHT24:
                if (lastDirection==0)
                    return true;
                break;
            case CONDOWN22: //all these cases turn if you come from the left
            case CONUP21:
            case CONDOWN12:
            case CONUP11:
            case CONUP13:
            case CONDOWN15:
            case CONUP23:
            case CONDOWN24:
                 if (lastDirection==3)
                    return true;
                break;
            case CONRIGHT22: //all these cases turn if you come from above
            case CONLEFT22:
            case CONRIGHT12:
            case CONLEFT12:
            case CONRIGHT13:
            case CONLEFT15:
            case CONRIGHT23:
            case CONLEFT24:
                if (lastDirection==2)
                    return true;
                break;
            case CONRIGHT14: //all these cases turn if you come from above or below
            case CONRIGHT25:
            case CONLEFT14:
            case CONLEFT25:
                if (lastDirection==0 || lastDirection==2)
                    return true;
                break;
            case CONUP14: //all these cases turn if you come from the right or left
            case CONUP25:
            case CONDOWN14:
            case CONDOWN25:
                if (lastDirection==1 || lastDirection==3)
                    return true;
                break;
            default:
                return false;
        }
        return false;
    }

    /**
     * @return 3 (right) or 1 (left)
     */
    public int gearDirection(TiledMapTileLayer gearLayer, int x, int y) {
        return Objects.requireNonNull(getTile(gearLayer.getCell(x, y).getTile().getId())).val;
    }

    /**
     * @return number of the flag
     */
    public int flagValue(TiledMapTileLayer flagLayer, int x, int y) {
        return Objects.requireNonNull(getTile(flagLayer.getCell(x, y).getTile().getId())).val;
    }

    /**
     * @return either 0 or 1 for even or odd phases
     */
    public int pushPhases(TiledMapTileLayer pushLayer, int x, int y) {
        return Objects.requireNonNull(getTile(pushLayer.getCell(x, y).getTile().getId())).val;
    }
}
