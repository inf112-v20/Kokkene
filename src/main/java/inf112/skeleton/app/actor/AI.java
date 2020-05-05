package inf112.skeleton.app.actor;

import com.badlogic.gdx.graphics.Color;
import inf112.skeleton.app.game.Menu;
import inf112.skeleton.app.game.RoboRally;
import inf112.skeleton.app.gameelements.Board;
import inf112.skeleton.app.gameelements.Card;
import inf112.skeleton.app.gameelements.Deck;
import inf112.skeleton.app.util.AIColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class AI extends Player {

    // Color to this specific AI gets taken from a global constant of AI colors.
    private final Color color;

    private final Board board;

    private final Set<ArrayList<Integer>> allPermutations = new HashSet<>();

    /**
     * @param name        the name for this robot.
     * @param xPos        starting x-position for this robot.
     * @param yPos        starting y-position for this robot.
     * @param orientation orientation (direction) in a 0-3 scale.
     * @param id          numeric ID of the player
     */
    public AI(Board board, String name, int xPos, int yPos, int orientation, int id) {
        super(name, xPos, yPos, orientation, id);
        this.board = board;
        this.color = new AIColor().Colors[id - 2];

    }

    public void setHand(Deck deck) {
        hand = new Hand(this, deck);
        aiMove();
    }

    /**
     * Calls the correct aiMove function depending on selected difficulty
     */
    public void aiMove() {
        switch (Menu.OptionsUtil.aiDifficulty) {
            case (0):
                aiMoveEasy();
                break;
            case (1):
                aiMoveMedium();
                break;
            case (2):
                aiMoveHard();
                break;
            case (3):
                aiMoveInsane();
                break;
            default:
                throw new IllegalArgumentException("Unsupported Difficulty: " + Menu.OptionsUtil.aiDifficulty);
        }
    }

    /**
     * Returns Arraylist with all sequences leading to the current objective, if none reach it will return the closest
     *
     * @param obj          index of the objective (Obj nr -1)
     * @param permutations List of permutations to iterate over
     * @return All sequences reaching the objective
     */
    private ArrayList<ArrayList<Integer>> findSuccessful(int obj, Collection<ArrayList<Integer>> permutations) {
        int[] aiXYD = {getxPos(), getyPos(), getOrientation()},
                obXY = board.objectives.get(obj),
                current = aiXYD;

        int shortest = hand.cardsToSelect();
        ArrayList<ArrayList<Integer>> successful = new ArrayList<>();
        ArrayList<Integer> best = new ArrayList<>();

        for (ArrayList<Integer> sequence : permutations) {
            int[] newXY = aiXYD;
            for (int i : sequence) {
                int index = sequence.indexOf(i);
                if (newXY[2] == -1 || index > shortest) {
                    break;
                }
                newXY = board.simulatePhase(hand.plHand[i], newXY, newXY[2], index + 1);
                if (newXY[0] == obXY[0] && newXY[1] == obXY[1] && index <= shortest) {
                    if (index < shortest) {
                        successful.clear();
                    }
                    successful.add(sequence);
                    shortest = index;
                    break;
                }
            }
            if (successful.isEmpty() && distance(newXY, obXY) < distance(current, obXY)) {
                current = newXY;
                best = sequence;
            }
        }
        if (successful.isEmpty()) {
            successful.add(best);
        }
        return successful;
    }

    /**
     * Currently chooses the best possible sequence of cards without considering other players or damage.
     */
    private void aiMoveInsane() {
        //TODO implement BFS to lower computation
        if (hand.cardsToSelect() == 0) {
            return;
        }
        int currentObj = getObjective() - 1;
        createSequences();
        ArrayList<ArrayList<Integer>> successful = findSuccessful(currentObj, allPermutations);

        while (1 < successful.size()) {
            currentObj++;
            successful = findSuccessful(currentObj, successful);
        }
        for (int i : successful.get(0)) {
            hand.toggleCard(hand.plHand[i]);
        }
        if (getSelected().size() < hand.cardsToSelect()) {
            System.out.println(getSelected());
            aiMoveHard();
        }
        setReady(true);
    }

    /**
     * Always move towards the current objective taking into account board elements, but not other robots or damage
     */
    private void aiMoveHard() {
        //TODO
        int[] aiXYD = {getxPos(), getyPos(), getOrientation()},
                obXY = board.objectives.get(getObjective() - 1);

        int tries = 0;
        while (getSelected().size() < hand.cardsToSelect()) {
            tries++;
            if (tries > 15) { // In case of locking it will choose random cards
                aiMoveMedium();
                continue;
            }

            Card current = hand.plHand[0];

            for (int i = 0; (i < hand.plHand.length) && getSelected().contains(current); i++) {
                current = hand.plHand[i];
            }
            int[] currentXYD = board.simulatePhase(current, aiXYD, aiXYD[2], getSelected().size());

            for (Card c : hand.plHand) {
                if (getSelected().contains(c)) { // Skips used cards
                    continue;
                }
                int[] sim = board.simulatePhase(c, aiXYD, aiXYD[2], getSelected().size()); // Simulates phase
                if (sim[2] == -1) { // If it kills the player (Outside board or in a hole), skip
                    continue;
                }
                if (distance(sim, obXY) <= distance(currentXYD, obXY)) { // Checks if it brings you closer to the obj
                    current = c;
                    currentXYD = sim;
                }
                if (current.getType() == 2) { // If current selected card is a turn; check if better turns are available
                    if (bestTurn(aiXYD, obXY, aiXYD[2], c, true)
                            && distance(sim, obXY) <= distance(currentXYD, obXY)) { // Turn optimally
                        current = c;
                        currentXYD = sim;
                        break;
                    }
                    if (!board.isBlocked(sim[0], sim[1], sim[2])
                            && distance(sim, obXY) <= distance(currentXYD, obXY) // Not pointing directly away from obj
                            && sim[2] != (Board.towardTarget(sim[0], sim[1], obXY[0], obXY[1]) + 2) % 4) {
                        current = c;
                        currentXYD = sim;
                    }
                }
            }
            hand.toggleCard(current);
            aiXYD = currentXYD;
        }
        setReady(true);
    }

    /**
     * Always tries to move in the direction most towards the next objective without taking into account board elements
     */
    private void aiMoveMedium() {
        int[] aiXYD = {getxPos(), getyPos(), getOrientation()},
                obXY = board.objectives.get(getObjective() - 1);

        int tries = 0;
        while (getSelected().size() < hand.cardsToSelect()) {
            tries++;
            if (tries > 15) { // In case of locking it will choose random cards
                aiMoveEasy();
                continue;
            }

            Card current = hand.plHand[0];

            for (int i = 0; (i < hand.plHand.length) && getSelected().contains(current); i++) {
                current = hand.plHand[i];
            }
            int[] currentXY = board.simulateMove(current, aiXYD, aiXYD[2]);

            for (Card c : hand.plHand) {
                if (getSelected().contains(c)) {
                    continue;
                }
                int[] sim = board.simulateMove(c, aiXYD, aiXYD[2]);
                if (sim[2] == -1) {
                    continue;
                }
                if (distance(sim, obXY) <= distance(currentXY, obXY)) {
                    current = c;
                    currentXY = sim;
                }
                if (current.getType() == 2) { // If current selected card is a turn; check if better turns are available
                    if (bestTurn(aiXYD, obXY, aiXYD[2], c, false)
                            && distance(sim, obXY) <= distance(currentXY, obXY)) { // Turn optimally
                        current = c;
                        currentXY = sim;
                        break;
                    }
                    if (!board.isBlocked(sim[0], sim[1], sim[2])
                            && distance(sim, obXY) <= distance(currentXY, obXY) // Not pointing directly away from obj
                            && sim[2] != (Board.towardTarget(sim[0], sim[1], obXY[0], obXY[1]) + 2) % 4) {
                        current = c;
                        currentXY = sim;
                    }
                }
            }
            hand.toggleCard(current);
            aiXYD = currentXY;
        }
        setReady(true);
    }

    /**
     * Selects random cards from the hand
     */
    public void aiMoveEasy() {
        int cardsToSelect = hand.cardsToSelect();

        while (getSelected().size() < cardsToSelect) {
            int rand = (int) (Math.random() * hand.plHand.length);
            if (getSelected().contains(hand.plHand[rand])) {
                continue;
            }
            hand.toggleCard(hand.plHand[rand]);
        }
        assert getSelected().size() == cardsToSelect : "Should be " + cardsToSelect + ", not " + getSelected().size();
        setReady(true);
    }

    /**
     * This method calculates the cards which makes the AI get the furthest towards the goal.
     */
    public void aiMovePerfect() {
        int cardsToSelect = hand.cardsToSelect();

        int obX = board.objectives.get(this.getObjective() - 1)[0],
                obY = board.objectives.get(this.getObjective() - 1)[1];

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
     * Check if given turn card will turn the actor in the optimal direction
     *
     * @param xy  coords of actor
     * @param ob  coords of objective
     * @param dir dir currently pointing
     * @param c   Turn card
     * @return True if the new dir is towards the objective and not blocked, false otherwise
     */
    private boolean bestTurn(int[] xy, int[] ob, int dir, Card c, boolean mapElem) {
        int newDir;
        if (mapElem) {
            newDir = board.simulatePhase(c, xy, dir, getSelected().size())[2];
        } else {
            newDir = board.simulateMove(c, xy, dir)[2];
        }
        return !board.isBlocked(xy[0], xy[1], newDir) && newDir == Board.towardTarget(xy[0], xy[1], ob[0], ob[1]);
    }

    /**
     * Calculates the distance between aiXY and obXY
     *
     * @param aiXY coordinates of the AI
     * @param obXY coordinates of the objective
     * @return the distance between the two arguments
     */
    private double distance(int[] aiXY, int[] obXY) {
        return Math.sqrt((obXY[0] - aiXY[0]) ^ 2 + (obXY[1] - aiXY[1]) ^ 2);
    }

    /**
     *
     * Calculates the sequence of cards which makes the AI get the closest to the current objective
     *
     * @return a list of the best sequence of cards to play
     * @throws CloneNotSupportedException throws CloneNotSupportedException
     */
    public ArrayList<Integer> getMoveClosestToObject() throws CloneNotSupportedException {

        // initialize the virtual board
        Board board;

        createSequences();
        /*
        int originX = this.getxPos();
        int originY = this.getyPos();
         */
        double distance = 9000.1;
        ArrayList<Integer> bestSequence = new ArrayList<>();
        ArrayList<Integer> currentPermutation;
        //Creating a Iterator that iterates through all the hand permutations
        // play each hand and check which hand gets the closest to the objective.
        for (ArrayList<Integer> allPermutation : allPermutations) {
            board = createVirtualBoard();
            currentPermutation = allPermutation;
            AI temp = (AI) this.clone();
            int obX = RoboRally.getBoard().objectives.get(temp.getObjective() - 1)[0],
                    obY = RoboRally.getBoard().objectives.get(temp.getObjective() - 1)[1];

            for (int j = 0; j < hand.cardsToSelect(); j++) {
                // do the move with the unique sequence
                int card = currentPermutation.get(j);
                board.doMove(temp, hand.plHand[card].getMove());
            }

            int aiX = temp.getxPos(),
                    aiY = temp.getyPos();

            //calculate the new position AI is in, then find the distance between AIXY and objectiveXY
            double calculate = Math.sqrt((aiX - obX) ^ 2 + (aiY - obY) ^ 2);

            if (calculate < distance) {
                distance = calculate;

                //save this sequence
                bestSequence = currentPermutation;
            }

            /*
            // set the AI back to its original position and render it invincible
            board.playerLayer.getCell(aiX, aiY).setTile(null);
            board.playerLayer.setCell(originX, originY, temp.getPlayerState().getPlayerStatus());
            board.getPlayers()[1].invinicible();
             */
        }

        System.out.println(bestSequence);
        return bestSequence;
    }

    // initializes a a copy of the current board, to do calculations on.
    private Board createVirtualBoard() throws CloneNotSupportedException {
        return (Board)new Board().clone();

    }

    /**
     * @return The color of the actor.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Creates a HashSet that contains all possible permutations of the hand
     * size = P(n,r) = n!/((n−r)!)
     */
    private void createSequences() {
        allPermutations.clear();
        ArrayList<Integer> handArray = getHandArray();
        heapPermutation(handArray, handArray.size(), hand.cardsToSelect());
        //Should be 15120 if AI is at max HP
        //System.out.println(allPermutations.size());
    }

    //Inspiration and more information: https://www.geeksforgeeks.org/heaps-algorithm-for-generating-permutations/
    /**
     * Generating permutation using Heap Algorithm
     * @param a objects to choose from (ArrayList of the hand)
     * @param size size of the actor's hand
     * @param choiceOfCards Sample of the permutation
     */
    public void heapPermutation(ArrayList<Integer> a, int size, int choiceOfCards)
    //Runtime is pretty bad, but should be manageable since the largest number of permutations = 362880,
    //and the allPermutations HashSet will never reach a size higher than P(9,5) = 15120
    {
        // if size becomes 1 add the permutation to the set. (duplicates will be removed)
        if (size == 1)
            addPermutation(a,choiceOfCards);

        for (int i = 0; i <size; i++) {

            heapPermutation(a, size - 1, choiceOfCards);

            // if size is odd, swap first and last element
            if (size % 2 == 1)
            {
                int temp = a.get(0);
                a.set(0, a.get(size - 1));
                a.set(size - 1, temp);
            }

            // If size is even, swap ith and last
            // element
            else {
                int temp = a.get(i);
                a.set(i, a.get(size - 1));
                a.set(size - 1, temp);
            }
        }
    }

    /**
     * Creates an array containing all the different cards the actor can choose from
     * @return ArrayList<Integer> of form [0, 1, 2, 3...((cards in hand) - 1)]
     */
    public ArrayList<Integer> getHandArray() {
        ArrayList<Integer> handArray = new ArrayList<>();
        for (int i = 0; i <= hand.plHand.length - 1; i++) {
            handArray.add(i);
        }
        return handArray;
    }

    /**
     * Adds the given permutation from the ArrayList's position: 0 to n
     * @param a The permutation of the hand (length = objects)
     * @param r sample size of the permutation
     */
    private void addPermutation(ArrayList<Integer> a, int r) {
        ArrayList<Integer> permutation = new ArrayList<>();
        for (int i = 0; i < r; i++) {
            permutation.add(a.get(i));
        }
        allPermutations.add(permutation);
    }
}
