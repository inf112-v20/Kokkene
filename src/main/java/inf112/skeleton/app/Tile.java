package inf112.skeleton.app;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.Objects;

public enum Tile {
    PUSHDOWN2(1), PUSHLEFT2(2), PUSHUP2(3), PUSHRIGHT2(4),
    PUSHDOWN1(8), PUSHLEFT1(9), PUSHUP1(10), PUSHRIGHT1(11),
    WRENCH2(7), WRENCH1(14),
    MOVEUP2(12), MOVERIGHT2(13), MOVEDOWN2(19), MOVELEFT2(20),
    MOVEUP1(43), MOVEDOWN1(44), MOVELEFT1(45), MOVERIGHT1(46),
    WALLRIGHT(21), WALLDOWN(26), WALLLEFT(27), WALLUP(28),
    GEARLEFT(47), GEARRIGHT(48),
    FLAG1(49), FLAG2(56), FLAG3(63), FLAG4(70);

    public final int num;

    Tile(final int num) {
        this.num = num;
    }

    private Tile getTile(int num) {
        for (Tile t: Tile.values()) {
            if (num == t.num)
                return t;
        }
        return null;
    }

    /**
     * @return values 0-3 (0=North)
     */
    public int wallSide(TiledMapTileLayer wallLayer, int x, int y) {
        switch (Objects.requireNonNull(getTile(wallLayer.getCell(x, y).getTile().getId()))) {
            case WALLUP:
                return 0;
            case WALLLEFT:
                return 1;
            case WALLDOWN:
                return 2;
            case WALLRIGHT:
                return 3;
        }
        return 9;
    }

    /**
     * @return 1 or 2
     */
    public int wrenchValue(TiledMapTileLayer wrenchLayer, int x, int y) {
        switch (Objects.requireNonNull(getTile(wrenchLayer.getCell(x, y).getTile().getId()))) {
            case WRENCH1:
                return 1;
            case WRENCH2:
                return 2;
        }
        return 9;
    }

    /**
     * @return string that says "even" or "odd"
     */
    public String pushRound(TiledMapTileLayer pushLayer, int x, int y) {
        switch (Objects.requireNonNull(getTile(pushLayer.getCell(x, y).getTile().getId()))) {
            case PUSHUP1:
            case PUSHDOWN1:
            case PUSHLEFT1:
            case PUSHRIGHT1:
                return "odd";
            case PUSHUP2:
            case PUSHDOWN2:
            case PUSHLEFT2:
            case PUSHRIGHT2:
                return "even";
        }
        return "none";
    }

    /**
     * @return direction values (0 = North)
     */
    public int pushDirection(TiledMapTileLayer pushLayer, int x, int y) {
        switch (Objects.requireNonNull(getTile(pushLayer.getCell(x, y).getTile().getId()))) {
            case PUSHRIGHT2:
            case PUSHRIGHT1:
                return 3;
            case PUSHDOWN2:
            case PUSHDOWN1:
                return 2;
            case PUSHLEFT2:
            case PUSHLEFT1:
                return 1;
            case PUSHUP2:
            case PUSHUP1:
                return 0;
        }
        return 9;
    }

    /**
     * @return amount to move
     */
    public int conveyorValue(TiledMapTileLayer conveyorLayer, int x, int y) {
        switch (Objects.requireNonNull(getTile(conveyorLayer.getCell(x, y).getTile().getId()))) {
            case MOVEUP2:
            case MOVEDOWN2:
            case MOVELEFT2:
            case MOVERIGHT2:
                return 2;
            case MOVEUP1:
            case MOVEDOWN1:
            case MOVELEFT1:
            case MOVERIGHT1:
                return 1;
        }
        return 9;
    }

    /**
     * @return -1 or 1
     */
    public int gearDirection(TiledMapTileLayer gearLayer, int x, int y) {
        switch (Objects.requireNonNull(getTile(gearLayer.getCell(x, y).getTile().getId()))) {
            case GEARLEFT:
                return 1;
            case GEARRIGHT:
                return -1;
        }
        return 9;
    }

    /**
     * @return number of the flag
     */
    public int flagValue(TiledMapTileLayer flagLayer, int x, int y) {
        switch (Objects.requireNonNull(getTile(flagLayer.getCell(x, y).getTile().getId()))) {
            case FLAG1:
                return 1;
            case FLAG2:
                return 2;
            case FLAG3:
                return 3;
            case FLAG4:
                return 4;
        }
        return 9;
    }

}
