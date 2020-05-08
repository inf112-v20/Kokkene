package inf112.skeleton.app.actor;

import com.badlogic.gdx.graphics.Color;
import inf112.skeleton.app.game.Menu;
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

    /**
     * @return The color of the actor.
     */
    public Color getColor() {
        return this.color;
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
     * Currently chooses the best possible sequence of cards without considering other players or damage.
     */
    private void aiMoveInsane() {
        if (hand.cardsToSelect() == 0) {
            setReady(true);
            return;
        }
        int currentObj = getObjective() - 1; // Index of current objective
        createSequences();
        ArrayList<ArrayList<Integer>> successful = findSuccessful(currentObj, allPermutations);

        // Gets the best paths to each subsequent objective from the list of successful paths to the prev. obj.
        while (1 < successful.size() && currentObj < board.objectives.size()) { // Size == 1 when it doesn't reach obj.
            currentObj++;
            successful = findSuccessful(currentObj, successful);
        }
        for (int i : successful.get(0)) { // Selects the cards from the optimal sequence
            hand.toggleCard(hand.plHand[i]);
        }
        if (getSelected().size() < hand.cardsToSelect()) { // If it malfunctions, use lower difficulty
            aiMoveHard();
        }
        setReady(true);
    }

    /**
     * Always move towards the current objective taking into account board elements, but not other robots or damage
     */
    private void aiMoveHard() {
        int[] aiXYD = {getxPos(), getyPos(), getOrientation()},
                obXY = board.objectives.get(getObjective() - 1);

        int tries = 0;
        while (getSelected().size() < hand.cardsToSelect()) { // While not enough cards are selected
            tries++;
            if (tries > 15) { // In case of locking it will go down a difficulty level
                aiMoveMedium();
                continue;
            }

            Card current = hand.plHand[0];
            int[] currentXYD = board.simulatePhase(current, aiXYD, aiXYD[2], getSelected().size());

            for (int i = 0; i < hand.plHand.length; i++) { // Sets current card to valid unselected card
                current = hand.plHand[i];
                currentXYD = board.simulatePhase(current, aiXYD, aiXYD[2], getSelected().size());
                if (!(getSelected().contains(current) || currentXYD[2] == -1)) {
                    break;
                }
            }

            for (Card c : hand.plHand) {
                int[] sim = board.simulatePhase(c, aiXYD, aiXYD[2], getSelected().size()); // Simulates phase
                if (sim[2] == -1 || getSelected().contains(c)) {
                    // If current card is lethal (Outside board or in a hole), or is already used; skip current card
                    continue;
                }
                // If this card brings you closer || if there is a better turn; update current selected card
                if (distance(sim, obXY) < distance(currentXYD, obXY) // Execute if it brings you closer to the objective
                        ||
                        (distance(sim, obXY) <= distance(currentXYD, obXY) // If current is a turn the distances are equal
                                && c.getType() == 2
                                && badTurn(aiXYD, obXY, aiXYD[2], current, true) // current=best, no point in changing
                                && !board.isBlocked(sim[0], sim[1], sim[2])
                                && sim[2] != (Board.towardTarget(sim[0], sim[1], obXY[0], obXY[1]) + 2) % 4)) {  // Not opposite
                    current = c;
                    currentXYD = sim;
                }
            }
            hand.toggleCard(current);
            aiXYD = currentXYD; // Updates start position to simulate from
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
        while (getSelected().size() < hand.cardsToSelect()) { // While not enough cards are selected
            tries++;
            if (tries > 15) { // In case of locking it will choose random cards
                aiMoveEasy();
                continue;
            }

            Card current = hand.plHand[0];
            int[] currentXY = board.simulateMove(current, aiXYD, aiXYD[2]);

            for (Card curr : hand.plHand) { // Sets the current card to a valid unselected card
                current = curr;
                currentXY = board.simulatePhase(current, aiXYD, aiXYD[2], getSelected().size());
                if (!(getSelected().contains(current) || currentXY[2] == -1)) { // Exits when valid card is selected
                    break;
                }
            }

            for (Card c : hand.plHand) { // Checks all the cards in hand for which is best
                int[] sim = board.simulateMove(c, aiXYD, aiXYD[2]);
                if (sim[2] == -1 || getSelected().contains(c)) { // Skip if already selected or it kills you
                    continue;
                }
                // If this card brings you closer || if there is a better turn; update current selected card
                if (distance(sim, obXY) < distance(currentXY, obXY) // if the card brings us closer to the objective
                        ||
                        (distance(sim, obXY) <= distance(currentXY, obXY) // If current is a turn the distance is equal
                                && c.getType() == 2
                                && badTurn(aiXYD, obXY, aiXYD[2], current, false) // Current=best; no point to change
                                && !board.isBlocked(sim[0], sim[1], sim[2])
                                && sim[2] != (Board.towardTarget(sim[0], sim[1], obXY[0], obXY[1]) + 2) % 4)) { // not opposite
                    current = c;
                    currentXY = sim;
                }
            }
            hand.toggleCard(current);
            aiXYD = currentXY; // Updates start position to simulate from
        }
        setReady(true);
    }

    /**
     * Selects random cards from the hand until enough are selected
     */
    public void aiMoveEasy() {
        int cardsToSelect = hand.cardsToSelect();

        while (getSelected().size() < cardsToSelect) {
            int rand = (int) (Math.random() * hand.plHand.length);
            while (getSelected().contains(hand.plHand[rand])) { // Walks the hand while the card is already selected
                rand = (rand + 1) % hand.plHand.length;
            }
            hand.toggleCard(hand.plHand[rand]);
        }
        assert getSelected().size() == cardsToSelect : "Should be " + cardsToSelect + ", not " + getSelected().size();
        setReady(true);
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
    private boolean badTurn(int[] xy, int[] ob, int dir, Card c, boolean mapElem) {
        int newDir;
        if (mapElem) {
            newDir = board.simulatePhase(c, xy, dir, getSelected().size())[2];
        } else {
            newDir = board.simulateMove(c, xy, dir)[2];
        }
        return board.isBlocked(xy[0], xy[1], newDir) || newDir != Board.towardTarget(xy[0], xy[1], ob[0], ob[1]);
    }

    /**
     * Calculates the distance between aiXY and obXY
     *
     * @param aiXY coordinates of the AI
     * @param obXY coordinates of the objective
     * @return the distance between the two arguments
     */
    private double distance(int[] aiXY, int[] obXY) {
        return Math.sqrt(((obXY[0] - aiXY[0]) ^ 2) + ((obXY[1] - aiXY[1]) ^ 2));
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

        int shortest = 6;
        ArrayList<ArrayList<Integer>> successful = new ArrayList<>();
        ArrayList<Integer> best = new ArrayList<>();

        for (ArrayList<Integer> sequence : permutations) { // Check all permutations
            int[] newXY = aiXYD.clone(); // Always start the simulations at the current position of the AI
            for (int phase = 0; phase < sequence.size() + hand.getLocked().size(); phase++) { // Walk through sequence
                newXY = simulateLocked(newXY, phase, sequence); // Simulates the card of current phase, hand and locked
                if (newXY[2] == -1 || phase > shortest) { // discard sequence if it kills or is longer than current
                    break;
                }
                if (newXY[0] == obXY[0] && newXY[1] == obXY[1]) { // If it hits the obj; save sequence as successful
                    if (phase < shortest) { // Remove all paths that are longer than this path to the objective
                        successful.clear();
                    }
                    successful.add(sequence); // Add sequence to list of successful sequences that reach the objective
                    shortest = phase; // Update lenth of shortest path to objective
                    break;
                }
            }
            // If this sequence brings us closer to the objective than any previous, save it as current best sequence
            if (newXY[2] != -1 && successful.isEmpty() && distance(newXY, obXY) < distance(current, obXY)) {
                current = newXY;
                best = sequence;
            }
        }
        // If no sequence takes us to the objective, save and return the one that brings us the closest
        if (successful.isEmpty()) {
            successful.add(best);
        }
        return successful;
    }

    /**
     * Simulates phase and return coordinates, takes into account locked registers
     *
     * @param xyd      coordinates you start at
     * @param index    index of the sequence to simulate, if larger than sequence size will return sim of locked card
     * @param sequence sequence of cards to simulate
     * @return coordinates after simulating the card indicated by the index
     */
    private int[] simulateLocked(int[] xyd, int index, ArrayList<Integer> sequence) {
        if (index < sequence.size()) {
            return board.simulatePhase(hand.plHand[sequence.get(index)], xyd, xyd[2], index + 1);
        }
        int lastLocked = hand.getLocked().size() - 1;
        return board.simulatePhase(hand.getLocked().get(lastLocked - (index - sequence.size())),
                xyd, xyd[2], index + 1);
    }

    /**
     * Creates a HashSet that contains all possible permutations of the hand
     * size = P(n,r) = n!/((n−r)!)
     */
    private void createSequences() {
        allPermutations.clear();
        ArrayList<Integer> handArray = getHandArray();
        heapPermutation(handArray, handArray.size(), hand.cardsToSelect());
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

        for (int i = 0; i < size; i++) {

            heapPermutation(a, size - 1, choiceOfCards);

            // if size is odd, swap first and last element
            int temp;
            if (size % 2 == 1) {
                temp = a.get(0);
                a.set(0, a.get(size - 1));
            }

            // If size is even, swap ith and last
            // element
            else {
                temp = a.get(i);
                a.set(i, a.get(size - 1));
            }
            a.set(size - 1, temp);
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
