package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HUD {

    Player player;
    int WIDTH = Main.cfg.width;
    int HEIGHT = Main.cfg.height;

    SpriteBatch batch = new SpriteBatch();
    BitmapFont font = new BitmapFont();
    Texture heart = new Texture(Gdx.files.internal("heart.png"));

    public HUD(Player player) {
        this.player = player;
        font.setColor(Color.RED);
        font.getData().setScale(2,2);


    }

    // renders a HUD staticly on the top of the game window.
    public void render() {
        batch.begin();
        //font.draw(batch, "HP: " + Integer.toString(player.getHealth()), WIDTH-100, HEIGHT-10);
        for(int i = 0; i < player.getHealth(); i++) {
            batch.draw(heart, (i*25), HEIGHT-40, 45,45);
        }
        batch.end();
    }
}