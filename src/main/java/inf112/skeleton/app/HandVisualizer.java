package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.game.RoboRally;
import inf112.skeleton.app.objects.Card;
import inf112.skeleton.app.player.Player;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import java.util.Stack;

public class HandVisualizer extends InputAdapter implements Screen {

    /**
     * TODO
     */

    private int WIDTH = Main.cfg.width;
    private int HEIGHT = Main.cfg.height;
    private int countTracker;
    //Keeps track of all the deselected cards
    Stack<Integer> count = new Stack<>();
    private boolean confirm;

    private Button lockInButton;

    SpriteBatch batch;
    BitmapFont font;
    Texture texture;
    Sprite playSprite;

    Player player = RoboRally.player;

    //array of all card sprites
    Sprite[] allSprites;

    //shows if the sprite has been toggled or not
    int[] display;

    //array to keep track of order of cards
    int[] order;

    Pixmap cards;
    Pixmap resizedCards;
    Texture[] textures;
    Sprite[] sp;

    //float widthHeightRatio = (float)tr.getRegionHeight()/(float)tr.getRegionWidth();

    public HandVisualizer(Player player) {

        this.player = player;
        textures = new Texture[player.getCards().length];
        sp = new Sprite[player.getCards().length];
        createCardTexture();

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.GREEN);
        font.getData().setScale(4,3);
        countTracker = 1;
        confirm = false;
        createAllSprites(textures);
        //createButtons();
    }

    /**
     * TODO
     * Create the texture for the cards and save them into the array texturedCards.
     *
     **/
    private void createCardTexture() {

        for(int i = 0; i < textures.length; i++) {
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
            textures[i] = new Texture(resizedCards);
            cards.dispose();
            resizedCards.dispose();
        }
    }

    private void createButtons(){
        Button resetButton = new Button(new TextureRegionDrawable(new TextureRegion(
                new Texture("assets/pictures/button.png"))));
        resetButton.setPosition(10, allSprites[0].getHeight()+10);
    }

    private void createAllSprites(Texture[] cards) {
        allSprites = new Sprite[cards.length];
        display = new int[cards.length];
        order = new int[cards.length];
        for(int i = 0; i < allSprites.length; i++) {
            allSprites[i] = new Sprite(cards[i]);
            display[i] = 0;
            order[i] = 0;
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
        screenY = HEIGHT-screenY;
        //toggle to display number of order above card.
        for(int i = 0; i < allSprites.length; i++) {
            if (allSprites[i].getBoundingRectangle().contains(screenX, screenY)
                    && !confirm) {

                //Checks if the card has been selected before, and turns it white if this is true
                if(display[i] != 0) {
                    player.toggleCard(player.getCards()[i]);
                    allSprites[i].setColor(Color.WHITE);
                    count.push(display[i]);
                    display[i] = 0;
                    order[i] = 0;
                }
                else if ((i) < player.getCards().length && !(count.isEmpty())) {
                        player.toggleCard(player.getCards()[i]);
                        allSprites[i].setColor(Color.GREEN);
                        order[i] = display[i] = count.pop();

                }
                else if((i) < player.getCards().length) {
                    player.toggleCard(player.getCards()[i]);
                    allSprites[i].setColor(Color.GREEN);
                    display[i] = countTracker;
                    order[i] = countTracker;
                    countTracker++;
                }

                if (countTracker == 6){
                    confirm = true;
                }
                return true;
            }
        }
        return false;
    }

    private void resetOrder() {
        countTracker = 1;
        count.empty();
        confirm = false;
        for(int i = 0; i < order.length; i++) {
            order[i] = 0;
            display[i] = 0;
        }
        for (Sprite s : allSprites) {
            s.setColor(Color.WHITE);
        }
        this.dispose();
    }

    @Override
    public boolean keyUp(int keycode) {

        Card c = new Card(0,0,1);
        int move = c.getMove();

        switch (keycode) {
            case (Input.Keys.W):
            case (Input.Keys.UP):
                player.setOrientation(0);
                arrowMove(player,move);
                break;
            case (Input.Keys.A):
            case (Input.Keys.LEFT):
                player.setOrientation(1);
                arrowMove(player,move);
                break;
            case (Input.Keys.S):
            case (Input.Keys.DOWN):
                player.setOrientation(2);
                arrowMove(player,move);
                break;
            case (Input.Keys.D):
            case (Input.Keys.RIGHT):
                player.setOrientation(3);
                arrowMove(player,move);
                break;
            case (Input.Keys.SPACE):
                arrowMove(player,0);
                break;

            case (Input.Keys.C):
                if(countTracker == 6) {
                    RoboRally.getBoard().doTurn();
                    createCardTexture();
                    createAllSprites(textures);
                    resetOrder();
                }
                break;
            case (Input.Keys.R):
                resetOrder();
                break;
            case (Input.Keys.M):
                RoboRally.muteToggle();
                break;
            case (Input.Keys.F11):
                fullscreenToggle();
                break;
            case (Input.Keys.ESCAPE):
            case (Input.Keys.Q):
                Gdx.app.exit();
                break;
        }
        return false;
    }

    private void fullscreenToggle() {
        try {
            if (Display.isFullscreen()) {
                Display.setFullscreen(false);
                Display.setResizable(true);
            }
            else {
                Display.setFullscreen(true);
                Display.setResizable(false);
            }
        }
        catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    private void arrowMove(Player player, int move) {
        RoboRally.getBoard().doMove(player, move);
        RoboRally.getBoard().afterArrowMove(player);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.input.setInputProcessor(this);
        batch.begin();

        font.draw(batch, "reset: R", 10, allSprites[0].getHeight()+40);
        font.draw(batch, "confirm: C", 10, allSprites[0].getHeight()+90);

        for (int i = 0; i < allSprites.length; i++) {
            int xPos = (int)(i*allSprites[i].getWidth());
            int yPos = 0;

            allSprites[i].draw(batch);
            allSprites[i].setPosition(xPos, yPos);

        }

        for(int i = 0; i < display.length; i++) {
            if(display[i] != 0) {
                float spritesWidth = allSprites[i].getWidth();
                font.draw(batch,Integer.toString(order[i]),
                        (int)(i*spritesWidth) + (spritesWidth/2)-10 , allSprites[i].getHeight());
            }
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