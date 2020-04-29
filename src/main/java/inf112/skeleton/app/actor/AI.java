package inf112.skeleton.app.actor;

import com.badlogic.gdx.graphics.Color;
import inf112.skeleton.app.game.Menu;
import inf112.skeleton.app.game.RoboRally;
import inf112.skeleton.app.gameelements.Board;
import inf112.skeleton.app.gameelements.Card;
import inf112.skeleton.app.gameelements.Deck;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;


public class AI extends Player {

    // Color to this specific AI gets taken from a global constant of AI colors.
    private final Color color;

    private Queue<ArrayList<Integer>> subSequences;
    private ArrayList<Integer> moveSet;

    /**
     * @param name        the name for this robot.
     * @param xPos        starting x-position for this robot.
     * @param yPos        starting y-position for this robot.
     * @param orientation orientation (direction) in a 0-3 scale.
     * @param id          numeric ID of the player
     */
    public AI(String name, int xPos, int yPos, int orientation, int id) {
        super(name, xPos, yPos, orientation, id);
        this.color = new AIColor().Colors[id - 2];

    }

    public void setHand(Deck deck) {
        hand = new Hand(this, deck);
        aiMove();
    }

    public void aiMove() {
        switch (Menu.OptionsUtil.aiDifficulty) {
            case (1):
                aiMoveMedium();
                break;
            case (2):
                aiMoveHard();
                break;
            default:
                aiMoveEasy();
        }
    }

    private void aiMoveHard() {
        int cardsToSelect = hand.cardsToSelect();

        // initialize the moveSet if it's empty, else play the next card.
        if(moveSet == null) {
            try {
                this.moveSet = getMoveClosestToObject(cardsToSelect);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        while (getSelected().size() < cardsToSelect) {
            int nextCard = moveSet.get(0);
            if (getSelected().contains(hand.plHand[nextCard])) {
                continue;
            }
            hand.toggleCard(hand.plHand[nextCard]);
            moveSet.remove(0);
            setReady(true);
        }
    }

    private void aiMoveMedium() {
        //TODO
    }

    public void aiMoveEasy() {
        int cardsToSelect = hand.cardsToSelect();

        while (getSelected().size() < cardsToSelect) {
            int rand = (int) (Math.random() * hand.plHand.length);
            if (getSelected().contains(hand.plHand[rand])) {
                continue;
            }
            hand.toggleCard(hand.plHand[rand]);
            setReady(true);
        }

        assert getSelected().size() == cardsToSelect : "Should be " + cardsToSelect + ", not " + getSelected().size();
    }


    /**
     * This method calculates the cards which makes the AI get the furthest towards the goal.
     */
    public void aiMovePerfect() {
        int cardsToSelect = hand.cardsToSelect();

        int obX = RoboRally.getBoard().objectives.get(this.getObjective() - 1)[0],
                obY = RoboRally.getBoard().objectives.get(this.getObjective() - 1)[1];

        // this many cards should be toggled.
        for (int i = 0; i < cardsToSelect; i++) {

            // check every card in the hand for the card which makes the distance to "objective" the lowest.
            double distance = 9000.1;
            Card cardToChoose = null;
            for (int j = 0; j < hand.plHand.length; j++) {
                //TODO these 2 parameters
                int aiX = 0; // x coordinate when hand[j] has happened
                int aiY = 0; // y coordinate when hand[j] has happened

                // sqrt((aiX - obX)^2 + (aiY - obY)^2) == distance between the objective the AI when hand[j] has happened
                double calculate = Math.sqrt((aiX - obX)^2 + (aiY - obY)^2);

                if(calculate < distance) {
                    distance = calculate;
                    cardToChoose = hand.plHand[j];
                }
            }

            // all cards are now checked, for the one card which makes the AI get the furthest towards the goal.
            // check for cardsToSelect more cards.
            hand.toggleCard(cardToChoose);
            setReady(true);
        }

    }


    /**
     *
     * Calculates the sequence of cards which makes the AI get the closest to the current objective
     *
     * @param cardsToselect
     * @return a list of the best sequence of cards to play
     * @throws CloneNotSupportedException
     */
    public ArrayList<Integer> getMoveClosestToObject(int cardsToselect) throws CloneNotSupportedException {

        // initialize the virtual board
        Board board = createVirtualBoard();

        // how many cards to calculate from.
        int cardsToSelect = hand.cardsToSelect();

        // create an array of N length unique sequences of cards to play
        ArrayList<Integer> sequences = new ArrayList<Integer>();

        createSequences(cardsToSelect, hand.plHand.length, sequences);

        double distance = 9000.1;
        int sequence = 0;
        ArrayList<Integer> bestSequence = new ArrayList<>();
        // play each sequence of cards and check which sequence gets the closest to the objective.
        while(!subSequences.isEmpty()) {
            int i = 0;
            AI temp = (AI) this.clone();
            int obX = RoboRally.getBoard().objectives.get(temp.getObjective() - 1)[0],
                    obY = RoboRally.getBoard().objectives.get(temp.getObjective() - 1)[1];

            for (int j = 0; j < cardsToSelect; j++) {
                // do the move with the unique sequence
                int card = subSequences.peek().get(j);
                board.doMove(temp, hand.plHand[card].getMove());
            }

            int aiX = temp.getxPos(),
                    aiY = temp.getyPos();

            //calculate the new position AI is in, then find the distance between AIXY and objectiveXY
            double calculate = Math.sqrt((aiX - obX) ^ 2 + (aiY - obY) ^ 2);

            if (calculate < distance) {
                distance = calculate;

                //save this sequence
                bestSequence = subSequences.poll();
            }
            else {
                subSequences.poll();
            }
        }
        System.out.println(bestSequence);
        return bestSequence;
    }

    /**
     * TODO
     * create an array of every unique sequence one could play n cards.
     * <p>
     * on the format int[0] = {0,1,2,3}, int[1] = {1,2,3,4}, int[2] = {4,3,2,1}... etc
     *
     * @param n
     * @return an array of unique sequences of n length
     */
    private void createSequences(int n, int k, ArrayList<Integer> A) {
        if (n <= 0) {
            subSequences.add(A);
        } else {
            for (int i = 1; i <= k; i++) {
                A.add(n - 1, i);
                createSequences(n - 1, k, A);
            }
        }
    }

    // initializes a a copy of the current board, to do calculations on.
    private Board createVirtualBoard() throws CloneNotSupportedException {
        return RoboRally.getCopyOfBoard();

    }

    /**
     * @return The color of the actor.
     */
    public Color getColor() {
        return this.color;
    }

}
