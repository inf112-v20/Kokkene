package inf112.skeleton.app.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import inf112.skeleton.app.HUD;
import inf112.skeleton.app.ShowDeck;
import inf112.skeleton.app.objects.Board;
import inf112.skeleton.app.player.Player;
import inf112.skeleton.app.player.PlayerState;
import inf112.skeleton.app.sound.Music;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class RoboRally extends InputAdapter implements Screen {
    private static Board board;
    private HUD hud;

    private OrthogonalTiledMapRenderer mapRenderer;

    public static Player player;
    private PlayerState ps;
    private PlayerState hb;

    private static Music music;

    private Game game;

    private int num = 0;

    ShowDeck showDeck;

    RoboRally(Game game, String mapFile, String playerFile) {
        //Initializes the board and HUD
        this.game = game;
        setBoard(mapFile);

        int extraSpace = 8;
        float displayWidthHeightRatio = ((float) Display.getWidth()) / ((float) Display.getHeight());

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(
                false,
                (board.boardWidth+extraSpace)*displayWidthHeightRatio,
                board.boardHeight+extraSpace);
        camera.position.x = board.boardWidth/2f;
        camera.position.y = board.boardHeight/2f - 3;
        camera.update();

        float unitScale = 1/300f;
        mapRenderer = new OrthogonalTiledMapRenderer(board.map, unitScale);
        mapRenderer.setView(camera);

        //Sets up one player and texture for testing purposes
        selectPlayer(1);
        TextureRegion[][] tr = player.setPlayerTextures(playerFile);
        ps = new PlayerState(player, board, tr);

        TextureRegion[][] healthbars = player.setPlayerTextures("assets/pictures/healthbars.png");
        hb = new PlayerState(player, board, healthbars);

        //sets up the hud to display information about the player in real time.
        hud = new HUD(player,board);

        //starts the background music.
        startMusic();

        showDeck = new ShowDeck(player, board);
    }

    //Selects the given player and updates the player field
    public static void selectPlayer(int multiplayerPosition) {player = board.players[multiplayerPosition-1];}

    //Selects the given board and updates the board field
    private static void setBoard(String mapFile) {board = new Board(mapFile, 1);}

    public static Board getBoard() {
        return board;
    }

    @Override
    public void show() {
        render(0);
    }

    public static void muteToggle() {
        music.muteToggle();
        board.muteToggle();
    }

    public static void fullscreenToggle() {
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
        music.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.3f, .3f, .3f, 1); //background color DARK GREY
        //Gdx.gl.glClearColor(1, 1, 1, 1); //background color WHITE
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int x = player.getxPos(),
                y = player.getyPos();

        board.playerLayer.setCell(x, y, ps.getPlayerStatus());
        board.playerLayer.getCell(x, y).setRotation(player.getOrientation());

        board.healthLayer.setCell(x, y, hb.getPlayerHealth());
        try {
            board.healthLayer.getCell(x, y).setRotation(player.getOrientation()); }
        catch (NullPointerException ignored) {}

        mapRenderer.render();
        hud.render();
        showDeck.render(delta);

        if(player.getObjective() == board.objectives+1 && board.objectives != 0) {
            //Need to render one last time before going back to menu,
            // since it stops at the tile before the last objective if not.
            if (num == 1) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dispose();
                game.setScreen(new Menu(game));
            }
            num = 1;
        }
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