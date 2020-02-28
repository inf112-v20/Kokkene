package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;

import java.util.Random;

public class Sound {
    //store sound object
    private com.badlogic.gdx.audio.Sound sound;

    //store id
    private long id;
    private float pitch = 0.5f;

    //May be in unpacked file format.
    private String soundFilePath;

    Sound(String filePath) {
        this.soundFilePath = filePath;
        this.sound = Gdx.audio.newSound(Gdx.files.internal(soundFilePath));
        this.id = sound.play((float) 0);
    }

    public void play() {
        id = sound.play(0.5f);
    }

    public void dispose() {
        sound.dispose();
    }
}