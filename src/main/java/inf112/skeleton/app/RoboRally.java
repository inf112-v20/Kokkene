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
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class RoboRally extends InputAdapter implements ApplicationListener {

    TiledMap map;
    TiledMapTileLayer boardLayer, playerLayer, holeLayer, flagLayer, wallLayer;

    int boardHeight;
    int boardWidth;

    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;

    Texture playerTexture;
    TextureRegion[][] tr;
    Cell playerNorm, playerWon, playerDead;
    Cell playerStatus;

    Player player;

    @Override
    public void create() {

        map = new TmxMapLoader().load("12by12DizzyDash.tmx");
        boardLayer = (TiledMapTileLayer) map.getLayers().get("Board");
        wallLayer = (TiledMapTileLayer) map.getLayers().get("Wall");
        playerLayer = (TiledMapTileLayer) map.getLayers().get("Player");
        holeLayer = (TiledMapTileLayer) map.getLayers().get("Hole");
        flagLayer = (TiledMapTileLayer) map.getLayers().get("Flag");

        boardHeight = boardLayer.getHeight();
        boardWidth = boardLayer.getWidth();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, boardWidth, boardHeight);
        camera.position.x = boardWidth/2f;
        camera.update();

        float unitScale = 1/300f;
        mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);
        mapRenderer.setView(camera);

        playerTexture = new Texture("assets/player.png");
        tr = TextureRegion.split(playerTexture, 300, 300);

        playerNorm = new Cell();
        playerNorm.setTile(new StaticTiledMapTile(tr[0][0]));

        playerDead = new Cell();
        playerDead.setTile(new StaticTiledMapTile(tr[0][1]));

        playerWon = new Cell();
        playerWon.setTile(new StaticTiledMapTile(tr[0][2]));

        playerStatus = new Cell();
        playerStatus = playerNorm;

        Gdx.input.setInputProcessor(this);
        player = new Player("Test", 0,0, 1);

    }

    @Override
    public boolean keyUp(int keycode) {

        //Sets the current cell with the player to null.
        playerLayer.setCell(player.getxPos(), player.getyPos(), null);

        int wall = 0;
        if (wallLayer.getCell(player.getxPos(), player.getyPos()) != null){
            wall =  wallLayer.getCell(player.getxPos(), player.getyPos()).getTile().getId();
        }

        if(keycode == Input.Keys.LEFT){
            if(player.getxPos() <= 0 || wall == 27) {
                System.out.println("You cannot move in this direction");
                return false;
            }
            else{
                player.setxPos(player.getxPos()-1);
                return true;
            }
        }

        if(keycode == Input.Keys.RIGHT){
            if(player.getxPos() >= boardWidth-1 || wall == 21) {
                System.out.println("You cannot move in this direction");
                return false;
            }
            else{
                player.setxPos(player.getxPos()+1);
                return true;
            }
        }

        if(keycode == Input.Keys.DOWN){
            if(player.getyPos() <= 0 || wall == 26) {
                System.out.println("You cannot move in this direction");
                return false;
            }
            else{
                player.setyPos(player.getyPos()-1);
                return true;
            }
        }

        if(keycode == Input.Keys.UP){
            if(player.getyPos() >= boardHeight-1 || wall == 28) {
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
        map.dispose();
        mapRenderer.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Returns
        getPlayerStatus();

        playerLayer.setCell(player.getxPos(), player.getyPos(), playerStatus);
        mapRenderer.render();

    }

    public void getPlayerStatus() {

        playerStatus = playerNorm;

        if(holeLayer.getCell(player.getxPos(), player.getyPos()) != null) {
            playerStatus = playerDead;
        }
        else if (flagLayer.getCell(player.getxPos(), player.getyPos()) != null) {
            playerStatus = playerWon;
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
}
