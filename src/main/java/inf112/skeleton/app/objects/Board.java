package inf112.skeleton.app.objects;

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

    public int objectives;

    private Player[] players;

    private Deck deck;

    private final Sound damageSound;
    private final Sound laserSound;

    public int phase = 0;

    /**
     * The Board Constructor creates all the objects on the board based on the Options selected at the menu
     */
    public Board() {
        map = new TmxMapLoader().load(Menu.Options.mapFile);
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
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                if (hasTile(laserLayer, x, y) && isLaserShooter(laserLayer, x, y)) {
                    int[] coords = new int[2];
                    coords[0] = x;
                    coords[1] = y;
                    lasers.add(coords);
                }
            }
        }
    }

    /**
     * Creates a deck and stores it in the deck field, then shuffles the deck
     */
    private void createDeck() {
        try {
            this.deck = new Deck(Menu.Options.deckFile);
            deck.shuffle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a certain amount of players based on options selected at the menu and store them in the players field
     */
    private void generatePlayers() {
        players = new Player[Menu.Options.nrPlayers];
        for (int i = 0; i < players.length; i++) {
            if (i < Menu.Options.humanPlayers) {
                players[i] = new Player("Player " + (i + 1), i, 0, 0, i + 1);
            } else {
                players[i] = new AI("AI " + (i + 1), i, 0, 0, i + 1);
            }
            TextureRegion[][] tr = players[i].setPlayerTextures(Menu.Options.playerModelFile);
            players[i].setPlayerState(new PlayerState(players[i], this, tr));
            TextureRegion[][] hb = players[i].setPlayerTextures("assets/pictures/healthbars2.png");
            players[i].setHealthBars(new PlayerState(players[i], this, hb));
            players[i].setHand(deck);
        }
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
        for (int i = 0; i < flagLayer.getWidth(); i++) {
            for (int j = 0; j < flagLayer.getHeight(); j++) {
                if (hasTile(flagLayer, i, j)) {
                    objectives++;
                }
            }
        }
    }

    /**
     * Tries to move a player in a direction, without changing orientation
     *
     * @param player    to move
     * @param direction to go
     */
    private boolean move(Player player, int direction) {
        int x = player.getxPos(), y = player.getyPos();
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
                } else if (x >= boardWidth-1 || isHole) {
                    player.addHealth(-player.getMaxHealth());
                    return false;
                } else {
                    player.setxPos(x + 1);
                }
                break;
        }
        return true;
    }

    /**
     * Recursive function that moves player forward.
     * @param player to move
     * @param move how many spaces to move from current position
     */
    public void doMove(Player player, int move) {
        doMove(player, move, true); }

    public void doMove(Player player, int move, boolean legalMove) {
        //This is easier to modify, in order to make it work with cards

        //Gets the orientation from the player, in order to check which direction they should move
        int orientation = player.getOrientation();

        if(move > 0 || move == -1) {
            if (move == -1) {
                backwardMove(player);
            } else if (legalMove) {
                legalMove = move(player, orientation);
            }
            doMove(player, move - 1, legalMove);
        }
    }

    /**
     * Moves the player backwards without changing orientation
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
    private boolean hasTile(TiledMapTileLayer layer, int x, int y) {
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

            if (p.announcepowerdown) {
                if (p.playerPower) {
                    p.addHealth(1);
                    p.announcepowerdown = false;
                }
                p.lockRegister(); //In case damage is taken
                p.playerPower = !p.playerPower;
            } else {
                p.lockRegister();
            }

            p.discardDraw(deck);
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
        if (phase%2 == pushRound(pushLayer, x, y)){
            int direction = pushDirection(pushLayer,x,y);
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
                neighbour[0]--;
                break;
            case (2):
                neighbour[1]--;
                break;
            case (3):
                neighbour[0]++;
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
        }
        return false;
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
            damage = damage || laser(x, y, laserDirection(laserLayer, x, y));
        }
        for (Player p : players) {
            if (!p.playerPower && !isBlocked(p.getxPos(), p.getyPos(), p.getOrientation())) {
                int[] nb = getNeighbour(p.getxPos(), p.getyPos(), p.getOrientation());
                damage = damage || laser(nb[0], nb[1], p.getOrientation());
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
