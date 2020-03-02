package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lwjgl.opengl.Display;

public class HUD {

    private Board board;
    private Player player;
    int HEIGHT = Main.cfg.height;

    SpriteBatch batch = new SpriteBatch();
    BitmapFont font = new BitmapFont();
    Texture heart = new Texture(Gdx.files.internal("pictures/heart.png"));

    public HUD(Player player, Board board) {
        this.board = board;
        this.player = player;
        font.setColor(Color.RED);
        font.getData().setScale(2,2);
    }

    /**
     * renders a HUD statically on the top of the game window.
     */
    public void render() {
        batch.begin();
        int heartSize = Display.getWidth()/40;
        for(int i = 0; i < player.getHealth(); i++) {
            batch.draw(heart, (i*25), HEIGHT-40, heartSize, heartSize);
        }
        font.draw(batch, "Objective: " + player.getObjective(),275, HEIGHT-10);
        batch.end();
    }
}