package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import inf112.skeleton.app.game.MainApplication;


public class Main {

    public static LwjglApplicationConfiguration cfg;

    public static void main(String[] args) {
        cfg = new LwjglApplicationConfiguration();
        cfg.title = "RoboRally";
        cfg.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());

        MainApplication menu = new MainApplication();
        new LwjglApplication(menu, cfg);
    }
}