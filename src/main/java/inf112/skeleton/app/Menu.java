package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
    //private final SelectBox<String> selectMap;

    private String select;

    private GlyphLayout glyphLayout;
    private BitmapFont font, titleFont;
    private SpriteBatch batch;

    private int height = Gdx.graphics.getHeight();
    private int width = Gdx.graphics.getWidth();
    Game game;

    public Menu(Game game) {
        this.game = game;

        sb = new SpriteBatch();
        stage = new Stage();
        batch = new SpriteBatch();
        glyphLayout = new GlyphLayout();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(4);
        titleFont = new BitmapFont();
        titleFont.setColor(Color.RED);
        titleFont.getData().setScale(6);

        select = "12by12DizzyDash";

        //Start Game button
        gameButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("assets/pictures/button.png"))));
        gameButton.setPosition(width/2f - gameButton.getWidth()/2, height/2f);


        /*
        selectMap = new SelectBox<>(skin);
        selectMap.setItems("12by12DizzyDash", "fiveTiles", "testConveyors", "testMostElements");
        selectMap.setPosition(width/2f - selectMap.getWidth()/2, gameButton.getY() - selectMap.getHeight());
         */

        //Exit Game button
        exitButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("assets/pictures/button.png"))));
        exitButton.setPosition(width/2f - exitButton.getWidth()/2, gameButton.getY() - 2*exitButton.getHeight());

        stage.addActor(gameButton);
        //stage.addActor(selectMap);
        stage.addActor(exitButton);
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

        stage.act(delta);
        stage.draw();
        batch.begin();

        glyphLayout.setText(titleFont, "RoboRally");
        titleFont.draw(batch, glyphLayout, width/2f - glyphLayout.width/2,
                height - 1.5f*glyphLayout.height);

        //draw 'Start Game' text on gameButton
        glyphLayout.setText(font, "Start Game");
        font.draw(batch, glyphLayout, gameButton.getX() + (gameButton.getWidth() - glyphLayout.width)/2,
                gameButton.getY() + (gameButton.getHeight() + glyphLayout.height)/2);

        //draw 'Exit Game' text on exitButton
        glyphLayout.setText(font, "Exit Game");
        font.draw(batch, glyphLayout, exitButton.getX() + (exitButton.getWidth() - glyphLayout.width)/2,
                exitButton.getY() + (exitButton.getHeight() + glyphLayout.height)/2);

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
        game.setScreen(new RoboRally("maps/" + select + ".tmx"));
    }

}