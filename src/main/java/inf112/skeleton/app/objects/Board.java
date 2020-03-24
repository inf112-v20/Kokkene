package inf112.skeleton.app.objects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import inf112.skeleton.app.player.Player;
import inf112.skeleton.app.sound.Sound;

import java.io.IOException;
import java.util.Arrays;

public class Board extends Tile{

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

    public int boardHeight,
            boardWidth;

    public int objectives;

    public Player[] players;

    private Deck deck;

    private boolean soundBool = true;
    private Sound damageSound;

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
        healthLayer = (TiledMapTileLayer) map.getLayers().get("Healthbar");

        boardHeight = boardLayer.getHeight();
        boardWidth = boardLayer.getWidth();

        try {
            this.deck = new Deck();
            deck.shuffle();
        } catch (IOException e) {
            e.printStackTrace();
        }

        players = new Player[nrPlayers];
        for (int i = 0; i < nrPlayers; i++){
            players[i] = new Player("Player " + (i + 1), i, 0, 0, i+1);
            players[i].setHand(deck);
        }

        for(int i = 0; i < flagLayer.getWidth(); i++) {
            for(int j = 0; j < flagLayer.getHeight(); j++) {
                if (hasTile(flagLayer,i,j)) {
                    objectives++;
                }
            }
        }

        damageSound = new Sound("assets/sound/oof_sound.mp3");
    }

    /**
     * Tries to move a player in a direction, without changing orientation
     * @param player to move
     * @param direction to go
     */
    private boolean move(Player player, int direction) {
        int x = player.getxPos(), y = player.getyPos();
        boolean isHole = hasTile(holeLayer,x,y);
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
        int x = player.getxPos(),
                y = player.getyPos();
        playerLayer.setCell(x, y, null);
        healthLayer.setCell(x, y, null);
        //Gets the orientation from the player, in order to check which direction they should move
        int orientation = player.getOrientation();

        if(move > 0 || move == -1) {
            if(move == -1) {
                backwardMove(player);
            }
            else if (legalMove) {
                legalMove = move(player, orientation);
            }
            doMove(player, move-1, legalMove);
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
        int name = card.getName();
        switch(name){
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
                throw new IllegalStateException("Unexpected value: " + name);
        }
    }

    /**
     * @return true if the tile is not null
     */
    private boolean hasTile(TiledMapTileLayer layer, int x, int y) {
        return (layer.getCell(x, y) != null);
    }

    /**
     * Does the entire turn in the correct order
     */
    public void doTurn(){
            for (int i = 0; i < 5; i++) {
                for (Card c : sortPhase(i)) {
                    if (c == null || c.getOwner() == null || c.getOwner().getHealth() <= 0) {
                        continue;
                    }
                    cardMove(c, c.getOwner());
                }
                afterPhase(i + 1);
            }

        afterRound();
    }

    public void afterArrowMove(Player player) {
        afterPhase(player,1);
    }

    /**
     * Checks the position of every player at the end of turn for interaction with the board objects
     */
    private void afterRound(){
        for (Player p : players){
            if (!p.isAlive()){
                continue;
            }
            p.respawn();
            p.lockRegister();
            p.discardDraw(deck);
        }
    }

    /**
     * Plays the damage sound
     */
    private void playDamageSound() {
        if (soundBool)
            damageSound.play();
    }

    /**
     * Sorts the cards of all the players in the given phase
     * @param phase which phase we're currently in
     * @return sorted list of cards of all the players in ascending priority
     */
    //This caused a crash (Remove before hand-in)
    private Card[] sortPhase(int phase){
        Card[] cardArray = new Card[players.length];
        for (int i = 0; i < players.length; i++){
            Player p = players[i];
            if (p.getHealth() <= 0 || !p.isAlive()){
                continue;
            }
            if (p.getSelected().size() <= phase) {
                int lastLocked = p.getLocked().size() - 1,
                        difference = phase - p.getSelected().size(),
                        reverseOrder = lastLocked - difference;

                cardArray[i] = p.getLocked().get(reverseOrder);
                continue;
            }
            cardArray[i] = p.getSelected().get(phase);
        }
        Arrays.sort(cardArray);
        return cardArray;
    }


    /**
     * Makes all the players interact with the board objects
     * @param phase to do
     */
    private void afterPhase(int phase){
        for (Player p : players){
            afterPhase(p, phase);
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

        if (hasTile(conveyorLayer, x, y)){
            moveDoubleConveyor(player, x, y);
            //TODO render between each part of the phases, but only if player moved.
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
        x = player.getxPos(); y = player.getyPos();
        if (hasTile(laserLayer, x, y)) {
            player.addHealth(-laserValue(laserLayer, x, y));
            playDamageSound();
        }
        if (hasTile(wrenchLayer, x, y)) {
            player.newBackup();
            player.addHealth(wrenchValue(wrenchLayer, x, y));
        }
        checkObjective(player);
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
     * Checks if the player has reached the objective, and updates the current objective.
     * @param player to be checked.
     */
    private void checkObjective(Player player) {
        int x = player.getxPos(),
                y = player.getyPos();
        if (hasTile(flagLayer, x, y)) {
            player.checkObjective(flagValue(flagLayer,x,y));
        }
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
        return getNeighbour(pl.getxPos(), pl.getyPos(), direction);
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
     * @param direction to move in
     * @param player to be moved
     * @return true if the path is blocked
     */
    private boolean isBlocked(Player player, int direction) {
        return isBlocked(player.getxPos(), player.getyPos(), direction);
    }

    /**
     * Checks the direction the laser at given coords is pointing until it hits a player or wall
     * @param x coordinate of laser
     * @param y coordinate of laser
     * @param dir direction laser is pointing
     * @return true when the laser has hit a player, false if it hit a wall
     */
    private boolean laser(int x, int y, int dir){
        if (hasTile(playerLayer, x, y)){
            players[playerLayer.getCell(x, y).getTile().getId()].addHealth(-1);
            return true;
        }
        else if (!isBlocked(x, y, dir)){
            int[] nb = getNeighbour(x, y, dir);
            return laser(nb[0], nb[1], dir);
        }
        return false;
    }

    public void muteToggle() {
        damageSound.muteToggle();
    }
}
