package inf112.skeleton.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.lwjgl.opengl.Display;

public class ControlsTable {

    private final Table table = new Table();

    public ControlsTable() {
        int width = Display.getWidth(),
                height = Display.getHeight();
        Skin skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));

        Label empty = new Label(" ", skin);
        Label confirm = new Label("Confirm:", skin);
        Label c = new Label("C", skin);
        Label mute = new Label("Mute:", skin);
        Label m = new Label("M", skin);
        Label fullscreen = new Label("Fullscreen:", skin);
        Label f11 = new Label("F11", skin);
        Label escape = new Label("Escape to Menu:", skin);
        Label esc = new Label("ESC", skin);
        Label close = new Label("Close the game:", skin);
        Label q = new Label("Q", skin);

        Label text = new Label("Have fun and enjoy our game :)", skin);

        table.add(confirm);
        table.add(c).width(50);
        table.row();
        table.add(mute);
        table.add(m).width(50);
        table.row();
        table.add(fullscreen);
        table.add(f11).width(50);
        table.row();
        table.add(escape);
        table.add(esc).width(50);
        table.row();
        table.add(close);
        table.add(q).width(50);
        table.row();
        table.add(empty);
        table.row();
        table.add(text).colspan(2);

        table.setWidth(width/6f);
        table.setHeight(table.getMinHeight());
        table.setPosition(width-table.getWidth()*1.4f, height-table.getHeight()-table.getRowMinHeight(0));
    }

    public Table getTable() {
        return table;
    }

}
