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
import inf112.skeleton.app.objects.Board;
import inf112.skeleton.app.objects.Card;
import inf112.skeleton.app.player.Player;

public class ToggleDeck extends InputAdapter implements Screen {

    /**
     * TODO
     */

    private int WIDTH = Main.cfg.width;
    private int HEIGHT = Main.cfg.height;
    private int count;
    private boolean confirm;

    private Button resetButton;
    private Button lockInButton;

    SpriteBatch batch;
    BitmapFont font;
    Pixmap pixmap;
    Texture texture;
    Sprite playSprite;

    Player player = RoboRally.player;
    Board board;

    //array of all card sprites
    Sprite[] allSprites;

    //shows if the sprite has been toggled or not
    boolean[] display;

    //array to keep track of order of cards
    int[] order;

    //float widthHeightRatio = (float)tr.getRegionHeight()/(float)tr.getRegionWidth();

    public ToggleDeck(Texture[] cards, Board board) {
        batch = new SpriteBatch();
        pixmap = new Pixmap(Gdx.files.internal("pictures/card.png"));
        texture = new Texture(pixmap);
        playSprite = new Sprite(texture);
        playSprite.setSize(texture.getWidth(),texture.getHeight());
        font = new BitmapFont();
        font.setColor(Color.GREEN);
        font.getData().setScale(4,3);
        count = 1;
        confirm = false;
        createAllSprites(cards);
        //createButtons();
        this.board = board;
    }

    private void createButtons(){
        resetButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("assets/pictures/button.png"))));
        resetButton.setPosition(10, allSprites[0].getHeight()+10);
    }

    private void createAllSprites(Texture[] cards) {
        allSprites = new Sprite[cards.length];
        display = new boolean[cards.length];
        order = new int[cards.length];
        for(int i = 0; i < allSprites.length; i++) {
            allSprites[i] = new Sprite(cards[i]);
            display[i] = false;
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
        for(int i = 0; i < allSprites.length; i++) {
            if (allSprites[i].getBoundingRectangle().contains(screenX, screenY)
                    && !confirm) {

                //toggle to display number of order above card.
                if(display[i]) {
                    player.toggleCard(player.getCards()[i]);
                    allSprites[i].setColor(Color.WHITE);
                    display[i] = false;
                    order[i] = 0;
                    count--;

                }
                else {
                    player.toggleCard(player.getCards()[i]);
                    allSprites[i].setColor(Color.GREEN);
                    display[i] = true;
                    order[i] = count;
                    count++;
                }
                if (count == 6){
                    confirm = true;
                }

                return true;
            }
        }
        return false;

    }

    private void resetOrder() {
        count = 1;
        confirm = false;
        for(int i = 0; i < order.length; i++) {
            order[i] = 0;
            display[i] = false;
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
                if(count == 6) {
                    RoboRally.getBoard().doTurn();
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
                RoboRally.fullscreenToggle();
                break;
            case (Input.Keys.ESCAPE):
            case (Input.Keys.Q):
                Gdx.app.exit();
                break;
        }
        return false;
    }

    private void arrowMove(Player player, int move) {
        board.doMove(player, move);
        board.afterArrowMove(player);
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
            if(display[i]) {

                font.draw(batch,Integer.toString(order[i]), (int)(i*allSprites[i].getWidth()) + (allSprites[i].getWidth()/2)-10 , allSprites[i].getHeight());
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