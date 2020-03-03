package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import org.lwjgl.opengl.Display;

public class ShowDeck extends InputAdapter {

    /**
     * TODO
     */

    private Player player;
    private final Vector2 mouseInWorld2D = new Vector2();

    public TextureRegion[] texturedCards;

    SpriteBatch batch = new SpriteBatch();
    BitmapFont font = new BitmapFont();
    Texture card = new Texture(Gdx.files.internal("pictures/card.png"));
    TextureRegion tr = new TextureRegion(card);


    public ShowDeck(Player player) {
        this.player = player;
        texturedCards = new TextureRegion[player.getCards().length];
        font.setColor(Color.BLACK);
        createCardTexture();
    }

    /**
     * Renders the cards which the player is holding
     * if the mouse coordinates collide with any of the cards they are highlighted
     * like in hearthstone TODO
     */
    public void render() {

        mouseInWorld2D.x = Gdx.input.getX();
        mouseInWorld2D.y = Gdx.input.getY();
        batch.begin();
        for (int i = 0; i < player.getCards().length; i++) {

            batch.draw(tr, (Main.cfg.width/2f)-(i*5),40-(i*10),0,0, 40, 80, 4,4, (i*10)-20);

        }

        batch.end();
    }

    /**
     * TODO
     * Create the texture for the cards and save them into the array texturedCards.
     */
    private void createCardTexture() {
        for(int i = 0; i < texturedCards.length; i++) {
            this.texturedCards[i] = new TextureRegion(card);

        }

    }
}