package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.util.Objects;

public class Menu implements Screen {

    private final Stage stage;
    private final SpriteBatch sb;
    private final Button gameButton;
    private final Button exitButton;
    private final SelectBox<String> selectMap;

    private String select;

    private SpriteBatch batch;

    private Texture roborally = new Texture(Gdx.files.internal("pictures/Roborally.png"));
    private Texture startGame = new Texture(Gdx.files.internal("pictures/StartGame.png"));
    private Texture exitGame = new Texture(Gdx.files.internal("pictures/ExitGame.png"));

    private int height = Gdx.graphics.getHeight();
    private int width = Gdx.graphics.getWidth();
    Game game;

    public Menu(Game game) {
        this.game = game;

        sb = new SpriteBatch();
        stage = new Stage();
        batch = new SpriteBatch();

        select = "12by12DizzyDash";

        //Start Game button
        gameButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("assets/pictures/button.png"))));
        gameButton.setPosition(width/2f - gameButton.getWidth()/2, height/2f);

        Skin skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));
        selectMap = new SelectBox<>(skin);
        selectMap.setItems(getMaps());
        selectMap.setWidth(gameButton.getWidth()*.87f);
        selectMap.setPosition(width/2f - selectMap.getWidth()/2, gameButton.getY() - selectMap.getHeight()*2);

        //Exit Game button
        exitButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("assets/pictures/button.png"))));
        exitButton.setPosition(width/2f - exitButton.getWidth()/2, selectMap.getY() - exitButton.getHeight() - selectMap.getHeight()*3);

        stage.addActor(gameButton);
        stage.addActor(selectMap);
        stage.addActor(exitButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        render(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.3f, .3f, .3f, 1); //background color DARK GREY
        //Gdx.gl.glClearColor(1, 1, 1, 1); //background color WHITE
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
        batch.begin();

        batch.draw(roborally, width/2f-roborally.getWidth()/2f, height*.75f, roborally.getWidth(), roborally.getHeight());
        batch.draw(startGame, width/2f-gameButton.getWidth()/2, gameButton.getY()+gameButton.getHeight()/3.5f, gameButton.getWidth(),
                gameButton.getHeight()*(gameButton.getWidth()/gameButton.getHeight()/4));
        batch.draw(exitGame, width/2f-exitButton.getWidth()/2, exitButton.getY()+exitButton.getHeight()/3.5f, exitButton.getWidth(),
                exitButton.getHeight()*(exitButton.getWidth()/exitButton.getHeight()/4));

        batch.end();

        if(gameButton.isPressed()){
            LoadGame();
        }
        if(exitButton.isPressed()) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        sb.dispose();
        stage.dispose();
    }

    public void LoadGame() {
        select = selectMap.getSelected();
        game.setScreen(new RoboRally("maps/" + select + ".tmx"));
    }

    public Array<String> getMaps(){
        Array<String> mapArray = new Array<>();
        File maps = new File("assets/maps");
        for (String m : Objects.requireNonNull(maps.list())){
            if (m.endsWith(".tmx")){
                mapArray.add(m.substring(0, m.length()-4));
            }
        }
        return mapArray;
    }

}