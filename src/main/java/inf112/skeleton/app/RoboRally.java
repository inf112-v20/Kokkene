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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class RoboRally extends InputAdapter implements ApplicationListener {

    String mapFile = "fiveTiles.tmx";

    Board board;
    HUD hud;

    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;

    Player player;
    PlayerState ps;

    Music music;

    @Override
    public void create() {

        //Initializes the board and HUD
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

        //sets up the hud to display information about the player in real time.
        hud = new HUD(player);

        Gdx.input.setInputProcessor(this);

        startMusic(); //starts the background music.

        //This is ugly
        try {
            createDeck();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
                System.exit(0);
                break;
        }
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

    //This is just a test, will probably not be in final product, it only prints out numbers extr<cted from txt file
    private void createDeck() throws FileNotFoundException {
        int priority, name, move;
        int indexNext = 0;
        Scanner Scn = new Scanner(new File("assets/test.txt"));
        while (Scn.hasNextLine()) {
            String data = Scn.nextLine();

            priority = Integer.parseInt(data.substring(0, data.indexOf("P")));
            indexNext = data.indexOf("P") + 1;

            name = Integer.parseInt(data.substring(indexNext, data.indexOf("N")));
            indexNext = data.indexOf("N") + 1;

            move = Integer.parseInt(data.substring(indexNext, data.indexOf("M")));
            //Here we just take and create the card with the values we extracted
            System.out.println(priority + " " + name + " "+ move);
        }
    }
}

