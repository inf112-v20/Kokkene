package inf112.skeleton.app;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import javax.swing.*;
import java.io.IOException;

public class RoboRally extends InputAdapter implements Screen {

    public static String mapFile = "fiveTiles.tmx";
    private SpriteBatch batch;

    Game game;

    Board board;
    HUD hud;

    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;

    Player player;
    PlayerState ps;

    Music music;

    RoboRally(Game game) {
        this.game = game;

        batch = new SpriteBatch();

        //Initializes the board and HUD
        board = new Board(mapFile, 0);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, board.boardWidth, board.boardHeight);
        camera.position.x = board.boardWidth/2f;
        camera.update();

        float unitScale = 1/300f;
        mapRenderer = new OrthogonalTiledMapRenderer(board.map, unitScale);
        mapRenderer.setView(camera);

        //Sets up one player and texture for testing purposes
        player = new Player("Test",0,0, 0);
        TextureRegion[][] tr = player.setPlayerTextures("assets/player.png");
        ps = new PlayerState(player, board, tr);

        //sets up the hud to display information about the player in real time.
        hud = new HUD(player);

        startMusic(); //starts the background music.

        //Cant find a way to get rid of these things
        try {
            Deck deck = new Deck();
            deck.print();
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
        //Clears the current cell of the player
        //Also stays in
        board.playerLayer.setCell(player.getxPos(), player.getyPos(), null);

        //Checks the keycode of the key pressed and sets the orientation of the player
        switch (keycode) {
            case (Input.Keys.UP):
                player.setOrientation(0);
                board.move(player);
                break;
            case (Input.Keys.RIGHT):
                player.setOrientation(1);
                board.move(player);
                break;
            case (Input.Keys.DOWN):
                player.setOrientation(2);
                board.move(player);
                break;
            case (Input.Keys.LEFT):
                player.setOrientation(3);
                board.move(player);
                break;

            case (Input.Keys.M):
                music.muteToggle();
                break;
            case (Input.Keys.P):
                music.pauseToggle();
                break;

            case (Input.Keys.Q):
                System.out.println("Quitting!");
                Gdx.app.exit();
                break;
        }
        return false;
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
