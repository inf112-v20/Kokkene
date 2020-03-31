package inf112.skeleton.app.player;

import inf112.skeleton.app.objects.Deck;

public class AI extends Player {

    /**
     * @param name        the name for this robot.
     * @param xPos        starting x-position for this robot.
     * @param yPos        starting y-position for this robot.
     * @param orientation orientation (direction) in a 0-3 scale.
     * @param id          numeric ID of the player
     */
    public AI(String name, int xPos, int yPos, int orientation, int id) {
        super(name, xPos, yPos, orientation, id);
    }

    public void setHand(Deck deck) {
        super.setHand(deck);
        while (getSelected().size() < cardsToSelect()) {
            int rand = (int) (Math.random() * getCards().length);
            if (getSelected().contains(getCards()[rand])) {
                continue;
            }
            toggleCard(getCards()[rand]);
        }
        assert getSelected().size() == cardsToSelect() : "Should be " + cardsToSelect() + ", not " + getSelected().size();
        setReady(true);
    }
}
