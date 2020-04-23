package inf112.skeleton.app.actor;

import com.badlogic.gdx.graphics.Color;
import inf112.skeleton.app.gameelements.Deck;


public class AI extends Player {

    /**
     * @param name        the name for this robot.
     * @param xPos        starting x-position for this robot.
     * @param yPos        starting y-position for this robot.
     * @param orientation orientation (direction) in a 0-3 scale.
     * @param id          numeric ID of the player
     */

    // Color to this specific AI gets taken from a global constant of AI colors.
    private Color color;

    public AI(String name, int xPos, int yPos, int orientation, int id) {
        super(name, xPos, yPos, orientation, id);
        this.color = new AIColor().Colors[id-2];

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

    /**
     *
     * @return
     */
    public Color getColor() {
        return this.color;
    }
}
