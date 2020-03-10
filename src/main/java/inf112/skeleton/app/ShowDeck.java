package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.player.Player;

public class ShowDeck extends InputAdapter {

    /**
     * TODO
     */

    private Player player;
    private int WIDTH = Main.cfg.width;
    private int HEIGHT = Main.cfg.height;

    public TextureRegion[] texturedCards;

    SpriteBatch batch;
    //BitmapFont font = new BitmapFont();
    //Texture card = new Texture(Gdx.files.internal("pictures/card.png"));
    //TextureRegion tr = new TextureRegion(card);
    Pixmap pixmap;
    Texture texture;


    //float widthHeightRatio = (float)tr.getRegionHeight()/(float)tr.getRegionWidth();

    public ShowDeck(Player player) {
        Gdx.input.setInputProcessor(this);
        this.player = player;
        texturedCards = new TextureRegion[player.getCards().length];
        //font.setColor(Color.BLACK);
        //createCardTexture();
        batch = new SpriteBatch();
        pixmap = new Pixmap(Gdx.files.internal("pictures/card.png"));
        texture = new Texture(pixmap);

    }

    /**
     * Renders the cards which the player is holding
     * if the mouse coordinates collide with any of the cards they are highlighted
     * like in hearthstone TODO
     */
    public void render() {
        batch.begin();
        for (int i = 0; i < player.getCards().length; i++) {
            batch.draw(texture,0, 0);

        }

        batch.end();
    }

    /**
     * TODO
     * Create the texture for the cards and save them into the array texturedCards.
     *
    private void createCardTexture() {
        for(int i = 0; i < texturedCards.length; i++) {
            this.texturedCards[i] = new TextureRegion(card);

        }

    }
     */

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        pixmap.setColor(Color.GREEN);
        pixmap.fillCircle(screenX,screenY, 5);
        texture.draw(pixmap,0,0);
        return true;
    }
}