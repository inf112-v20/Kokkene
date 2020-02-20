package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        //cfg.fullscreen = true;
        cfg.title = "RoboRally";
        cfg.width = 720;
        cfg.height = 600;

        RoboRally rr = new RoboRally();
        //Sets the current map
        rr.mapFile = "12by12DizzyDash.tmx";
        new LwjglApplication(rr, cfg);
    }
}