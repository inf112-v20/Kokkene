package inf112.skeleton.app.objects;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Deck {
    Scanner Scn = new Scanner(new File("assets/deck1994.txt"));

    //Saves all the cards from the given txt file in this queue
    public Queue<Card> Cards = new LinkedList<>();

    //Creates a deck from the text file set inside the constructor
    public Deck() throws IOException {
        while (Scn.hasNextLine()) {
            String data = Scn.nextLine();

            //Parsing through the information provided in the txt file and saving them for use in the card constructor
            int priority = Integer.parseInt(data.substring(0, data.indexOf("P")));
            int indexNext = data.indexOf("P") + 1;

            int name = Integer.parseInt(data.substring(indexNext, data.indexOf("N")));
            indexNext = data.indexOf("N") + 1;

            int move = Integer.parseInt(data.substring(indexNext, data.indexOf("M")));
            Card card = new Card(priority, name, move);
            Cards.add(card);
        }
    }

    //Shuffles the Current deck
    public void shuffle() {
        Collections.shuffle((List<?>) Cards);
    }
}