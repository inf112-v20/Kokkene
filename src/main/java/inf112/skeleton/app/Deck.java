package inf112.skeleton.app;

import java.io.File;
import java.io.IOException;
import java.util.*;

//deck1994 does not contain the correct priorities but follows the rule: (3m -> 2m -> 1m/-1m -> left/right/U-turn)
public class Deck {
    int priority, name, move, indexNext;
    Scanner Scn = new Scanner(new File("assets/deck1994.txt"));

    Queue<Card> Cards = new LinkedList<>();

    //Creates a deck from the text file set inside the constructor
    public Deck() throws IOException {
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

    //Shuffles the Current deck
    public void shuffle() {
        Collections.shuffle((List<Card>) Cards);
    }
}
