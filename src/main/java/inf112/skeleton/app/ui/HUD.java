package inf112.skeleton.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.Main;
import inf112.skeleton.app.game.RoboRally;
import inf112.skeleton.app.actor.Player;
import org.lwjgl.opengl.Display;

public class HUD {

    private Player[] player;
    private int HEIGHT = Main.cfg.height;

    private SpriteBatch batch = new SpriteBatch();
    private BitmapFont font = new BitmapFont();
    private Texture heart = new Texture(Gdx.files.internal("pictures/heart.png"));

    public HUD(Player[] player) {
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
        for(int i = 0; i < player[0].getHealth(); i++) {
            batch.draw(heart, i*(heartSize/2f), HEIGHT-heartSize, heartSize, heartSize);
        }
        float obX = (heartSize*player[0].getMaxHealth() + heartSize)/2f;
        float obY = HEIGHT - heartSize/4f;
        if(RoboRally.getBoard().objectives == player[0].getObjective()-1) {
            font.getData().setScale(2f);
            font.draw(batch, "Objective: " + "Won!", obX, obY);
        }
        else {
            font.draw(batch, "Objective: " + player[0].getObjective(), obX, obY);
        }
        font.getData().setScale(1f);
        for(int i = 1; i < player.length; i++) {
            float nmX = (heartSize*player[i].getMaxHealth() + heartSize)/2f;
            float nmY = (HEIGHT-30)-(heartSize*i);
            for(int j = 0; j < player[i].getHealth(); j++) {
                font.draw(batch, player[i].getName(), (heartSize)/2f-10, nmY+heartSize/2f);
                batch.draw(heart, (j*(heartSize)/3f)+50, nmY, heartSize/1.5f, heartSize/1.5f);
            }
        }
        font.getData().setScale(2f);

        batch.end();
    }
}
