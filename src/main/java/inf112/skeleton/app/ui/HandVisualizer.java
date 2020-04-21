package inf112.skeleton.app.ui;

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
import inf112.skeleton.app.Main;
import inf112.skeleton.app.actor.Player;
import inf112.skeleton.app.game.RoboRally;
import inf112.skeleton.app.objects.Card;
import inf112.skeleton.app.sound.Sound;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class HandVisualizer extends InputAdapter implements Screen {


    private final int WIDTH = Main.cfg.width;
    private final int HEIGHT = Main.cfg.height;

    private final Texture confirm = new Texture(Gdx.files.internal("pictures/Confirm.png"));
    private final Texture powerDown = new Texture(Gdx.files.internal("pictures/PowerDown.png"));
    private Sprite lockInButton;
    private Sprite powerButton;

    private final Sound powerSound;

    private final SpriteBatch batch;
    private final BitmapFont font;

    private final Player player;

    //array of all card sprites
    private Sprite[] allSprites;

    private Pixmap cards;
    private Texture[] textures;

    public HandVisualizer(Player player) {
        this.player = player;

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.GREEN); //Number color
        font.getData().setScale(4, 3);

        createButtons();

        createTextures();

        powerSound = new Sound("assets/sound/MachinePowerOff.ogg");
    }

    /**
     * Create the texture for the cards and save them into the "textures" array.
     **/
    private void createCardTexture() {
        int lastLocked = player.getLocked().size() - 1;

        for (int i = 0; i < textures.length; i++) { //Create textures for the selectable hand
            if (i < player.getCards().length) {
                cardPictureFactory(player.getCards()[i]); //Sets .png file for the card
            } else { //Create textures for the locked cards
                int difference = i - player.getCards().length,
                        reverseLocked = lastLocked - difference;
                cardPictureFactory(player.getLocked().get(reverseLocked));
            }

            Pixmap resizedCards = new Pixmap(WIDTH / 10, 350, cards.getFormat());
            resizedCards.drawPixmap(cards,
                    0, 0, cards.getWidth(), cards.getHeight(),
                    0, 0, resizedCards.getWidth(), resizedCards.getHeight());
            textures[i] = new Texture(resizedCards);
            cards.dispose();
            resizedCards.dispose();
        }
    }

    /**
     * Sets the cards field to the address for the .png file of the card
     *
     * @param c Card to look up picture of
     */
    public void cardPictureFactory(Card c) {
        switch (c.getType()) {
            case (0):
                cards = new Pixmap(Gdx.files.internal("pictures/Move" + c.getMove() + ".png"));
                break;
            case (1):
                cards = new Pixmap(Gdx.files.internal("pictures/BackUp.png"));
                break;
            case (2):
                cards = new Pixmap(Gdx.files.internal("pictures/Turn" + c.getMove() + ".png"));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + c.toString());
        }
    }

    /**
     * Makes the buttons in a proper scale.
     */
    private void createButtons(){
        Pixmap buttons = new Pixmap(Gdx.files.internal("assets/pictures/button.png"));
        int width = WIDTH / 7;
        int height = (int) (width * (((float) buttons.getHeight())/(float) buttons.getWidth()));

        Pixmap resizedButton = new Pixmap(width, height, buttons.getFormat());
        resizedButton.drawPixmap(buttons,
                0, 0, buttons.getWidth(), buttons.getHeight(),
                0, 0, resizedButton.getWidth(), resizedButton.getHeight());
        Texture button = new Texture(resizedButton);

        lockInButton = new Sprite(button);
        lockInButton.setPosition(10, 360);

        powerButton = new Sprite(button);
        powerButton.setPosition(Display.getWidth() - powerButton.getWidth() - 10, 360);

        buttons.dispose();
        resizedButton.dispose();
    }


    /**
     * Create all the sprites on the board
     *
     * @param cards Array of textures that will be turned into sprites
     */
    private void createAllSprites(Texture[] cards) {
        allSprites = new Sprite[cards.length];
        for (int i = 0; i < allSprites.length; i++) {
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
        //toggle to display number of order above card.
        for (int i = 0; i < allSprites.length - player.getLocked().size(); i++) {
            if (allSprites[i].getBoundingRectangle().contains(screenX, HEIGHT - screenY)
                    && player.getSelected().size() <= player.cardsToSelect()) {

                if (player.getSelected().size() == player.cardsToSelect()
                        && !player.getSelected().contains(player.getCards()[i])) { //If trying to select more than max
                    return true;
                }
                player.toggleCard(player.getCards()[i]);
                return true;
            }
        }

        if (lockInButton.getBoundingRectangle().contains(screenX, HEIGHT - screenY)) {
            tryLockIn();
        }
        else if (powerButton.getBoundingRectangle().contains(screenX, HEIGHT - screenY)) {
            player.setAnnouncepowerdown();
            //Will add a better visual way to identify power down.
            setPowerDown();
        }
        return false;
    }

    /**
     * Sets all the cards to the correct color depending on whether locked or not
     */
    private void resetColors() {
        for (int i = 0; i < allSprites.length; i++) {
            if (i < player.getCards().length) {
                allSprites[i].setColor(Color.WHITE);
            } else {
                allSprites[i].setColor(Color.RED); //Locked cards are red
            }
        }
        if (player.getReady()) {
            lockInButton.setColor(Color.GREEN);
        } else {
            lockInButton.setColor(Color.WHITE);
            if(!player.playerPower) {
                powerButton.setColor(Color.WHITE);
            }
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
                arrowMove(player, move);
                break;
            case (Input.Keys.SPACE):
                arrowMove(player, 0);
                break;

            case (Input.Keys.C):
                tryLockIn();
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
            default:
                break;
        }
        return false;
    }

    /**
     * Toggles the ready signal if all the cards are selected
     */
    private void tryLockIn() {
        if (player.getSelected().size() == player.cardsToSelect()) {
            if (player.toggleReady()) {
                lockInButton.setColor(Color.GREEN);
            } else {
                lockInButton.setColor(Color.WHITE);
            }
        }
    }

    private void setPowerDown() {
        if(player.announcepowerdown) {
            powerButton.setColor(Color.GREEN);
            powerSound.play();
        }
        else {
            powerButton.setColor(Color.WHITE);
        }
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
        int playerHealth = player.getHealth();
        RoboRally.getBoard().doMove(player, move);
        RoboRally.getBoard().afterArrowMove(player);

        if (player.getHealth() != playerHealth) {
            textures = new Texture[player.getCards().length + player.getLocked().size()];
            createCardTexture();
            createAllSprites(textures);
            resetColors();
        }
    }

    @Override
    public void show() {
        //Must have this method because we implement Screen
    }

    @Override
    public void render(float v) {
        Gdx.input.setInputProcessor(this);

        batch.begin();

        if(!player.getReady()) {
            drawCardSprites();
            drawRegisterNumbers();
            drawPriorityNumbers();
        }
        drawButtons();

        float scale = 1.5f;
        batch.draw(confirm, lockInButton.getX() + lockInButton.getWidth() / 2f - confirm.getWidth() / (scale * 2f),
                lockInButton.getY() + lockInButton.getHeight() / 2f - confirm.getHeight() / (scale * 2f),
                confirm.getWidth() / scale, confirm.getHeight() / scale);
        scale = 1.75f;
        batch.draw(powerDown, powerButton.getX() + powerButton.getWidth() / 2f - powerDown.getWidth() / (scale * 2f),
                powerButton.getY() + powerButton.getHeight() / 2f - powerDown.getHeight() / (scale * 2f),
                powerDown.getWidth() / scale, powerDown.getHeight() / scale);
        batch.end();

    }

    private void drawPriorityNumbers() {
        for(int i = 0; i < player.getCards().length; i++) {
            font.getData().setScale(2.5f);
            font.draw(batch, Integer.toString(player.getCards()[i].getPriority()),
                    allSprites[i].getX() + allSprites[i].getWidth() / 3, 50);
        }
    }

    private void drawButtons() {
        lockInButton.draw(batch);
        lockInButton.setPosition(10, 360);
        powerButton.draw(batch);
        powerButton.setPosition(Display.getWidth() - powerButton.getWidth() - 10, 360);
    }

    /**
     * Draw the cards on the screen in the correct positions
     */
    public void drawCardSprites() {
        for (int i = 0; i < allSprites.length; i++) {
            int xPos = (int) (i * allSprites[i].getWidth());
            int yPos = 0;

            allSprites[i].draw(batch);
            allSprites[i].setPosition(xPos, yPos);
        }

    }

    /**
     * Draws the register number on top of the cards
     */
    public void drawRegisterNumbers() {
        font.getData().setScale(4, 3);
        float spriteWidth = allSprites[0].getWidth();
        for (int i = 0; i < player.getCards().length; i++) { //Draws register nr. on top of card
            allSprites[i].setColor(Color.WHITE);
            Card c = player.getCards()[i];
            if (player.getSelected().contains(c)) { //Whether current card is selected
                int nr = player.getSelected().indexOf(c) + 1; //Which register the card is in
                allSprites[i].setColor(Color.GREEN);
                font.draw(batch, Integer.toString(nr),
                        allSprites[i].getX() + spriteWidth / 2 - 10, allSprites[i].getHeight());
            }
        }

        int register = allSprites.length - 1;
        for (int i = 0; i < player.getLocked().size(); i++) { //Draw locked registers
            font.draw(batch, Integer.toString(5 - i),
                    allSprites[register - i].getX() + spriteWidth / 2 - 10, allSprites[register - i].getHeight());
        }
    }

    /**
     * Creates all the textures, will be called after every round
     */
    public void createTextures() {
        textures = new Texture[player.getCards().length + player.getLocked().size()];
        createCardTexture();
        createAllSprites(textures);
        resetColors();
    }

    @Override
    public void resize(int i, int i1) {
        //Must have this method because we implement Screen
    }

    @Override
    public void pause() {
        //Must have this method because we implement Screen
    }

    @Override
    public void resume() {
        //Must have this method because we implement Screen
    }

    @Override
    public void hide() {
        //Must have this method because we implement Screen
    }

    @Override
    public void dispose() {
        //Must have this method because we implement Screen
    }
}