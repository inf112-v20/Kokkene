package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import java.io.IOException;

public class RoboRally extends InputAdapter implements Screen {
    private Board board;
    private HUD hud;
    private ShowDeck showDeck;

    private OrthogonalTiledMapRenderer mapRenderer;

    private Player player;
    private PlayerState ps;
    private PlayerState hb;

    private Music music;

    Deck deck;
    Card[] playerHand;

    RoboRally(String mapFile) {
        //Initializes the board and HUD
        board = new Board(mapFile, 0);

        int extraSpace = 8;
        float displayWidthHeightRatio = ((float) Display.getWidth()) / ((float) Display.getHeight());

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(
                false,
                board.boardWidth+extraSpace*displayWidthHeightRatio,
                board.boardHeight+extraSpace);
        camera.position.x = board.boardWidth/2f;
        camera.position.y = board.boardHeight/2f - 3;
        camera.update();

        float unitScale = 1/300f;
        mapRenderer = new OrthogonalTiledMapRenderer(board.map, unitScale);
        mapRenderer.setView(camera);

        //Sets up one player and texture for testing purposes
        player = new Player("Test", 0, 0, 0);
        TextureRegion[][] tr = player.setPlayerTextures("assets/pictures/player.png");
        ps = new PlayerState(player, board, tr);

        TextureRegion[][] healthbars = player.setPlayerTextures("assets/pictures/healthbars.png");
        hb = new PlayerState(player, board, healthbars);

        //sets up the hud to display information about the player in real time.
        hud = new HUD(player,board);


        //starts the background music.
        startMusic();

        //Makes deck and gives the initial set of cards. Will be moved once we implement rounds.
        try {
            deck = new Deck();
            deck.shuffle();
            player.setHand(deck);
            showDeck = new ShowDeck(player);
            playerHand = player.getCards();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        render(0);
    }

    @Override
    public boolean keyUp(int keycode) {
        /*temporary card, to work with the forwardmove.
        Until cards are fully implemented, arrow keys will stay here.*/
        Card c = new Card(0,0,1);
        int move = c.getMove();

        switch (keycode) {
            case (Input.Keys.W):
            case (Input.Keys.UP):
                player.setOrientation(0);
                board.forwardMove(player, move);
                break;
            case (Input.Keys.A):
            case (Input.Keys.LEFT):
                player.setOrientation(1);
                board.forwardMove(player, move);
                break;
            case (Input.Keys.S):
            case (Input.Keys.DOWN):
                player.setOrientation(2);
                board.forwardMove(player, move);
                break;
            case (Input.Keys.D):
            case (Input.Keys.RIGHT):
                player.setOrientation(3);
                board.forwardMove(player, move);
                break;

            case (Input.Keys.M):
                music.muteToggle();
                player.muteToggle();
                break;
            case (Input.Keys.P):
                music.pauseToggle();
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



    @Override
    public void dispose(){
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.3f, .3f, .3f, 1); //background color DARK GREY
        //Gdx.gl.glClearColor(1, 1, 1, 1); //background color WHITE
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int x = player.getxPos(), y = player.getyPos();

        board.playerLayer.setCell(x, y, ps.getPlayerStatus());
        board.playerLayer.getCell(x, y).setRotation(player.getOrientation());

        board.healthLayer.setCell(x, y, hb.getPlayerHealth());
        board.healthLayer.getCell(x, y).setRotation(player.getOrientation());

        mapRenderer.render();
        hud.render();
        showDeck.render();
    }

    @Override
    public void resize(int width, int height) {
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

    /**
     * Initialises the gameplay music
     */
    private void startMusic() {
        music = new Music();
        music.play();
    }
}