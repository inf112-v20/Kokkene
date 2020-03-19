package inf112.skeleton.app.sound;

import com.badlogic.gdx.Gdx;

public class Sound {
    //store sound object
    private com.badlogic.gdx.audio.Sound sound;

    //store id
    private long id;
    private float pitch = 0.5f;
    private boolean mute = false;

    public Sound(String filePath) {
        this.sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
        this.id = sound.play((float) 0);
    }

    public void play() {
        id = sound.play(pitch);
    }

    public void muteToggle() {
        if (mute) {
            pitch = 0.5f;
            mute = false;
        }
        else {
            pitch = 0;
            mute = true;
        }
    }

    //public void dispose() { sound.dispose(); }
}