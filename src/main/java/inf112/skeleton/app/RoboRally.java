package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class RoboRally extends InputAdapter implements ApplicationListener {

    public int boardHeight;
    public int boardWidth;

    private TiledMapTileLayer boardLayer, playerLayer, holeLayer, flagLayer;

    private OrthogonalTiledMapRenderer mapRenderer;

    private Cell playerNormal, playerWon, playerDead;
    private Cell playerStatus;

    private Player player;

    @Override
    public void create() {

        TiledMap map = new TmxMapLoader().load("12by12DizzyDash.tmx");
        boardLayer = (TiledMapTileLayer) map.getLayers().get("Board");
        playerLayer = (TiledMapTileLayer) map.getLayers().get("Player");
        holeLayer = (TiledMapTileLayer) map.getLayers().get("Hole");
        flagLayer = (TiledMapTileLayer) map.getLayers().get("Flag");

        boardHeight = boardLayer.getHeight();
        boardWidth = boardLayer.getWidth();

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, boardWidth, boardHeight);
        camera.position.x = boardWidth/2f;
        camera.update();

        float unitScale = 1/300f;
        mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);
        mapRenderer.setView(camera);

        Texture playerTexture = new Texture("assets/player.png");
        TextureRegion[][] tr = TextureRegion.split(playerTexture, 300, 300);

        playerNormal = new Cell();
        playerDead = new Cell();
        playerWon = new Cell();
        playerNormal.setTile(new StaticTiledMapTile(tr[0][0]));
        playerDead.setTile(new StaticTiledMapTile(tr[0][1]));
        playerWon.setTile(new StaticTiledMapTile(tr[0][2]));

        playerStatus = playerNormal;

        Gdx.input.setInputProcessor(this);

        player = new Player("Test", 0,0, 1);

    }

    @Override
    public boolean keyUp(int keycode) {

        //Clears the current cell
        playerLayer.setCell(player.getxPos(), player.getyPos(), null);

        //Switch Case is another possible solution
        if(keycode == Input.Keys.LEFT){
            if(player.getxPos() <= 0) {
                //temporary fix for moving of board
                System.out.println("You cannot move in this direction");
                return false;
            }
            else{
                player.setxPos(player.getxPos()-1);
                return true;
            }
        }

        if(keycode == Input.Keys.RIGHT){
            if(player.getxPos() >= boardWidth-1) {
                System.out.println("You cannot move in this direction");
                return false;
            }
            else{
                player.setxPos(player.getxPos()+1);
                return true;
            }
        }

        if(keycode == Input.Keys.DOWN){
            if(player.getyPos() <= 0) {
                System.out.println("You cannot move in this direction");
                return false;
            }
            else{
                player.setyPos(player.getyPos()-1);
                return true;
            }
        }

        if(keycode == Input.Keys.UP){
            if(player.getyPos() >= boardHeight-1) {
                System.out.println("You cannot move in this direction");
                return false;
            }
            else{
                player.setyPos(player.getyPos()+1);
                return true;
            }
        }

        return false;

    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getPlayerStatus();

        playerLayer.setCell(player.getxPos(), player.getyPos(), playerStatus);
        mapRenderer.render();

    }

    public void getPlayerStatus() {

        playerStatus = playerNormal;

        if(holeLayer.getCell(player.getxPos(), player.getyPos()) != null)
            playerStatus = playerDead;
        else if(flagLayer.getCell(player.getxPos(), player.getyPos()) != null)
            playerStatus = playerWon;

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
}
