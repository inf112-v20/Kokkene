package inf112.skeleton.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Deck {

    Queue<Card> Cards = new LinkedList<>();

    //Creates a deck from the text file set inside the constructor
    public Deck() throws FileNotFoundException {
        int priority, name, move, indexNext;

        Scanner Scn = new Scanner(new File("assets/test.txt"));
        while (Scn.hasNextLine()) {
            String data = Scn.nextLine();

            priority = Integer.parseInt(data.substring(0, data.indexOf("P")));
            indexNext = data.indexOf("P") + 1;

            name = Integer.parseInt(data.substring(indexNext, data.indexOf("N")));
            indexNext = data.indexOf("N") + 1;

            move = Integer.parseInt(data.substring(indexNext, data.indexOf("M")));
            Card card = new Card(priority, name, move);
            Cards.add(card);
        }
    }

    //Prints out the cards in order from the queue
    public void print() {
        for (Card i : Cards) {
            System.out.println(i.toString());
        }
    }

    //Shuffles the Current deck
    public void shuffle() {
        Collections.shuffle((List<Card>) Cards);
    }
}
