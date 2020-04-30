package inf112.skeleton.app.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PermutationCalculator {

    private Set<ArrayList<Integer>> permutationsAt9HP = new HashSet<>();
    private Set<ArrayList<Integer>> permutationsAt8HP = new HashSet<>();
    private Set<ArrayList<Integer>> permutationsAt7HP = new HashSet<>();
    private Set<ArrayList<Integer>> permutationsAt6HP = new HashSet<>();
    private Set<ArrayList<Integer>> permutationsAt5HP = new HashSet<>();
    private Set<ArrayList<Integer>> permutationsAt4HP = new HashSet<>();
    private Set<ArrayList<Integer>> permutationsAt3HP = new HashSet<>();
    private Set<ArrayList<Integer>> permutationsAt2HP = new HashSet<>();

    /**
     * Creates a HashSet that contains all possible permutations of the hand,
     * and adds them to the corresponding HashSet
     * size = P(n,r) = n!/((n−r)!)
     */
    private void createSequences() {
        for (int i = 9; i >= 1; i--) {
            ArrayList<Integer> handArray = getHandArray(i);
            int handSize = handArray.size();
            if (i <= 5) {
                heapPermutation(handArray, handSize, handSize);
            }
            else {
                heapPermutation(handArray, handSize, 5);
            }
        }
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
     * Adds the given permutation from the ArrayList's position: 0 to n
     * @param a The permutation of the hand (length = objects)
     * @param r sample size of the permutation
     */
    void addPermutation(ArrayList<Integer> a, int r)
    {
        ArrayList<Integer> permutation = new ArrayList<>();
        for (int i = 0; i < r; i++) {
            permutation.add(a.get(i));
        }

        //Checks which HashSet to add the permutation
        int HPcheck = a.size();

        switch (HPcheck) {
            case 9:
                permutationsAt9HP.add(permutation);
                break;
            case 8:
                permutationsAt8HP.add(permutation);
                break;
            case 7:
                permutationsAt7HP.add(permutation);
                break;
            case 6:
                permutationsAt6HP.add(permutation);
                break;
            case 5:
                permutationsAt5HP.add(permutation);
                break;
            case 4:
                permutationsAt4HP.add(permutation);
                break;
            case 3:
                permutationsAt3HP.add(permutation);
                break;
            case 2:
                permutationsAt2HP.add(permutation);
                break;
        }
    }

    /**
     * Creates an array containing all the different cards the actor can choose from
     * @param n The size of the wanted hand
     * @return ArrayList<Integer> of form [0, 1, 2, 3...((cards in hand) - 1)]
     */
    public ArrayList<Integer> getHandArray(int n) {
        ArrayList<Integer> handArray = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            handArray.add(i);
        }
        return handArray;
    }

    public static void main(String[] args) {
        PermutationCalculator calc = new PermutationCalculator();
        calc.createSequences();

        System.out.println(calc.permutationsAt9HP.size() + "    |Should be P(9,5) = 15120");
        System.out.println(calc.permutationsAt8HP.size()+ "     |Should be P(8,5) = 6720");
        System.out.println(calc.permutationsAt7HP.size()+ "     |Should be P(7,5) = 2520");
        System.out.println(calc.permutationsAt6HP.size()+ "      |Should be P(6,5) = 720");
        System.out.println(calc.permutationsAt5HP.size()+ "      |Should be P(5,5) = 120");
        System.out.println(calc.permutationsAt4HP.size()+ "       |Should be P(4,4) = 24");
        System.out.println(calc.permutationsAt3HP.size()+ "        |Should be P(3,3) = 6");
        System.out.println(calc.permutationsAt2HP.size()+ "        |Should be P(2,2) = 2");
    }
}
