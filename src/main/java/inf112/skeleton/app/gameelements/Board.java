package inf112.skeleton.app.gameelements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import inf112.skeleton.app.actor.AI;
import inf112.skeleton.app.actor.Player;
import inf112.skeleton.app.actor.PlayerState;
import inf112.skeleton.app.game.Menu;
import inf112.skeleton.app.sound.Sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Board extends Tile {

    public TiledMap map;
    public TiledMapTileLayer boardLayer,
            playerLayer,
            holeLayer,
            flagLayer,
            wallLayer,
            laserLayer,
            pushLayer,
            wrenchLayer,
            conveyorLayer,
            gearLayer,
            healthLayer;

    private ArrayList<int[]> lasers;

    public int boardHeight,
            boardWidth;

    public ArrayList<int[]> objectives;

    private Player[] players;

    private Deck deck;

    private final Sound damageSound;
    private final Sound laserSound;

    public int phase = 0;

    /**
     * The Board Constructor creates all the objects on the board based on the Options selected at the menu
     */
    public Board() {
        buildMap();

        findLasers();

        createDeck();

        generatePlayers();

        generateObjectives();

        damageSound = new Sound("assets/sound/oof_sound.mp3");
        laserSound = new Sound("assets/sound/laserfire01.ogg");
    }


    /**
     * Separates the map into different layers and stores the layers as fields
     */
    private void buildMap() {
        map = new TmxMapLoader().load(Menu.OptionsUtil.mapFile);

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
        healthLayer = (TiledMapTileLayer) map.getLayers().get("Healthbar");

        boardHeight = boardLayer.getHeight();
        boardWidth = boardLayer.getWidth();
    }

    /**
     * Finds all the lasers on the map and stores them in the lasers field
     */
    private void findLasers() {
        lasers = new ArrayList<>();
        for (int[] xy : Tile.findGroupMembers(laserLayer, Tile.Tiles.Group.LASERS)) {
            if (isLaserShooter(laserLayer, xy[0], xy[1])) {
                lasers.add(xy);
            }
        }
    }

    /**
     * Creates a deck and stores it in the deck field, then shuffles the deck
     */
    private void createDeck() {
        try {
            this.deck = new Deck(Menu.OptionsUtil.deckFile);
            deck.shuffle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a certain amount of players based on options selected at the menu and store them in the players field
     */
    private void generatePlayers() {
        players = new Player[Menu.OptionsUtil.nrPlayers];
        ArrayList<int[]> spawns = Menu.OptionsUtil.spawns.get(Menu.OptionsUtil.mapFile);
        for (int i = 0; i < players.length; i++) {
            int[] xy = spawns.get(i); // x- and y-coordinate to spawn
            if (i < Menu.OptionsUtil.humanPlayers) {
                players[i] = new Player("Player " + (i + 1), xy[0], xy[1], towardCenter(xy[0], xy[1]), i + 1);
            } else {
                players[i] = new AI("AI " + (1 + i - Menu.OptionsUtil.humanPlayers), // always i >= humanPlayers here
                        xy[0], xy[1], towardCenter(xy[0], xy[1]), i + 1);
            }
            createPlayerTextures(players[i]);
            players[i].setHand(deck); //Deals the hand to the players
        }
    }

    /**
     * Creates and sets textures to the given player
     *
     * @param p player to set the textures of
     */
    private void createPlayerTextures(Player p) {
        TextureRegion[][] tr = p.setPlayerTextures(Menu.OptionsUtil.playerModelFile);
        p.setPlayerState(new PlayerState(p, this, tr));
        TextureRegion[][] hb = p.setPlayerTextures("assets/pictures/healthbars2.png");
        p.setHealthBars(new PlayerState(p, this, hb));
    }

    /**
     * Returns the direction it is furthest to the center of the map on that axis
     *
     * @param x x-coordinate to check distance to the center of the map from
     * @param y y-coordinate to check distance to the center of the map from
     * @return Direction pointing inwards, away from the edges of the map
     */
    private int towardCenter(int x, int y) { //TODO unnecessary when we add direction value to the spawn tiles
        ArrayList<Integer> dirs = new ArrayList<>(Arrays.asList(1, 3, 0, 2));
        int centerX = boardWidth / 2,
                centerY = boardHeight / 2;
        if (y < centerY) {
            dirs.remove(3); // indices are guaranteed as we're working forwards from the back of the list
        } else {
            dirs.remove(2);
        }
        if (x >= centerX) {
            dirs.remove(1);
        } else {
            dirs.remove(0);
        }
        return Math.abs(y - centerY) < Math.abs(x - centerX) ? dirs.get(0) : dirs.get(1); // returns left/right if true
    }

    /**
     * Gets the array of players on the board
     *
     * @return A Player Array containing all the players on the board
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Generates objectives from the map
     */
    private void generateObjectives() {
        objectives = Tile.findGroupMembers(flagLayer, Tiles.Group.OBJECTIVES);
    }

    /**
     * Tries to move a player in a direction, without changing orientation
     *
     * @param player    to move
     * @param direction to go
     */
    private boolean move(Player player, int direction) {
        int x = player.getxPos(),
                y = player.getyPos();
        boolean isHole = hasTile(holeLayer, x, y);

        switch (direction) {
            //North
            case (0):
                if (isBlocked(player, 0)) {
                    return false;
                } else if (y >= boardHeight-1 || isHole ) {
                    player.addHealth(-player.getMaxHealth());
                    return false;
                } else {
                    player.setyPos(y + 1);
                }
                break;
            //West
            case (1):
                if (isBlocked(player, 1)) {
                    return false;
                } else if (x <= 0 || isHole) {
                    player.addHealth(-player.getMaxHealth());
                    return false;
                } else {
                    player.setxPos(x - 1);
                }
                break;
            //South
            case (2):
                if (isBlocked(player, 2)) {
                    return false;
                } else if (y <= 0 || isHole) {
                    player.addHealth(-player.getMaxHealth());
                    return false;
                } else {
                    player.setyPos(y - 1);
                }
                break;
            //East
            case (3):
                if (isBlocked(player, 3)) {
                    return false;
                } else if (x >= boardWidth - 1 || isHole) {
                    player.addHealth(-player.getMaxHealth());
                    return false;
                } else {
                    player.setxPos(x + 1);
                }
                break;
            default:
                throw new IllegalArgumentException("Direction: " + direction + ", is not a valid direction. ");
        }
        return true;
    }

    /**
     * Recursive function that moves player forward.
     *
     * @param player to move
     * @param move   how many spaces to move from current position
     */
    public void doMove(Player player, int move) {
        doMove(player, move, true);
    }

    /**
     * Gets neighbour in given direction from position given by x and y
     *
     * @param direction to check neighbour
     * @param x         coordinate to check neighbour of
     * @param y         coordinate to check neighbour of
     * @return Array of x- and y-coordinate of the neighbour in the given direction
     */
    public static int[] getNeighbour(int x, int y, int direction) {
        int[] neighbour = new int[]{x, y};
        switch (direction) {
            case (0):
                neighbour[1]++;
                break;
            case (1):
                neighbour[0]--;
                break;
            case (2):
                neighbour[1]--;
                break;
            case (3):
                neighbour[0]++;
                break;
            default:
                throw new IllegalArgumentException("Direction is not 0-3, but: " + direction);
        }
        return neighbour;
    }

    /**
     * Moves the player backwards without changing orientation
     *
     * @param player to move backwards
     */
    public void backwardMove(Player player) {
        int orientation = (player.getOrientation() + 2) % 4;
        move(player, orientation);
    }

    /**
     * Modify given player as the cards instructs
     * @param card instructions for the player
     * @param pl player to move
     */
    public void cardMove(Card card, Player pl) {
        int type = card.getType();
        switch(type){
            //Forward
            case (0):
            case (1):
                doMove(pl, card.getMove());
                break;
            //Turn
            case (2):
                pl.turn(card.getMove());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    /**
     * @return true if the tile is not null
     */
    public static boolean hasTile(TiledMapTileLayer layer, int x, int y) {
        return (layer.getCell(x, y) != null);
    }

    public void afterArrowMove(Player player) {
        afterPhase(player,1);
        player.respawn();
    }

    /**
     * Checks the position of every player at the end of turn for interaction with the board objects
     */
    public void afterRound() {
        for (Player p : players) {
            if (!p.isAlive()) {
                continue;
            }
            p.setReady(false);
            playerLayer.setCell(p.getxPos(), p.getyPos(), null);
            healthLayer.setCell(p.getxPos(), p.getyPos(), null);
            p.respawn();
            playerLayer.setCell(p.getxPos(), p.getyPos(), p.getPlayerState().getPlayerStatus());
            healthLayer.setCell(p.getxPos(), p.getyPos(), p.getHealthBars().getPlayerHealth());

            if (p.announcePowerDown) {
                if (p.playerPower) {
                    p.addHealth(1);
                    p.announcePowerDown = false;
                }
                p.hand.lockRegister(); //In case damage is taken
                p.playerPower = !p.playerPower;
            } else {
                p.hand.lockRegister();
            }

            p.hand.discardDraw(deck);
        }

        this.phase = 0;
    }

    /**
     * Sorts the cards of all the players in the given phase
     *
     * @param phase which phase we're currently in
     * @return sorted list of cards of all the players in ascending priority
     */
    public ArrayList<Card> sortPhase(int phase) {
        ArrayList<Card> cardList = new ArrayList<>();
        for (Player p : players) {
            if (p.getHealth() <= 0 || !p.isAlive() || p.playerPower) {
                continue;
            } else if (p.getSelected().size() <= phase) {
                int lastLocked = p.getLocked().size() - 1,
                        difference = phase - p.getSelected().size(),
                        reverseOrder = lastLocked - difference;

                cardList.add(p.getLocked().get(reverseOrder));
                continue;
            }
            cardList.add(p.getSelected().get(phase));
        }
        Collections.sort(cardList);

        return cardList;
    }

    /**
     * Makes all the players interact with the board objects
     */
    public void afterPhase() {
        this.phase++;
        for (Player p : players) {
            afterPhase(p, this.phase);
        }
        setPlayersOnBoard();
        fireLasers();
        nullPlayerBoard();
        for (Player finish : players) {
            finishPhase(finish);
        }
    }

    /**
     * Makes the given player interact with all board objects
     * @param player to interact
     * @param phase to do
     */
    private void afterPhase(Player player, int phase){
        int x = player.getxPos(),
                y = player.getyPos();

        if (hasTile(conveyorLayer, x, y)) {
            moveDoubleConveyor(player, x, y);
        }
        x = player.getxPos(); y = player.getyPos();
        //Must update x and y because player may change position.
        if (hasTile(conveyorLayer, x, y)){
            moveConveyor(player, x, y);
        }
        x = player.getxPos(); y = player.getyPos();
        if (hasTile(pushLayer, x, y)){
            doPush(player, phase, x, y);
        }
        x = player.getxPos(); y = player.getyPos();
        if (hasTile(gearLayer, x, y)){
            player.turn(gearDirection(gearLayer, x, y));
        }
    }

    /**
     * Does a push
     * @param player to push
     * @param phase to do
     * @param x position
     * @param y position
     */
    private void doPush(Player player, int phase, int x, int y) {
        if (phase % 2 == pushPhases(pushLayer, x, y)) {
            int direction = pushDirection(pushLayer, x, y);
            move(player, direction);
        }
    }

    /**
     * Moves a player if he is on a double conveyor
     * @param player player to move
     * @param x position of player
     * @param y position of player
     */
    private void moveDoubleConveyor(Player player, int x, int y){
        if (conveyorValue(conveyorLayer, x, y) == 2) {
            int direction = conveyorDirection(conveyorLayer, x, y);
            move(player, direction);
            doConveyorTurn(player, direction);
        }
    }

    /**
     * Moves a player who is on a conveyor
     * @param player player to move
     * @param x position of player
     * @param y position of player
     */
    private void moveConveyor(Player player, int x, int y){
        int direction = conveyorDirection(conveyorLayer, x, y);
        move(player, direction);
        doConveyorTurn(player, direction);
    }

    /**
     * Turns a player according to the conveyors
     * @param player that will turn
     * @param lastDirection from the last conveyor
     */
    private void doConveyorTurn(Player player, int lastDirection) {
        int x = player.getxPos(),
                y = player.getyPos();
        if (hasTile(conveyorLayer, x, y)
                && (conveyorWillTurn(conveyorLayer, x, y, lastDirection))) {
            player.turn(conveyorDirection(conveyorLayer, x, y)-lastDirection);
        }
    }

    /**
     * Checks if the player is on a wrench or flag, and updates the damage, backup and current objective.
     *
     * @param player to be checked.
     */
    private void finishPhase(Player player) {
        int x = player.getxPos(),
                y = player.getyPos();
        if (hasTile(wrenchLayer, x, y)) {
            player.newBackup();
            player.addHealth(wrenchValue(wrenchLayer, x, y));
        }
        if (hasTile(flagLayer, x, y)) {
            player.checkObjective(flagValue(flagLayer, x, y));
        }
    }

    public void doMove(Player player, int move, boolean legalMove) {

        //Gets the orientation from the player, in order to check which direction they should move
        int orientation = player.getOrientation();

        if (move > 0 || move == -1) {
            if (move == -1) {
                backwardMove(player);
            } else if (legalMove) {
                doMove(player, move - 1, move(player, orientation));
            }
        }
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
        int wallThis = 9,
                wallNext = 9;
        if (hasTile(wallLayer, x, y)) {
            wallThis = wallSide(wallLayer, x, y);
        }
        if (hasTile(wallLayer, nb[0], nb[1])) {
            wallNext = wallSide(wallLayer, nb[0], nb[1]);
        }
        switch (dir) {
            case 0:
                return wallThis == 0 || wallNext == 2;
            case 1:
                return wallThis == 1 || wallNext == 3;
            case 2:
                return wallThis == 2 || wallNext == 0;
            case 3:
                return wallThis == 3 || wallNext == 1;
            default:
                throw new IllegalArgumentException("Direction should be 0-3, actually: " + dir);
        }
    }

    /**
     * Checks if given direction is blocked for given player
     *
     * @param direction to move in
     * @param player    to be moved
     * @return true if the path is blocked
     */
    private boolean isBlocked(Player player, int direction) {
        if (isBlocked(player.getxPos(), player.getyPos(), direction)) {
            return true;
        }
        else {
            int[] nb = getNeighbour(player.getxPos(), player.getyPos(), direction);
            setPlayersOnBoard();
            if (hasTile(playerLayer, nb[0], nb[1])) {
                Player otherPlayer = players[playerLayer.getCell(nb[0],nb[1]).getTile().getId()-1];
                nullPlayerBoard();
                return !move(otherPlayer, direction); //tries to move next player.
            }
            nullPlayerBoard();
            return false;
        }
    }

    /**
     * Iterate through all lasers on the map and fire them. Plays the damage sound if anyone takes damage.
     */
    private void fireLasers() {
        boolean damage = false;
        for (int[] coords : lasers) {
            int x = coords[0],
                    y = coords[1];
            damage = laser(x, y, laserDirection(laserLayer, x, y)) || damage;
        }
        for (Player p : players) {
            if (p.getHealth() > 0 && !p.playerPower && !isBlocked(p.getxPos(), p.getyPos(), p.getOrientation())) {
                int[] nb = getNeighbour(p.getxPos(), p.getyPos(), p.getOrientation());
                damage = laser(nb[0], nb[1], p.getOrientation()) || damage;
            }
        }
        laserSound.play();
        if (damage) {
            damageSound.play();
        }
    }

    /**
     * Checks the direction the laser at given coords is pointing until it hits a player or wall
     *
     * @param x   coordinate of laser
     * @param y   coordinate of laser
     * @param dir direction laser is pointing
     * @return true when the laser has hit a player, false if it hit a wall
     */
    private boolean laser(int x, int y, int dir) {
        if (hasTile(playerLayer, x, y)) { //If there is a player there
            int damage = -1;
            if (hasTile(laserLayer, x, y)) {
                damage = -laserValue(laserLayer, x, y);
            }
            players[playerLayer.getCell(x, y).getTile().getId() - 1].addHealth(damage);
            return true;
        } else if (!isBlocked(x, y, dir) && 0 <= x && x <= boardWidth
                && 0 <= y && y <= boardHeight) { //Continues if it isn't blocked or outside map
            int[] nb = getNeighbour(x, y, dir);
            return laser(nb[0], nb[1], dir);
        }
        return false;
    }

    /**
     * Set all cells on playerLayer to null, thus emptying the board of players
     */
    public void nullPlayerBoard() {
        for (int x = 0; x < playerLayer.getWidth(); x++) {
            for (int y = 0; y < playerLayer.getHeight(); y++) {
                playerLayer.setCell(x, y, null);
                healthLayer.setCell(x, y, null);
            }
        }
    }

    /**
     * Place all the players on the board in the correct positions
     */
    public void setPlayersOnBoard() {
        for (Player p : players) {
            if (p.isAlive() && p.getHealth() > 0) {
                int x = p.getxPos(),
                        y = p.getyPos();
                playerLayer.setCell(x, y, p.getPlayerState().getPlayerStatus());
                playerLayer.getCell(x, y).setRotation(p.getOrientation());
                healthLayer.setCell(x, y, p.getHealthBars().getPlayerHealth());
                healthLayer.getCell(x, y).setRotation(p.getOrientation());
            }
        }
    }

    /**
     * Toggles whether the sounds are muted or not
     */
    public void muteToggle() {
        damageSound.muteToggle();
        laserSound.muteToggle();
    }
}
