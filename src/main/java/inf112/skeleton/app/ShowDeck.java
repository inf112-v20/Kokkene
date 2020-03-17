package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.app.player.Player;

public class ShowDeck {

    /**
     * TODO
     */

    private Player player;
    private int WIDTH = Main.cfg.width;
    private int HEIGHT = Main.cfg.height;

    SpriteBatch batch;
    BitmapFont font = new BitmapFont();
    Pixmap pixmap;
    Pixmap cards;
    Pixmap resizedCards;
    Texture[] texture;
    Sprite[] sp;

    ToggleDeck toggleDeck;

    public ShowDeck(Player player) {
        this.player = player;
        texture = new Texture[player.getCards().length];
        sp = new Sprite[player.getCards().length];
        font.setColor(Color.BLACK);
        createCardTexture();
        batch = new SpriteBatch();
        toggleDeck = new ToggleDeck(texture);
    }

    /**
     * Renders the cards which the player is holding
     * if the mouse coordinates collide with any of the cards they are highlighted
     */
    public void render(float delta) {
        toggleDeck.render(delta);
    }

    /**
     * TODO
     * Create the texture for the cards and save them into the array texturedCards.
     *
     **/
    private void createCardTexture() {

        for(int i = 0; i < texture.length; i++) {
            if(player.getCards()[i].getName() == 0) {
                cards = new Pixmap(Gdx.files.internal("pictures/Move"+player.getCards()[i].getMove()+".png"));
            }
            else if(player.getCards()[i].getName() == 1) {
                cards = new Pixmap(Gdx.files.internal("pictures/BackUp.png"));
            }
            else if(player.getCards()[i].getName() == 2) {
                cards = new Pixmap(Gdx.files.internal("pictures/Turn"+player.getCards()[i].getMove()+".png"));
            }

            resizedCards = new Pixmap(WIDTH/10, 350, cards.getFormat());
            resizedCards.drawPixmap(cards,
                    0, 0, cards.getWidth(), cards.getHeight(),
                    0, 0, resizedCards.getWidth(), resizedCards.getHeight());
            texture[i] = new Texture(resizedCards);
            cards.dispose();
            resizedCards.dispose();
        }

    }

}