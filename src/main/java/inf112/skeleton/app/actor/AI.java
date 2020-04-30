package inf112.skeleton.app.actor;

import com.badlogic.gdx.graphics.Color;
import inf112.skeleton.app.game.Menu;
import inf112.skeleton.app.game.RoboRally;
import inf112.skeleton.app.gameelements.Board;
import inf112.skeleton.app.gameelements.Card;
import inf112.skeleton.app.gameelements.Deck;
import inf112.skeleton.app.util.AIColor;

import java.util.*;


public class AI extends Player {

    // Color to this specific AI gets taken from a global constant of AI colors.
    private final Color color;

    private ArrayList<Integer> moveSet;
    private Set<ArrayList<Integer>> allPermutations = new HashSet<>();

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
                break;
        }
    }

    private void aiMoveHard() {
        // initialize the moveSet if it's empty, else play the next card.
        if(moveSet == null) {
            try {
                this.moveSet = getMoveClosestToObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        while (getSelected().size() < hand.cardsToSelect()) {
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
        aiMovePerfect();
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

        createSequences();

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
     * @return a list of the best sequence of cards to play
     * @throws CloneNotSupportedException throws CloneNotSupportedException
     */
    public ArrayList<Integer> getMoveClosestToObject() throws CloneNotSupportedException {

        // initialize the virtual board
        Board board = createVirtualBoard();

        createSequences();

        double distance = 9000.1;
        ArrayList<Integer> bestSequence = new ArrayList<>();
        ArrayList<Integer> currentPermutation;
        //Creating a Iterator that iterates through all the hand permutations
        Iterator<ArrayList<Integer>> iterator= allPermutations.iterator();
        // play each hand and check which hand gets the closest to the objective.
        while (iterator.hasNext()) {
            currentPermutation = iterator.next();
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
        System.out.println(allPermutations.size());
    }

    //Inspiration and more information: https://www.geeksforgeeks.org/heaps-algorithm-for-generating-permutations/
    /**
     * Generating permutation using Heap Algorithm
     * @param a objects to choose from (ArrayList of the hand)
     * @param size size of the actor's hand
     * @param choiceOfCards Sample of the permutation
     */
    private void heapPermutation(ArrayList<Integer> a, int size, int choiceOfCards)
    //Runtime is pretty bad, but should be manageable since the largest number of permutations = 362880,
    //and the allPermutations HashSet will never reach a size higher than P(9,5) = 15120
    {
        // if size becomes 1 add the permutation to the set. (duplicates will be removed)
        if (size == 1)
            addPermutation(a,choiceOfCards);

        for (int i = 0; i <size; i++)
        {
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
            else
            {
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
    private void addPermutation(ArrayList<Integer> a, int r)
    {
        ArrayList<Integer> permutation = new ArrayList<>();
        for (int i = 0; i < r; i++) {
            permutation.add(a.get(i));
        }
        allPermutations.add(permutation);
    }
}
