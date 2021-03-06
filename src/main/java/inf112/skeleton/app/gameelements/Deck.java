package inf112.skeleton.app.gameelements;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Deck {

    //Saves all the cards from the given txt file in this queue
    public Queue<Card> Cards = new LinkedList<>();
    public Queue<Card> Discard = new LinkedList<>();

    public String deck;

    //Creates a deck from the text file set inside the constructor
    public Deck(String deck) throws IOException {

        this.deck = deck;

        Scanner scn = new Scanner(new File(deck));

        while (scn.hasNextLine()) {
            String data = scn.nextLine();

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

    /**
     * Shuffles the Current deck
     */
    public void shuffle() {
        while (true) {
            assert Cards.peek() != null;
            if (Cards.peek().getOwner() == null) break;
            Card c = Cards.poll();
            assert c != null;
            c.setOwner(null);
            Cards.add(c);
        }
        Collections.shuffle((List<?>) Cards);
    }

    /**
     * Called if the deck is empty; refills the deck from the discard pile
     */
    public void empty() {
        while (!Discard.isEmpty()) {
            Cards.add(Discard.poll());
        }
        shuffle();
    }
}
