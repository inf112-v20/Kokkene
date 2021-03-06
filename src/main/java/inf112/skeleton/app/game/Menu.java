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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.util.Tile;
import inf112.skeleton.app.util.Tiles;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Menu implements Screen {

    private final Stage stage;
    private final SpriteBatch sb;
    private final Button gameButton;
    private final Button exitButton;
    private final SelectBox<String> selectMap;
    private final SelectBox<String> selectNrPlayers;
    private final SelectBox<String> selectAIDifficulty;
    private final SelectBox<String> selectPlayerModels;

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

        selectMap = createSelectionBox(getMaps(), gameButton);
        scanMaps();
        selectNrPlayers = createSelectionBox(getPlayers(), selectMap);
        selectAIDifficulty = createSelectionBox(new Array<>(new String[]{"Easy", "Medium", "Hard", "Insane"}), selectNrPlayers);
        selectPlayerModels = createSelectionBox(getPlayerModels(), selectAIDifficulty);
        exitButton = createButton(selectPlayerModels);

        stage.addActor(gameButton);
        stage.addActor(selectMap);
        stage.addActor(selectNrPlayers);
        stage.addActor(selectAIDifficulty);
        stage.addActor(selectPlayerModels);
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
    private SelectBox<String> createSelectionBox(Array<String> items, Actor previous) {
        SelectBox<String> selectBox = new SelectBox<>(OptionsUtil.skin);
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

        checkSelectors();

        drawBatchTextures();

        drawSelectorText();

        showPlayer();

        if (gameButton.isPressed()) {
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
        gl.setText(font, "AI Difficulty:  ");
        fontDraw(font, gl, selectAIDifficulty);
        gl.setText(font, "Model Select:  ");
        fontDraw(font, gl, selectPlayerModels);
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
        Texture player = new Texture(Gdx.files.internal("assets/pictures/PlayerModels/" + selectPlayerModels.getSelected() + " Player.png"));
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        sb.dispose();
        stage.dispose();
        batch.dispose();
    }

    /**
     * Starts the game and saves the settings to the Options static class
     */
    public void loadGame() {
        OptionsUtil.mapFile = "assets/maps/" + selectMap.getSelected() + ".tmx";
        OptionsUtil.playerModelFile = "assets/pictures/PlayerModels/" + selectPlayerModels.getSelected() + " Player.png";
        OptionsUtil.deckFile = "assets/decks/deck1994.txt";
        OptionsUtil.humanPlayers = 1;
        OptionsUtil.thisPlayer = 1;
        OptionsUtil.nrPlayers = selectNrPlayers.getSelectedIndex() + 1;
        OptionsUtil.aiDifficulty = selectAIDifficulty.getSelectedIndex();

        dispose();
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
     * @return Array containg str number from 1 to number of spawnpoints on current map
     */
    public Array<String> getPlayers() {
        Array<String> nrPlayers = new Array<>();
        for (int i = 1; i <= Math.max(OptionsUtil.spawns.get(OptionsUtil.mapFile).size(), 1); i++) {
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
        File players = new File("assets/pictures/PlayerModels");
        for (String p : Objects.requireNonNull(players.list())) {
            if (p.contains("Player")) {
                playerArray.add(p.substring(0, p.lastIndexOf(" ")));
            }
        }
        return playerArray;
    }

    /**
     * Check selectors and update options based on the current selections
     */
    private void checkSelectors() {
        String selectedMap = "assets/maps/" + selectMap.getSelected() + ".tmx";
        if (!selectedMap.equals(OptionsUtil.mapFile)) {
            OptionsUtil.mapFile = selectedMap;
            selectNrPlayers.setItems(getPlayers());
        }
    }

    /**
     * Scan the maps and save the spawn coordinates to the Options Class
     */
    private void scanMaps() {
        TmxMapLoader loader = new TmxMapLoader();
        for (String mapName : getMaps()) {
            String map = "assets/maps/" + mapName + ".tmx";
            TiledMapTileLayer boardLayer = (TiledMapTileLayer) loader.load(map).getLayers().get("Board");

            OptionsUtil.spawns.put(map, Tile.findGroupMembers(boardLayer, Tiles.Group.SPAWNS));
        }
    }

    public static class OptionsUtil {

        public static Skin skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));

        public static String mapFile = "assets/maps/DizzyDash.tmx";

        public static HashMap<String, ArrayList<int[]>> spawns = new HashMap<>();

        public static String playerModelFile = "assets/pictures/PlayerModels/Owl Player.png";

        public static String deckFile;

        public static int nrPlayers = 2;

        public static int aiDifficulty = 0;

        public static int thisPlayer = 1;

        public static int humanPlayers = 1;
    }

}
