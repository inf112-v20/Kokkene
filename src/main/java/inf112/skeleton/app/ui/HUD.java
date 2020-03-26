package inf112.skeleton.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.Main;
import inf112.skeleton.app.objects.Board;
import inf112.skeleton.app.player.Player;
import org.lwjgl.opengl.Display;

public class HUD {

    private Board board;
    private Player player;
    private int HEIGHT = Main.cfg.height;
    private String[] dir = {"^", "<", "v", ">"};

    private SpriteBatch batch = new SpriteBatch();
    private BitmapFont font = new BitmapFont();
    private Texture heart = new Texture(Gdx.files.internal("pictures/heart.png"));

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
            batch.draw(heart, i*(heartSize/2f), HEIGHT-heartSize, heartSize, heartSize);
        }
        float obX = (heartSize*player.getMaxHealth() + heartSize)/2f;
        float obY = HEIGHT - heartSize/4f;
        if(board.objectives == player.getObjective()-1) {
            font.draw(batch, "Objective: " + "Won!", obX, obY);
        }
        else {
            font.draw(batch, "Objective: " + player.getObjective(), obX, obY);
        }

        // direction of which the laser will shoot
        int xMove = (Main.cfg.width/3) / 12;
        font.draw(batch, dir[player.getOrientation()], ((player.getxPos()*xMove) + Main.cfg.width/3) + xMove/3,
                ((player.getyPos()*xMove) + HEIGHT/2) - xMove*2);

        batch.end();
    }
}
