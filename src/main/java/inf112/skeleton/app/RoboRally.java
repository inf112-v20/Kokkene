package inf112.skeleton.app;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.io.IOException;

public class RoboRally extends InputAdapter implements Screen {

    public static String mapFile;

    private Board board;
    private HUD hud;

    private OrthogonalTiledMapRenderer mapRenderer;

    private Player player;
    private PlayerState ps;

    private Music music;

    Deck deck;

    RoboRally(Game game) {

        //Initializes the board and HUD
        board = new Board(mapFile, 0);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, board.boardWidth, board.boardHeight);
        camera.position.x = board.boardWidth/2f;
        camera.update();

        float unitScale = 1/300f;
        mapRenderer = new OrthogonalTiledMapRenderer(board.map, unitScale);
        mapRenderer.setView(camera);

        //Sets up one player and texture for testing purposes
        player = new Player("Test", 0, 0, 0);
        TextureRegion[][] tr = player.setPlayerTextures("assets/player.png");
        ps = new PlayerState(player, board, tr);

        //sets up the hud to display information about the player in real time.
        hud = new HUD(player);

        startMusic(); //starts the background music.


        try {
            deck = new Deck();
            deck.shuffle();
            player.hand(deck);

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
            case (Input.Keys.Q):
                System.out.println("Quitting!");
                Gdx.app.exit();
                break;
        }

        //Currently movement is limited to the five first cards in hand, over and over.
        /*
        System.out.println(player.getOrientation());
        cardMove(player.playerHand[turn]);
        turn = (turn + 1)%5;
        */

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


    public void cardMove (Card card) {
        switch(card.getName()){
            //Forward
            case (0):
                board.forwardMove(player, card.getMove());
                break;
            //Backward
            case (1):
                board.backwardMove(player, card);
                break;
            //Turn
            case (2):
                player.turn(card.getMove());
                break;
        }
    }

    /**
     * Initialises the gameplay music
     */
    private void startMusic() {
        music = new Music();
        music.play();
    }
}
