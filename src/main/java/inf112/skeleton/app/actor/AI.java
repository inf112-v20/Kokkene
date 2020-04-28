package inf112.skeleton.app.actor;

import com.badlogic.gdx.graphics.Color;
import inf112.skeleton.app.game.Menu;
import inf112.skeleton.app.gameelements.Card;
import inf112.skeleton.app.gameelements.Deck;


public class AI extends Player {

    // Color to this specific AI gets taken from a global constant of AI colors.
    private final Color color;

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
            case (0):
                aiMoveEasy();
                break;
            case (1):
                aiMoveMedium();
                break;
            case (2):
                aiMoveHard();
                break;
            default:
                aiMoveEasy();
        }
    }

    private void aiMoveHard() {
        //TODO
    }

    private void aiMoveMedium() {
        //TODO
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

        assert getSelected().size() == cardsToSelect : "Should be " + cardsToSelect + ", not " + getSelected().size();
    }

    /**
     * This method calculates the cards which makes the AI get the furthest towards the goal.
     */
    public void aiMovePerfect() {
        int cardsToSelect = hand.cardsToSelect();

        // this many cards should be toggled.
        for(int i = 0; i < cardsToSelect; i++) {

            // check every card in the hand for the card which makes the distance to "objective" the lowest.
            double distance = 9000.1;
            Card cardToChoose = null;
            for(int j = 0; j < hand.plHand.length; j++) {
                //TODO these 4 parameters
                int aiX; // x coordinate when hand[j] has happened
                int aiY; // y coordinate when hand[j] has happened
                int obX; // ai.getObjective.X
                int obY; // ai.getObjective.Y

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
     * @return The color of the actor.
     */
    public Color getColor() {
        return this.color;
    }

    //We can use this when selecting difficulty
    public Integer getDifficulty() {
        return 0;
    }
}
