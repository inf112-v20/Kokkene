package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class Main {

    public static LwjglApplicationConfiguration cfg;

    public static void main(String[] args) {
        cfg = new LwjglApplicationConfiguration();
        cfg.title = "RoboRally";
        cfg.width = 720;
        cfg.height = 600;
        cfg.setFromDisplayMode(cfg.getDesktopDisplayMode());

        RoboRally rr = new RoboRally();
        //Sets the current map
        rr.mapFile = "12by12DizzyDash.tmx";
        new LwjglApplication(rr, cfg);
    }
}