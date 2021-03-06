package inf112.skeleton.app.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import inf112.skeleton.app.Main;
import inf112.skeleton.app.actor.Hand;
import inf112.skeleton.app.actor.Player;
import inf112.skeleton.app.game.Menu;
import inf112.skeleton.app.game.RoboRally;
import inf112.skeleton.app.gameelements.Card;
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

    public Sprite yesButton;
    public Sprite noButton;

    private Sprite controlsButton;
    private final Table controls;

    private final Sound powerSound;

    private final Stage stage;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private final Player player;
    private final Hand hand;

    //array of all card sprites
    private Sprite[] allSprites;

    private Pixmap cards;
    private Texture[] textures;

    private final Game game;

    public HandVisualizer(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.hand = player.hand;

        stage = new Stage();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.GREEN); //Number color
        font.getData().setScale(4, 3);

        ControlsTable ct = new ControlsTable();
        controls = ct.getTable();
        stage.addActor(controls);
        controls.setVisible(false);

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
            if (i < hand.plHand.length) {
                cardPictureFactory(hand.plHand[i]); //Sets .png file for the card
            } else { //Create textures for the locked cards
                int difference = i - hand.plHand.length,
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
                cards = new Pixmap(Gdx.files.internal("pictures/Cards/Move" + c.getMove() + ".png"));
                break;
            case (1):
                cards = new Pixmap(Gdx.files.internal("pictures/Cards/BackUp.png"));
                break;
            case (2):
                cards = new Pixmap(Gdx.files.internal("pictures/Cards/Turn" + c.getMove() + ".png"));
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
        Texture button = makeButtonTexture(buttons, width);

        lockInButton = new Sprite(button);
        lockInButton.setPosition(10, 360);

        powerButton = new Sprite(button);
        powerButton.setPosition(Display.getWidth() - powerButton.getWidth() - 10, 360);

        yesButton = new Sprite(button);
        yesButton.setPosition(Display.getWidth() / 2f - powerButton.getWidth(), 0);

        noButton = new Sprite(button);
        noButton.setPosition(Display.getWidth() / 2f, 0);

        buttons = new Pixmap(Gdx.files.internal("assets/pictures/greybutton.png"));

        button = makeButtonTexture(buttons,width/3);
        controlsButton = new Sprite(button);
        controlsButton.setPosition(Gdx.graphics.getWidth()-button.getWidth()-button.getHeight(),
                Gdx.graphics.getHeight()-button.getHeight()*2);

        buttons.dispose();
    }

    private Texture makeButtonTexture(Pixmap buttons, int width) {
        int height = (int) (width * (((float) buttons.getHeight())/(float) buttons.getWidth()));

        Pixmap resizedButton = new Pixmap(width, height, buttons.getFormat());
        resizedButton.drawPixmap(buttons,
                0, 0, buttons.getWidth(), buttons.getHeight(),
                0, 0, resizedButton.getWidth(), resizedButton.getHeight());
        Texture button = new Texture(resizedButton);

        resizedButton.dispose();
        return button;
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
        int cardsToSelect = hand.cardsToSelect();
        for (int i = 0; i < allSprites.length - player.getLocked().size(); i++) {
            if (allSprites[i].getBoundingRectangle().contains(screenX, HEIGHT - screenY)
                    && player.getSelected().size() <= cardsToSelect) {

                if (player.getSelected().size() == cardsToSelect
                        && !player.getSelected().contains(hand.plHand[i])) { //If trying to select more than max
                    return true;
                }
                hand.toggleCard(hand.plHand[i]);
                return true;
            }
        }

        if (lockInButton.getBoundingRectangle().contains(screenX, HEIGHT - screenY)) {
            if (!player.getReady()){
                tryLockIn();
            }
        }
        else if (powerButton.getBoundingRectangle().contains(screenX, HEIGHT - screenY)) {
            if (!player.getReady() && !player.powerDownLastRound) {
                player.setAnnouncer();
                setPowerDown();
            }
        }
        else if (controlsButton.getBoundingRectangle().contains(screenX, HEIGHT - screenY)){
            controls.setVisible(!controls.isVisible());
        }

        if (yesButton.getBoundingRectangle().contains(screenX, HEIGHT - screenY)) {
            player.setAnnouncer();
            player.playerPower = true;
            player.powerDownLastRound = false;
            setPowerDown();
        }
        else if (noButton.getBoundingRectangle().contains(screenX, HEIGHT - screenY)) {
            player.playerPower = false;
            player.powerDownLastRound = false;
        }

        return false;
    }

    /**
     * Sets all the cards to the correct color depending on whether locked or not
     */
    private void resetColors() {
        for (int i = 0; i < allSprites.length; i++) {
            if (i < hand.plHand.length) {
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
        switch (keycode) {
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
                RoboRally.music.dispose();
                game.setScreen(new Menu(game));
                break;
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
        if (player.getSelected().size() == hand.cardsToSelect()) {
            if (player.toggleReady()) {
                lockInButton.setColor(Color.GREEN);
            } else {
                lockInButton.setColor(Color.WHITE);
            }
        }
    }

    /**
     * Is called when powerDown button is pressed, changes the color of the button. Plays sound if it's activating.
     */
    private void setPowerDown() {
        if (player.announcePowerDown) {
            powerButton.setColor(Color.GREEN);
            powerSound.play();
        } else {
            powerButton.setColor(Color.WHITE);
        }
    }

    /**
     * Toggles whether fullscreen or not
     */
    private void fullscreenToggle() {
        try {
            boolean full = Display.isFullscreen();
            Display.setFullscreen(!full);
            Display.setResizable(full);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        //Must have this method because we implement Screen
    }

    @Override
    public void render(float v) {
        Gdx.input.setInputProcessor(this);

        stage.act(v);
        stage.draw();
        batch.begin();

        //Only draws the cards if player hasn't locked in the cards yet.
        if(!player.powerDownLastRound && !player.getReady()) {
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

    /**
     * Draws the priority numbers on the cards
     */
    private void drawPriorityNumbers() {
        for (int i = 0; i < hand.plHand.length; i++) {
            font.getData().setScale(2.5f);
            font.draw(batch, Integer.toString(hand.plHand[i].getPriority()),
                    allSprites[i].getX() + allSprites[i].getWidth() / 3, 50);
        }
    }

    /**
     * Draws the buttons on the screen in the correct positions
     */
    private void drawButtons() {
        lockInButton.draw(batch);
        powerButton.draw(batch);
        controlsButton.draw(batch);
        BitmapFont font = new BitmapFont(Gdx.files.internal("assets/skins/default.fnt"));
        GlyphLayout gl = new GlyphLayout(font, "Controls");
        font.draw(batch, gl, controlsButton.getX()+controlsButton.getWidth()/2-gl.width/2,
                controlsButton.getY()+controlsButton.getHeight()-gl.height/2);

        if(player.powerDownLastRound) {
            drawPowerDownButtons();
        }

    }

    public void drawPowerDownButtons() {
        BitmapFont font = new BitmapFont(Gdx.files.internal("assets/skins/default.fnt"));
        //Draws text above the two buttons
        GlyphLayout gl = new GlyphLayout(font, "Do you want to keep staying powered down?");
        font.draw(batch, gl, (yesButton.getX()+noButton.getX())/2 + (yesButton.getWidth() - noButton.getWidth())/2 - 20,
                yesButton.getY() + (yesButton.getHeight()+gl.height + 20));

        //Draws the buttons
        yesButton.draw(batch);
        noButton.draw(batch);
        //Draws the font on top of the buttons.
        gl.reset();
        gl.setText(font, "Yes");
        font.draw(batch, gl, yesButton.getX()+ (yesButton.getWidth() - gl.width)/2 ,
                yesButton.getY() + (yesButton.getHeight()+gl.height)/2);

        gl.reset();
        gl.setText(font, "No");
        font.draw(batch, gl, noButton.getX()+ (noButton.getWidth() - gl.width)/2 ,
                noButton.getY() + (noButton.getHeight()+gl.height)/2);
    }

    /**
     * Draw the cards on the screen in the correct positions
     */
    public void drawCardSprites() {
        double oneHalf = allSprites[0].getWidth() / 2;

        for (int i = 0; i < allSprites.length; i++) {
            int xPos = (int) (i * allSprites[i].getWidth() + (oneHalf * (10-allSprites.length)));
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
        for (int i = 0; i < hand.plHand.length; i++) { //Draws register nr. on top of card
            allSprites[i].setColor(Color.WHITE);
            Card c = hand.plHand[i];
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
        textures = new Texture[hand.plHand.length + player.getLocked().size()];
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