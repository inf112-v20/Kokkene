package inf112.skeleton.app;

import com.badlogic.gdx.*;
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

    private OrthogonalTiledMapRenderer mapRenderer;

    private Player player;
    private PlayerState ps;

    private Music music;

    Deck deck;
    Card[] playerHand;

    RoboRally(String mapFile) {

        //Initializes the board and HUD
        board = new Board(mapFile, 0);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, board.boardWidth+2, board.boardHeight+2);
        camera.position.x = board.cardWidth/2f; //board.boardWidth/2f;
        camera.position.y = board.cardHeight/2f; //board.boardHeight/2f;
        camera.update();

        float unitScale = 1/300f;
        mapRenderer = new OrthogonalTiledMapRenderer(board.map, unitScale);
        mapRenderer.setView(camera);

        //Sets up one player and texture for testing purposes
        player = new Player("Test", 0, 0, 0);
        TextureRegion[][] tr = player.setPlayerTextures("assets/pictures/player.png");
        ps = new PlayerState(player, board, tr);

        //sets up the hud to display information about the player in real time.
        hud = new HUD(player,board);

        startMusic(); //starts the background music.

        //Makes deck and gives the initial set of cards. Will be moved once we implement rounds.
        try {
            deck = new Deck();
            deck.shuffle();
            playerHand = player.hand(deck);

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
        //Checks the keycode

        //temporary card, to work with the forwardmove.
        //Until cards are fully implemented, arrow keys will stay here.
        Card c = new Card(0,0,1);
        int move = c.getMove();

        switch (keycode) {
            case (Input.Keys.UP):
                player.setOrientation(0);
                board.forwardMove(player, move);
                break;
            case (Input.Keys.RIGHT):
                player.setOrientation(1);
                board.forwardMove(player, move);
                break;
            case (Input.Keys.DOWN):
                player.setOrientation(2);
                board.forwardMove(player, move);
                break;
            case (Input.Keys.LEFT):
                player.setOrientation(3);
                board.forwardMove(player, move);
                break;

            case (Input.Keys.M):
                music.muteToggle();
                break;
            case (Input.Keys.P):
                music.pauseToggle();
                break;
            case (Input.Keys.F11):
                fullscreenToggle();
                break;
            case (Input.Keys.Q):
                System.out.println("Quitting!");
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
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        board.playerLayer.setCell(player.getxPos(), player.getyPos(), ps.getPlayerStatus());
        mapRenderer.render();
        hud.render();

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
