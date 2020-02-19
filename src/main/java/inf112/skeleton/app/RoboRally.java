package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class RoboRally extends InputAdapter implements ApplicationListener {

    String mapFile = "fiveTiles.tmx";

    Board board;

    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;

    Player player;
    PlayerState ps;

    Music music;

    @Override
    public void create() {

        //Initializes the board
        board = new Board(mapFile);

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

        Gdx.input.setInputProcessor(this);

        startMusic(); //starts the background music.

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
                break;
            case (Input.Keys.RIGHT):
                player.setOrientation(1);
                break;
            case (Input.Keys.DOWN):
                player.setOrientation(2);
                break;
            case (Input.Keys.LEFT):
                player.setOrientation(3);
                break;
            case (Input.Keys.M):
                music.muteToggle();
                break;
            case (Input.Keys.P):
                music.pauseToggle();
                break;
        }
        //Moves the player on the board according to their orientation
        board.move(player);
        return false;
    }


    @Override
    public void dispose(){
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        board.playerLayer.setCell(player.getxPos(), player.getyPos(), ps.getPlayerStatus());
        mapRenderer.render();

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

    /**
     * Initialises the gameplay music
     */
    private void startMusic() {

        try {
            music = new Music();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
}
