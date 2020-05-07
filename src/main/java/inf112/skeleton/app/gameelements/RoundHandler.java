package inf112.skeleton.app.gameelements;

import inf112.skeleton.app.actor.Player;

import java.util.ArrayList;
import java.util.Collections;

public class RoundHandler {

    private Board board;
    Player[] players;
    Deck deck;

    public int phase = 0;

    public RoundHandler(Board board) {
        this.board = board;
        this.players = board.getPlayers();
        this.deck = board.getDeck();
    }


    /**
     * Checks if the player is on a flag, and updates the backup and current objective.
     *
     * @param player to be checked.
     */
    private void finishPhase(Player player) {
        int x = player.getxPos(),
                y = player.getyPos();
        if (board.hasTile(board.flagLayer, x, y)) {
            player.checkObjective(board.flagValue(board.flagLayer, x, y));
        }
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
            board.playerLayer.setCell(p.getxPos(), p.getyPos(), null);
            board.healthLayer.setCell(p.getxPos(), p.getyPos(), null);
            p.respawn();
            board.playerLayer.setCell(p.getxPos(), p.getyPos(), p.getPlayerState().getPlayerStatus());
            board.healthLayer.setCell(p.getxPos(), p.getyPos(), p.getHealthBars().getPlayerHealthBar());

            if (board.hasTile(board.wrenchLayer, p.getxPos(), p.getyPos())) {
                p.newBackup();
                p.addHealth(board.wrenchValue(board.wrenchLayer, p.getxPos(), p.getyPos()));
            }
            else if (board.hasTile(board.flagLayer, p.getxPos(), p.getyPos())) {
                p.addHealth(1);
            }

            if (p.announcePowerDown) {
                p.addHealth(p.getMaxHealth() - p.getHealth());
                if (p.playerPower) {
                    p.announcePowerDown = false;
                    p.wasPlayerPoweredLast = true;
                }
                p.hand.lockRegister(); //In case damage is taken
                p.playerPower = !p.playerPower;
            } else {
                p.wasPlayerPoweredLast = false;
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
        board.setPlayersOnBoard();
        board.fireLasers();
        board.nullPlayerBoard();
        for (Player finish : players) {
            finishPhase(finish);
        }
    }

    /**
     * Makes the given player interact with all board objects
     * @param player to interact
     * @param phase to do
     */
    public void afterPhase(Player player, int phase){
        int x = player.getxPos(),
                y = player.getyPos();

        if (board.hasTile(board.conveyorLayer, x, y)) {
            board.moveDoubleConveyor(player, x, y);
        }
        x = player.getxPos(); y = player.getyPos();
        //Must update x and y because player may change position.
        if (board.hasTile(board.conveyorLayer, x, y)){
            board.moveConveyor(player, x, y);
        }
        x = player.getxPos(); y = player.getyPos();
        if (board.hasTile(board.pushLayer, x, y)){
            board.doPush(player, phase, x, y);
        }
        x = player.getxPos(); y = player.getyPos();
        if (board.hasTile(board.gearLayer, x, y)){
            player.turn(board.gearDirection(board.gearLayer, x, y));
        }
    }

}