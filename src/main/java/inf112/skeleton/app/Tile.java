package inf112.skeleton.app;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Tile {

    private enum Tiles {
        //Pistons
        PUSHDOWN2(1,2,2), PUSHLEFT2(2,2,1),
        PUSHUP2(3,2,0), PUSHRIGHT2(4,2,3),
        PUSHDOWN1(8,1,2), PUSHLEFT1(9,1,1),
        PUSHUP1(10,1,0), PUSHRIGHT1(11,1,3),

        //Wrench
        WRENCH2(7,1,0), WRENCH1(14,2,0),

        //Conveyors
        MOVEUP2(12,2,0), MOVERIGHT2(13,2,3),
        MOVEDOWN2(19,2,2), MOVELEFT2(20,2,1),
        MOVEUP1(43,1,0), MOVERIGHT1(46,1,3),
        MOVEDOWN1(44,1,2), MOVELEFT1(45,1,1),

        //Walls
        WALLRIGHT(21,0,3), WALLDOWN(26,0,2),
        WALLLEFT(27,0,1), WALLUP(28,0,0),

        //Rotating gears
        GEARLEFT(47,-1,0), GEARRIGHT(48,1,0),

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
     * @return values 0-3 (0=North)
     */
    public int wallSide(TiledMapTileLayer wallLayer, int x, int y) {
        return getTile(wallLayer.getCell(x, y).getTile().getId()).dir;
    }

    /**
     * @return 1 or 2
     */
    public int wrenchValue(TiledMapTileLayer wrenchLayer, int x, int y) {
        return getTile(wrenchLayer.getCell(x, y).getTile().getId()).val;
    }

    /**
     * @return string that says "even" or "odd"
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
