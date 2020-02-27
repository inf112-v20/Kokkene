package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class Main {

    public static LwjglApplicationConfiguration cfg;

    public static void main(String[] args) {
        cfg = new LwjglApplicationConfiguration();
        cfg.title = "RoboRally";
        cfg.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());

        MainApplication menu = new MainApplication();
        Menu.mapFile = "maps/12by12DizzyDash.tmx";
        //Sets the current map
        new LwjglApplication(menu, cfg);
    }
}