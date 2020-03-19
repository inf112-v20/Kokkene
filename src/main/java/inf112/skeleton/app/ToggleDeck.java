package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.game.RoboRally;
import inf112.skeleton.app.player.Player;

public class ToggleDeck extends InputAdapter implements Screen {

    /**
     * TODO
     */

    private int WIDTH = Main.cfg.width;
    private int HEIGHT = Main.cfg.height;

    SpriteBatch batch;
    BitmapFont font;
    Pixmap pixmap;
    Texture texture;
    Sprite playSprite;

    Player player = RoboRally.player;

    //array of all card sprites
    Sprite[] allSprites;


    //float widthHeightRatio = (float)tr.getRegionHeight()/(float)tr.getRegionWidth();

    public ToggleDeck(Texture[] cards) {
        batch = new SpriteBatch();
        pixmap = new Pixmap(Gdx.files.internal("pictures/card.png"));
        texture = new Texture(pixmap);
        playSprite = new Sprite(texture);
        playSprite.setSize(texture.getWidth(),texture.getHeight());

        createAllSprites(cards);

    }

    private void createAllSprites(Texture[] cards) {
        allSprites = new Sprite[cards.length];
        for(int i = 0; i < allSprites.length; i++) {
            allSprites[i] = new Sprite(cards[i]);
        }
    }

    /**
     * Renders the cards which the player is holding
     * if the mouse coordinates are over that specific card. Highlight it and put it in the stack.
     * with its corresponding index number.
     */

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        /*if (player.getSelected().size() == player.cardsToSelect() && lockButton.getBoundingRectangle().contains(screenX, screenY)){
            //TODO must add button to the right of the cards that will call RoboRally.getBoard().doTurn()
        }*/
        for(int i = 0; i < allSprites.length; i++) {
            if (allSprites[i].getBoundingRectangle().contains(screenX, screenY)
                    && player.getSelected().size() < player.cardsToSelect()) {

                player.toggleCard(player.getCards()[i]);

                allSprites[i].flip(true, true);

                if (player.getSelected().size() >= player.cardsToSelect()){
                    RoboRally.getBoard().doTurn();
                }

                return true;
            }
        }
        return false;

    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case (Input.Keys.M):
                RoboRally.muteToggle();
                break;
            case (Input.Keys.F11):
                RoboRally.fullscreenToggle();
                break;
            case (Input.Keys.ESCAPE):
            case (Input.Keys.Q):
                Gdx.app.exit();
                break;
        }
        return false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.input.setInputProcessor(this);
        batch.begin();

        for (int i = 0; i < allSprites.length; i++) {
            int xPos = (int)(i*allSprites[i].getWidth());
            int yPos = 0;
            batch.draw(allSprites[i],xPos, yPos);
            allSprites[i].setPosition(xPos,HEIGHT-allSprites[i].getHeight());

        }

        batch.end();

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}