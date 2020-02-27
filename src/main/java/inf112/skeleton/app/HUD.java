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
    int WIDTH = Main.cfg.width;
    int HEIGHT = Main.cfg.height;

    SpriteBatch batch = new SpriteBatch();
    BitmapFont font = new BitmapFont();
    Texture heart = new Texture(Gdx.files.internal("heart.png"));

    public HUD(Player player, Board board) {
        this.board = board;
        this.player = player;
        font.setColor(Color.RED);
        font.getData().setScale(2,2);
    }

    // renders a HUD staticly on the top of the game window.
    public void render() {
        batch.begin();
        int tileX = Display.getWidth()/board.cardWidth;
        int tileY = Display.getHeight()/board.cardHeight;
        int heartSize = Display.getWidth()/40;
        //font.draw(batch, "HP: " + player.getHealth(), WIDTH-100, HEIGHT-10);
        for(int i = 0; i < player.getHealth(); i++) {
            batch.draw(heart, (i*25), HEIGHT-40, heartSize,heartSize);
        }
        int lifeP = player.getLifePoints();
        for(int i = lifeP; 0 < i; i--) {
            batch.draw(heart, ((player.getxPos()+1)*tileX)-((tileX/(lifeP+1))*i)-heartSize/2, player.getyPos()*tileY+heartSize/2, heartSize, heartSize);
        }
        font.draw(batch, "Objective: " + player.getObjective(),275, HEIGHT-10);
        batch.end();
    }
}