package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Main menu should have buttons to change options, start the game and quit the game.
 * If you choose multiplayer you should get a drop-down list to set whether a given player
 * should be human-controlled or an AI.
 * After starting the game a RoboRally object should be created and rendered.
 * After the game is finished we should return to the main menu.
 *
 * -Jan Kåre Sandblåst
 */
public class MainMenu implements ApplicationListener {

    private Options options;
    private SpriteBatch batch;
    private BitmapFont font;

    MainMenu(Options o){
        this.options = o;
    }

    // Exits the game
    public void quit(){
        System.exit(0);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        Button b = new Button();
        b.setLabel("Quit");
        b.setLocation(100, 200);
        font.draw(batch, "RoboRally", 500, 700);
        font.draw(batch, "Options", 100, 250);
        font.draw(batch, "Quit", 100, 200);
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
