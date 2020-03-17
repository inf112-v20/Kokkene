package inf112.skeleton.app;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Tile {

    private enum Tiles {
        //Pistons
        PUSHDOWN2(1,0,2), PUSHLEFT2(2,0,1),
        PUSHUP2(3,0,0), PUSHRIGHT2(4,0,3),
        PUSHDOWN1(8,1,2), PUSHLEFT1(9,1,1),
        PUSHUP1(10,1,0), PUSHRIGHT1(11,1,3),

        //Wrench
        WRENCH2(7,2,0), WRENCH1(14,1,0),

        //Double conveyors
        CONUP2(12,2,0), CONRIGHT2(13,2,3),
        CONDOWN2(19,2,2), CONLEFT2(20,2,1),
        CONDOWN21(15,2, 2), CONLEFT21(16,2,1),
        CONRIGHT21(17,2,3),CONDOWN22(18,2,2),
        CONRIGHT22(22,2,3),CONUP21(23,2,0),
        CONUP22(24,2,0),CONLEFT22(25,2,1),
        CONUP23(64,2,0),CONRIGHT23(65,2,3),
        CONDOWN23(66,2,2),CONLEFT23(67,2,1),
        CONUP24(68,2,0),CONRIGHT24(69,2,3),
        CONRIGHT25(71,2,3),CONDOWN25(72,2,2),
        CONLEFT25(73,2,1),CONUP25(74,2,0),
        CONLEFT24(75,2,1),CONDOWN24(76,2,2),

        //Single conveyors
        CONDOWN11(29,1,2),CONLEFT11(30,1,1),
        CONRIGHT11(31,1,3),CONDOWN12(32,1,2),
        CONRIGHT12(36,1,3),CONUP11(37,1,0),
        CONUP12(38,1,0),CONLEFT12(39,1,1),
        CONUP1(43,1,0), CONDOWN1(44,1,2),
        CONLEFT1(45,1,1), CONRIGHT1(46,1,3),
        CONUP13(50,1,0),CONRIGHT13(51,1,3),
        CONDOWN13(52,1,2),CONLEFT13(53,1,1),
        CONRIGHT14(54,1,3),CONDOWN14(55,1,2),
        CONUP15(57,1,0),CONRIGHT15(58,1,3),
        CONDOWN15(59,1,2),CONLEFT15(60,1,1),
        CONUP14(61,1,0),CONLEFT14(62,1,1),

        //Walls
        WALLRIGHT(21,0,3), WALLDOWN(26,0,2),
        WALLLEFT(27,0,1), WALLUP(28,0,0),

        //Lasers
        LASERUP1(33,1,0),LASERRIGHT1(34,1,3),
        LASERDOWN1(40,1,2),LASERLEFT1(41,1,1),
        LASERH1(35,1,1),LASERV1(42,1,0),
        LASERUP2(77,2,0),LASERRIGHT2(82,2,3),
        LASERDOWN2(83,2,2),LASERLEFT2(84,2,1),
        LASERH2(91,2,1),LASERV2(90,2,0),

        //Rotating gears
        GEARLEFT(47,1,0), GEARRIGHT(48,-1,0),

        //Objectives
        FLAG1(49,1,0), FLAG2(56,2,0),
        FLAG3(63,3,0), FLAG4(70,4,0);

        public final int num;
        public final int val;
        public final int dir;

        Tiles(final int num, final int val, final int dir) {
            this.num = num;
            this.val = val;
            this.dir = dir;
        }
    }

    public Tiles getTile(int num) {
        for (Tiles t: Tiles.values()) {
            if (num == t.num)
                return t;
        }
        return null;
    }

    /**
     * @return amount of damage
     */
    public int laserValue(TiledMapTileLayer laserLayer, int x, int y) {
        return getTile(laserLayer.getCell(x, y).getTile().getId()).val;
    }

    /**
     * @return values 0-3 (0=North)
     */
    public int laserDirection(TiledMapTileLayer laserLayer, int x, int y) {
        return getTile(laserLayer.getCell(x, y).getTile().getId()).dir;
    }

    public boolean isLaserShooter(TiledMapTileLayer laserLayer, int x, int y) {
        switch (getTile(laserLayer.getCell(x, y).getTile().getId())) {
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
        return getTile(wallLayer.getCell(x, y).getTile().getId()).dir;
    }

    /**
     * @return value of wrench
     */
    public int wrenchValue(TiledMapTileLayer wrenchLayer, int x, int y) {
        return getTile(wrenchLayer.getCell(x, y).getTile().getId()).val;
    }

    /**
     * @return either 1 or 2
     */
    public int pushRound(TiledMapTileLayer pushLayer, int x, int y) {
        return getTile(pushLayer.getCell(x, y).getTile().getId()).val;
    }

    /**
     * @return direction values (0 = North)
     */
    public int pushDirection(TiledMapTileLayer pushLayer, int x, int y) {
        return getTile(pushLayer.getCell(x, y).getTile().getId()).dir;
    }

    /**
     * @return amount to move
     */
    public int conveyorValue(TiledMapTileLayer conveyorLayer, int x, int y) {
        return getTile(conveyorLayer.getCell(x, y).getTile().getId()).val;
    }

    /**
     * @return direction of conveyor
     */
    public int conveyorDirection(TiledMapTileLayer conveyorLayer, int x, int y) {
        return getTile(conveyorLayer.getCell(x, y).getTile().getId()).dir;
    }

    /**
     * @param conveyorLayer
     * @param x of new position
     * @param y of new position
     * @param lastDirection direction of the last conveyor
     * @return true if player will turn
     */
    public boolean conveyorWillTurn(TiledMapTileLayer conveyorLayer, int x, int y, int lastDirection) {
        switch (getTile(conveyorLayer.getCell(x, y).getTile().getId())) {
            case CONDOWN21:
            case CONUP22:
            case CONDOWN11:
            case CONUP12:
            case CONDOWN13:
            case CONUP15:
            case CONDOWN23:
            case CONUP24:
                if (lastDirection==1)
                    return true;
                break;
            case CONLEFT21:
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
            case CONDOWN22:
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
            case CONRIGHT22:
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
            case CONRIGHT14:
            case CONRIGHT25:
            case CONLEFT14:
            case CONLEFT25:
                if (lastDirection==0 || lastDirection==2)
                    return true;
                break;
            case CONUP14:
            case CONUP25:
            case CONDOWN14:
            case CONDOWN25:
                if (lastDirection==1 || lastDirection==3)
                    return true;
                break;
        }
        return false;
    }

    /**
     * @return -1 or 1
     */
    public int gearDirection(TiledMapTileLayer gearLayer, int x, int y) {
        return getTile(gearLayer.getCell(x, y).getTile().getId()).val;
    }

    /**
     * @return number of the flag
     */
    public int flagValue(TiledMapTileLayer flagLayer, int x, int y) {
        return getTile(flagLayer.getCell(x, y).getTile().getId()).val;
    }
}
