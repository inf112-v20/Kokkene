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
     * @return -1 or 1
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

    public enum Tiles {
        //id = the tile number from tiles.png
        //Dir = direction of the tile

        //Pistons
        //val = 0 for even phase / 1 for odd phase
        PUSHDOWN2(1, 0, 2, Group.PISTONS), PUSHLEFT2(2, 0, 1, Group.PISTONS),
        PUSHUP2(3, 0, 0, Group.PISTONS), PUSHRIGHT2(4, 0, 3, Group.PISTONS),
        PUSHDOWN1(8, 1, 2, Group.PISTONS), PUSHLEFT1(9, 1, 1, Group.PISTONS),
        PUSHUP1(10, 1, 0, Group.PISTONS), PUSHRIGHT1(11, 1, 3, Group.PISTONS),

        //Wrench
        //Val = 2 for double wrench / 1 for single wrench
        WRENCH2(7, 2, 0, Group.WRENCHES), WRENCH1(14, 1, 0, Group.WRENCHES),

        //Double conveyors
        //Val = 2 because they have 2 arrows
        CONUP2(12, 2, 0, Group.DOUBLE_CONVEYORS), CONRIGHT2(13, 2, 3, Group.DOUBLE_CONVEYORS),
        CONDOWN2(19, 2, 2, Group.DOUBLE_CONVEYORS), CONLEFT2(20, 2, 1, Group.DOUBLE_CONVEYORS),
        CONDOWN21(15, 2, 2, Group.DOUBLE_CONVEYORS), CONLEFT21(16, 2, 1, Group.DOUBLE_CONVEYORS),
        CONRIGHT21(17, 2, 3, Group.DOUBLE_CONVEYORS), CONDOWN22(18, 2, 2, Group.DOUBLE_CONVEYORS),
        CONRIGHT22(22, 2, 3, Group.DOUBLE_CONVEYORS), CONUP21(23, 2, 0, Group.DOUBLE_CONVEYORS),
        CONUP22(24, 2, 0, Group.DOUBLE_CONVEYORS), CONLEFT22(25, 2, 1, Group.DOUBLE_CONVEYORS),
        CONUP23(64, 2, 0, Group.DOUBLE_CONVEYORS), CONRIGHT23(65, 2, 3, Group.DOUBLE_CONVEYORS),
        CONDOWN23(66, 2, 2, Group.DOUBLE_CONVEYORS), CONLEFT23(67, 2, 1, Group.DOUBLE_CONVEYORS),
        CONUP24(68, 2, 0, Group.DOUBLE_CONVEYORS), CONRIGHT24(69, 2, 3, Group.DOUBLE_CONVEYORS),
        CONRIGHT25(71, 2, 3, Group.DOUBLE_CONVEYORS), CONDOWN25(72, 2, 2, Group.DOUBLE_CONVEYORS),
        CONLEFT25(73, 2, 1, Group.DOUBLE_CONVEYORS), CONUP25(74, 2, 0, Group.DOUBLE_CONVEYORS),
        CONLEFT24(75, 2, 1, Group.DOUBLE_CONVEYORS), CONDOWN24(76, 2, 2, Group.DOUBLE_CONVEYORS),

        //Single conveyors
        //Val = 1 because they have 1 arrow
        CONDOWN11(29, 1, 2, Group.SINGLE_CONVEYORS), CONLEFT11(30, 1, 1, Group.SINGLE_CONVEYORS),
        CONRIGHT11(31, 1, 3, Group.SINGLE_CONVEYORS), CONDOWN12(32, 1, 2, Group.SINGLE_CONVEYORS),
        CONRIGHT12(36, 1, 3, Group.SINGLE_CONVEYORS), CONUP11(37, 1, 0, Group.SINGLE_CONVEYORS),
        CONUP12(38, 1, 0, Group.SINGLE_CONVEYORS), CONLEFT12(39, 1, 1, Group.SINGLE_CONVEYORS),
        CONUP1(43, 1, 0, Group.SINGLE_CONVEYORS), CONDOWN1(44, 1, 2, Group.SINGLE_CONVEYORS),
        CONLEFT1(45, 1, 1, Group.SINGLE_CONVEYORS), CONRIGHT1(46, 1, 3, Group.SINGLE_CONVEYORS),
        CONUP13(50, 1, 0, Group.SINGLE_CONVEYORS), CONRIGHT13(51, 1, 3, Group.SINGLE_CONVEYORS),
        CONDOWN13(52, 1, 2, Group.SINGLE_CONVEYORS), CONLEFT13(53, 1, 1, Group.SINGLE_CONVEYORS),
        CONRIGHT14(54, 1, 3, Group.SINGLE_CONVEYORS), CONDOWN14(55, 1, 2, Group.SINGLE_CONVEYORS),
        CONUP15(57, 1, 0, Group.SINGLE_CONVEYORS), CONRIGHT15(58, 1, 3, Group.SINGLE_CONVEYORS),
        CONDOWN15(59, 1, 2, Group.SINGLE_CONVEYORS), CONLEFT15(60, 1, 1, Group.SINGLE_CONVEYORS),
        CONUP14(61, 1, 0, Group.SINGLE_CONVEYORS), CONLEFT14(62, 1, 1, Group.SINGLE_CONVEYORS),

        //Walls
        //Dir = which side is blocked
        WALLRIGHT(21, 0, 3, Group.WALLS), WALLDOWN(26, 0, 2, Group.WALLS),
        WALLLEFT(27, 0, 1, Group.WALLS), WALLUP(28, 0, 0, Group.WALLS),

        //Lasers
        //Val = amount to damage
        //Dir = which direction does the laser go (lasers without shooters has two directions)
        LASERUP1(33, 1, 0, Group.LASERS), LASERRIGHT1(34, 1, 3, Group.LASERS),
        LASERDOWN1(40, 1, 2, Group.LASERS), LASERLEFT1(41, 1, 1, Group.LASERS),
        LASERH1(35, 1, 1, Group.LASERS), LASERV1(42, 1, 0, Group.LASERS),
        LASERUP2(77, 2, 0, Group.LASERS), LASERRIGHT2(82, 2, 3, Group.LASERS),
        LASERDOWN2(83, 2, 2, Group.LASERS), LASERLEFT2(84, 2, 1, Group.LASERS),
        LASERH2(91, 2, 1, Group.LASERS), LASERV2(90, 2, 0, Group.LASERS),

        //Rotating gears
        //Val = which direction to rotate
        GEARLEFT(47, 1, 0, Group.GEARS), GEARRIGHT(48, 3, 0, Group.GEARS),

        //Objectives
        //Val = flag number
        FLAG1(49, 1, 0, Group.OBJECTIVES), FLAG2(56, 2, 0, Group.OBJECTIVES),
        FLAG3(63, 3, 0, Group.OBJECTIVES), FLAG4(70, 4, 0, Group.OBJECTIVES),

        //sPAWNS
        START(78, 0, 0, Group.SPAWNS);

        public final int id;
        public final int val;
        public final int dir;
        public final Group group;

        Tiles(final int id, final int val, final int dir, final Group group) {
            this.id = id;
            this.val = val;
            this.dir = dir;
            this.group = group;
        }

        public boolean inGroup(Group group) {
            return this.group == group;
        }

        public enum Group {
            PISTONS,
            WRENCHES,
            DOUBLE_CONVEYORS,
            SINGLE_CONVEYORS,
            WALLS,
            LASERS,
            GEARS,
            OBJECTIVES,
            SPAWNS
        }

    }
}
