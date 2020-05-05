package inf112.skeleton.app.util;

public enum Tiles {
    //id = the tile number from tiles.png
    //Dir = direction of the tile

    //Pistons
    //val = 0 for even phase / 1 for odd phase
    PUSHDOWN2(1, 0, 2, Tiles.Group.PISTONS), PUSHLEFT2(2, 0, 1, Tiles.Group.PISTONS),
    PUSHUP2(3, 0, 0, Tiles.Group.PISTONS), PUSHRIGHT2(4, 0, 3, Tiles.Group.PISTONS),
    PUSHDOWN1(8, 1, 2, Tiles.Group.PISTONS), PUSHLEFT1(9, 1, 1, Tiles.Group.PISTONS),
    PUSHUP1(10, 1, 0, Tiles.Group.PISTONS), PUSHRIGHT1(11, 1, 3, Tiles.Group.PISTONS),

    //Wrench
    //Val = 2 for double wrench / 1 for single wrench
    WRENCH2(7, 2, 0, Tiles.Group.WRENCHES), WRENCH1(14, 1, 0, Tiles.Group.WRENCHES),

    //Double conveyors
    //Val = 2 because they have 2 arrows
    CONUP2(12, 2, 0, Tiles.Group.DOUBLE_CONVEYORS), CONRIGHT2(13, 2, 3, Tiles.Group.DOUBLE_CONVEYORS),
    CONDOWN2(19, 2, 2, Tiles.Group.DOUBLE_CONVEYORS), CONLEFT2(20, 2, 1, Tiles.Group.DOUBLE_CONVEYORS),
    CONDOWN21(15, 2, 2, Tiles.Group.DOUBLE_CONVEYORS), CONLEFT21(16, 2, 1, Tiles.Group.DOUBLE_CONVEYORS),
    CONRIGHT21(17, 2, 3, Tiles.Group.DOUBLE_CONVEYORS), CONDOWN22(18, 2, 2, Tiles.Group.DOUBLE_CONVEYORS),
    CONRIGHT22(22, 2, 3, Tiles.Group.DOUBLE_CONVEYORS), CONUP21(23, 2, 0, Tiles.Group.DOUBLE_CONVEYORS),
    CONUP22(24, 2, 0, Tiles.Group.DOUBLE_CONVEYORS), CONLEFT22(25, 2, 1, Tiles.Group.DOUBLE_CONVEYORS),
    CONUP23(64, 2, 0, Tiles.Group.DOUBLE_CONVEYORS), CONRIGHT23(65, 2, 3, Tiles.Group.DOUBLE_CONVEYORS),
    CONDOWN23(66, 2, 2, Tiles.Group.DOUBLE_CONVEYORS), CONLEFT23(67, 2, 1, Tiles.Group.DOUBLE_CONVEYORS),
    CONUP24(68, 2, 0, Tiles.Group.DOUBLE_CONVEYORS), CONRIGHT24(69, 2, 3, Tiles.Group.DOUBLE_CONVEYORS),
    CONRIGHT25(71, 2, 3, Tiles.Group.DOUBLE_CONVEYORS), CONDOWN25(72, 2, 2, Tiles.Group.DOUBLE_CONVEYORS),
    CONLEFT25(73, 2, 1, Tiles.Group.DOUBLE_CONVEYORS), CONUP25(74, 2, 0, Tiles.Group.DOUBLE_CONVEYORS),
    CONLEFT24(75, 2, 1, Tiles.Group.DOUBLE_CONVEYORS), CONDOWN24(76, 2, 2, Tiles.Group.DOUBLE_CONVEYORS),

    //Single conveyors
    //Val = 1 because they have 1 arrow
    CONDOWN11(29, 1, 2, Tiles.Group.SINGLE_CONVEYORS), CONLEFT11(30, 1, 1, Tiles.Group.SINGLE_CONVEYORS),
    CONRIGHT11(31, 1, 3, Tiles.Group.SINGLE_CONVEYORS), CONDOWN12(32, 1, 2, Tiles.Group.SINGLE_CONVEYORS),
    CONRIGHT12(36, 1, 3, Tiles.Group.SINGLE_CONVEYORS), CONUP11(37, 1, 0, Tiles.Group.SINGLE_CONVEYORS),
    CONUP12(38, 1, 0, Tiles.Group.SINGLE_CONVEYORS), CONLEFT12(39, 1, 1, Tiles.Group.SINGLE_CONVEYORS),
    CONUP1(43, 1, 0, Tiles.Group.SINGLE_CONVEYORS), CONDOWN1(44, 1, 2, Tiles.Group.SINGLE_CONVEYORS),
    CONLEFT1(45, 1, 1, Tiles.Group.SINGLE_CONVEYORS), CONRIGHT1(46, 1, 3, Tiles.Group.SINGLE_CONVEYORS),
    CONUP13(50, 1, 0, Tiles.Group.SINGLE_CONVEYORS), CONRIGHT13(51, 1, 3, Tiles.Group.SINGLE_CONVEYORS),
    CONDOWN13(52, 1, 2, Tiles.Group.SINGLE_CONVEYORS), CONLEFT13(53, 1, 1, Tiles.Group.SINGLE_CONVEYORS),
    CONRIGHT14(54, 1, 3, Tiles.Group.SINGLE_CONVEYORS), CONDOWN14(55, 1, 2, Tiles.Group.SINGLE_CONVEYORS),
    CONUP15(57, 1, 0, Tiles.Group.SINGLE_CONVEYORS), CONRIGHT15(58, 1, 3, Tiles.Group.SINGLE_CONVEYORS),
    CONDOWN15(59, 1, 2, Tiles.Group.SINGLE_CONVEYORS), CONLEFT15(60, 1, 1, Tiles.Group.SINGLE_CONVEYORS),
    CONUP14(61, 1, 0, Tiles.Group.SINGLE_CONVEYORS), CONLEFT14(62, 1, 1, Tiles.Group.SINGLE_CONVEYORS),

    //Walls
    //Dir = which side is blocked
    WALLRIGHT(21, 0, 3, Tiles.Group.WALLS), WALLDOWN(26, 0, 2, Tiles.Group.WALLS),
    WALLLEFT(27, 0, 1, Tiles.Group.WALLS), WALLUP(28, 0, 0, Tiles.Group.WALLS),

    //Lasers
    //Val = amount to damage
    //Dir = which direction does the laser go (lasers without shooters has two directions)
    LASERUP1(33, 1, 0, Tiles.Group.LASERS), LASERRIGHT1(34, 1, 3, Tiles.Group.LASERS),
    LASERDOWN1(40, 1, 2, Tiles.Group.LASERS), LASERLEFT1(41, 1, 1, Tiles.Group.LASERS),
    LASERH1(35, 1, 1, Tiles.Group.LASERS), LASERV1(42, 1, 0, Tiles.Group.LASERS),
    LASERUP2(77, 2, 0, Tiles.Group.LASERS), LASERRIGHT2(82, 2, 3, Tiles.Group.LASERS),
    LASERDOWN2(83, 2, 2, Tiles.Group.LASERS), LASERLEFT2(84, 2, 1, Tiles.Group.LASERS),
    LASERH2(91, 2, 1, Tiles.Group.LASERS), LASERV2(90, 2, 0, Tiles.Group.LASERS),

    //Rotating gears
    //Val = which direction to rotate
    GEARLEFT(47, 1, 0, Tiles.Group.GEARS), GEARRIGHT(48, 3, 0, Tiles.Group.GEARS),

    //Objectives
    //Val = flag number
    FLAG1(49, 1, 0, Tiles.Group.OBJECTIVES), FLAG2(56, 2, 0, Tiles.Group.OBJECTIVES),
    FLAG3(63, 3, 0, Tiles.Group.OBJECTIVES), FLAG4(70, 4, 0, Tiles.Group.OBJECTIVES),

    //sPAWNS
    START(78, 0, 0, Tiles.Group.SPAWNS);

    public final int id;
    public final int val;
    public final int dir;
    public final Tiles.Group group;

    Tiles(final int id, final int val, final int dir, final Tiles.Group group) {
        this.id = id;
        this.val = val;
        this.dir = dir;
        this.group = group;
    }

    public boolean inGroup(Tiles.Group group) {
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
