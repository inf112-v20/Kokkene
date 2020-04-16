package inf112.skeleton.app.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.util.Objects;

public class Menu implements Screen {

    private final Stage stage;
    private final SpriteBatch sb;
    private final Button gameButton;
    private final Button exitButton;
    private final SelectBox<String> selectMap;
    private final SelectBox<String> selectPlayerModels;
    private final SelectBox<String> selectDeck;
    private final SelectBox<String> selectNrPlayers;

    private final SpriteBatch batch;

    private final Texture roboRally = new Texture(Gdx.files.internal("pictures/Roborally.png"));
    private final Texture startGame = new Texture(Gdx.files.internal("pictures/StartGame.png"));
    private final Texture exitGame = new Texture(Gdx.files.internal("pictures/ExitGame.png"));

    private final int height = Gdx.graphics.getHeight();
    private final int width = Gdx.graphics.getWidth();
    private final Game game;

    public Menu(Game game) {
        this.game = game;

        sb = new SpriteBatch();
        stage = new Stage();
        batch = new SpriteBatch();

        gameButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("assets/pictures/button.png"))));
        gameButton.setPosition(width / 2f - gameButton.getWidth() / 2, height / 2f);

        selectMap = createSelection(getMaps(), gameButton);
        selectNrPlayers = createSelection(getPlayers(), selectMap);
        selectPlayerModels = createSelection(getPlayerModels(), selectNrPlayers);
        selectDeck = createSelection(getDecks(), selectPlayerModels);
        exitButton = createButton(selectDeck);

        stage.addActor(gameButton);
        stage.addActor(selectMap);
        stage.addActor(selectNrPlayers);
        stage.addActor(selectPlayerModels);
        stage.addActor(selectDeck);
        stage.addActor(exitButton);
    }

    /**
     * Creates a button underneath the actor given as previous
     *
     * @param previous Actor this button is supposed to be underneath
     * @return the newly created button with a position right underneath the previous actor
     */
    private Button createButton(Actor previous) {
        Button button = new Button(new TextureRegionDrawable(new TextureRegion(
                new Texture("assets/pictures/button.png"))));
        button.setPosition(width / 2f - button.getWidth() / 2,
                previous.getY() - button.getHeight() - previous.getHeight());
        return button;
    }

    /**
     * Creates a SelectBox<String> with the listed items underneath the previous actor
     *
     * @param items    Array of strings where each item can be selected
     * @param previous Previous actor drawn, this new SelectBox will be placed underneath it
     * @return the newly created SelectBox
     */
    private SelectBox<String> createSelection(Array<String> items, Actor previous) {
        SelectBox<String> selectBox = new SelectBox<>(Options.skin);
        selectBox.setItems(items);
        selectBox.setWidth(gameButton.getWidth() * .87f);
        selectBox.setPosition(width / 2f - selectBox.getWidth() / 2,
                previous.getY() - selectBox.getHeight() * 1.5f);
        return selectBox;
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.3f, .3f, .3f, 1); //background color DARK GREY
        //Gdx.gl.glClearColor(1, 1, 1, 1); //background color WHITE
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        drawBatchTextures();

        drawSelectorText();

        showPlayer();

        if(gameButton.isPressed()){
            loadGame();
        }
        if(exitButton.isPressed()) {
            Gdx.app.exit();
        }
    }

    /**
     * Draw the title and the button pictures
     */
    private void drawBatchTextures() {
        batch.begin();
        batch.draw(roboRally, width / 2f - roboRally.getWidth() / 2f,
                height * .75f, roboRally.getWidth(), roboRally.getHeight());
        batchDrawButton(startGame, gameButton);
        batchDrawButton(exitGame, exitButton);
        batch.end();
    }

    /**
     * Draw the texture centered on the button
     *
     * @param tex    texture to draw
     * @param button button to drawn in the center of
     */
    public void batchDrawButton(Texture tex, Actor button) {
        batch.draw(tex,
                width / 2f - button.getWidth() / 2,
                button.getY() + button.getHeight() / 4f, button.getWidth(),
                button.getHeight() * (button.getWidth() / button.getHeight() / 4));
    }

    /**
     * Draws all the text to the left of the selector boxes
     */
    private void drawSelectorText() {
        BitmapFont font = new BitmapFont(Gdx.files.internal("assets/skins/default.fnt"));
        GlyphLayout gl = new GlyphLayout(font, "Map Select:  ");
        batch.begin();
        fontDraw(font, gl, selectMap);
        gl.setText(font, "Player Select:  ");
        fontDraw(font, gl, selectNrPlayers);
        gl.setText(font, "Model Select:  ");
        fontDraw(font, gl, selectPlayerModels);
        gl.setText(font, "Deck Select:  ");
        fontDraw(font, gl, selectDeck);
        batch.end();
    }

    /**
     * Draws text to the left of given SelectBox
     *
     * @param font font to use to draw
     * @param gl   glyphlayout containing text to draw
     * @param sel  SelectBox to draw to the left of
     */
    public void fontDraw(BitmapFont font, GlyphLayout gl, SelectBox<String> sel) {
        font.draw(batch, gl, sel.getX() - gl.width, sel.getY() + (sel.getHeight() + gl.height) / 2);
    }

    /**
     * Show what the player looks like
     */
    private void showPlayer() {
        Texture player = new Texture(Gdx.files.internal("assets/pictures/" + selectPlayerModels.getSelected() + " Player.png"));
        TextureRegion standardPlayer = new TextureRegion(player, 0, 0, 300, 300);
        float scale = 0.5f;

        batch.begin();
        batch.draw(standardPlayer, selectPlayerModels.getX() + selectPlayerModels.getWidth(),
                selectPlayerModels.getY() + selectPlayerModels.getHeight() / 2 - (standardPlayer.getRegionHeight() / 2f) * scale,
                300 * scale, 300 * scale);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        sb.dispose();
        stage.dispose();
    }

    /**
     * Starts the game and saves the settings to the Options static class
     */
    public void loadGame() {
        Options.mapFile = "assets/maps/" + selectMap.getSelected() + ".tmx";
        Options.playerModelFile = "assets/pictures/" + selectPlayerModels.getSelected() + " Player.png";
        Options.deckFile = "assets/decks/" + selectDeck.getSelected() + ".txt";
        Options.humanPlayers = 1;
        Options.thisPlayer = 1;
        Options.nrPlayers = Integer.parseInt(selectNrPlayers.getSelected().substring(0, 1));

        game.setScreen(new RoboRally(game));
    }

    /**
     * Gets an array containing all the maps found in the assets folder
     *
     * @return an array containing all maps located in assets/maps
     */
    public Array<String> getMaps() {
        Array<String> mapArray = new Array<>();
        File maps = new File("assets/maps");
        for (String m : Objects.requireNonNull(maps.list())) {
            if (m.endsWith(".tmx")) {
                mapArray.add(m.substring(0, m.length() - 4));
            }
        }
        return mapArray;
    }

    /**
     * Creates an Array of numbers as string
     *
     * @return Array containg str number from 1 to 8
     */
    public Array<String> getPlayers() {
        Array<String> nrPlayers = new Array<>();
        for (int i = 1; i <= 8; i++) {
            nrPlayers.add(i + " Players");
        }
        return nrPlayers;
    }

    /**
     * Get all player models as an array
     *
     * @return An array containing all the player models found in assets/pictures
     */
    public Array<String> getPlayerModels() {
        Array<String> playerArray = new Array<>();
        File players = new File("assets/pictures");
        for (String p : Objects.requireNonNull(players.list())) {
            if (p.contains("Player")) {
                playerArray.add(p.substring(0, p.lastIndexOf(" ")));
            }
        }
        return playerArray;
    }

    /**
     * Checks the assets and creates an array with all the decks
     *
     * @return An array containing all decks found in assets/decks
     */
    public Array<String> getDecks() {
        Array<String> deckArray = new Array<>();

        File deck = new File("assets/decks");
        for (String d : Objects.requireNonNull(deck.list())) {
            deckArray.add(d.substring(0, d.length() - 4));
        }
        return deckArray;
    }

    public static class Options {

        public static Skin skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));

        public static String mapFile = "assets/maps/12by12DizzyDash.tmx";

        public static String playerModelFile = "assets/pictures/Owl Player.png";

        public static String deckFile;

        public static int nrPlayers = 2;

        public static int thisPlayer = 1;

        public static int humanPlayers = 1;
    }


}
