package inf112.skeleton.app.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import inf112.skeleton.app.actor.Player;
import inf112.skeleton.app.objects.Board;
import inf112.skeleton.app.objects.Card;
import inf112.skeleton.app.sound.Music;
import inf112.skeleton.app.ui.HUD;
import inf112.skeleton.app.ui.HandVisualizer;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;

public class RoboRally extends InputAdapter implements Screen {
    private static Board board;
    private final HUD hud;

    private final OrthogonalTiledMapRenderer mapRenderer;

    public static Player player;

    int num = 0;

    private static Music music;

    private final Game game;

    private final HandVisualizer handVisualizer;

    private final ArrayList<ArrayList<Card>> phases = new ArrayList<>();

    private ArrayList<Card> nextPhase;

    private boolean builtPhases = false;

    RoboRally(Game game, String mapFile, String playerFile, String deckFile, int nrPlayers, int thisPlayer, int humanPlayers) {
        //Initializes the board and HUD
        this.game = game;
        setBoard(mapFile, playerFile, deckFile, nrPlayers, humanPlayers);

        int extraSpace = 8;
        int widthHeightDifference = board.boardWidth - board.boardHeight;
        float displayWidthHeightRatio = ((float) Display.getWidth()) / ((float) Display.getHeight());

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(
                false,
                (board.boardWidth+extraSpace-widthHeightDifference*2)*displayWidthHeightRatio,
                board.boardHeight+extraSpace);
        camera.position.x = board.boardWidth/2f;
        camera.position.y = board.boardHeight/2f - 3;
        camera.update();

        float unitScale = 1/300f;
        mapRenderer = new OrthogonalTiledMapRenderer(board.map, unitScale);
        mapRenderer.setView(camera);

        //Selects the player to show the hand of visually
        selectPlayer(thisPlayer);


        //sets up the hud to display information about the player in real time.
        hud = new HUD(player);

        //starts the background music.
        startMusic();

        handVisualizer = new HandVisualizer(player, board);
    }

    /**
     * Selects the given player and updates the player field
     */
    public static void selectPlayer(int multiplayerPosition) {
        player = board.getPlayers()[multiplayerPosition - 1];
    }

    /**
     * Selects the given board and updates the board field
     */
    private void setBoard(String mapFile, String playerFile, String deckFile, int nrPlayers, int humanPlayers) {
        board = new Board(this, mapFile, playerFile, deckFile, nrPlayers, humanPlayers);
    }

    /**
     * @return board of the current game
     */
    public static Board getBoard() {
        return board;
    }

    @Override
    public void show() {
        render(Gdx.graphics.getDeltaTime());
    }

    /**
     * Toggle all sounds
     */
    public static void muteToggle() {
        music.muteToggle();
        board.muteToggle();
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

        board.setPlayersOnBoard();

        mapRenderer.render();
        hud.render();
        handVisualizer.render(delta);

        if (checkReady()) {
            if (!builtPhases) {
                buildPhases();
                nextPhase = phases.remove(0);
            }
            nextPhase = doTurn(nextPhase);
        }

        board.nullPlayerBoard();

        checkFinished();
    }

    /**
     * Checks whether all players are ready to play their hand
     *
     * @return true if selected the correct number of cards and locked in
     */
    private boolean checkReady() {
        for (Player p : board.getPlayers()) {
            if (!p.getReady()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if all players are dead
     *
     * @return true if all are dead
     */
    private boolean allPlayersAreDead() {
        for (Player p : board.getPlayers()) {
            if (p.isAlive())
                return false;
        }
        return true;
    }

    /**
     * Checks if the game is finished and returns to Main Menu after rendering one final time
     */
    private void checkFinished() {
        if (allPlayersAreDead() || (player.getObjective() == board.objectives + 1 && board.objectives != 0)) {
            //Need to render one last time before going back to menu,
            // since it stops at the tile before the last objective if not.
            if (num >= 2) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                num = 0;
                dispose();
                game.setScreen(new Menu(game));
            }
            num++;
        }
    }

    /**
     * Creates lists of cards to be played in each phase
     */
    private void buildPhases() {
        for (int i = 0; i < 5; i++) {
            phases.add(board.sortPhase(i));
        }
        this.builtPhases = true;
    }

    /**
     * Does the turn one move at a time in the correct order such that it renders between each move
     *
     * @param currentPhase List of cards for the current phase listen by increasing priority
     * @return currentPhase after the first card has been used
     */
    private ArrayList<Card> doTurn(ArrayList<Card> currentPhase) {
        if (currentPhase.isEmpty()) {
            board.afterPhase();
            if (phases.isEmpty()) {
                board.afterRound();
                this.builtPhases = false;
                handVisualizer.createTextures();
                return currentPhase;
            }
            return phases.remove(0);
        }
        Card c = currentPhase.remove(0);
        if (c == null || c.getOwner() == null || c.getOwner().getHealth() <= 0) {
            return currentPhase;
        }
        board.cardMove(c, c.getOwner());
        return currentPhase;
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
