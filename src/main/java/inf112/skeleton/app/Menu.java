package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Menu implements Screen {

    private final Stage stage;
    private final SpriteBatch sb;

    private final Button gameButton;
    private final Button exitButton;

    public static String mapFile;

    private BitmapFont font;
    private SpriteBatch batch;

    private int height = Gdx.graphics.getHeight();
    private int width = Gdx.graphics.getWidth();

    //private Texture background;

    Game game;

    public Menu(Game game) {
        this.game = game;

        sb = new SpriteBatch();
        stage = new Stage();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(4);


        //Start Game button
        gameButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("assets/pictures/button.png"))));
        gameButton.setPosition((width /2f)-(gameButton.getWidth()/2), (height/2f)-(gameButton.getHeight()/2)*2);

        //Exit Game button
        exitButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("assets/pictures/button.png"))));
        exitButton.setPosition((width /2f)-(gameButton.getWidth()/2), (height/2f)-(gameButton.getHeight()/2)*4);

        stage.addActor(gameButton);
        stage.addActor(exitButton);

        //Change background
        //background = new Texture("");

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        render(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /*
         * sb.begin();
         * sb.draw(background, 0, 0, width, height);
         * sb.end();
         */

        stage.act(delta);
        stage.draw();
        batch.begin();

        //draw start game title on gamebutton
        font.draw(batch, "Start Game!", (width/2f)-(gameButton.getWidth()/2)+47,
                (height /2f)-(gameButton.getHeight()/2)+27);

        //draw exit title on button
        font.draw(batch, "Exit game!", (width/2f)-(exitButton.getWidth()/2) + 70,
                (height/4f)-10);

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
        game.setScreen(new RoboRally(mapFile));
    }

}
