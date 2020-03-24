package inf112.skeleton.app.sound;

import com.badlogic.gdx.Gdx;

public class Sound {
    //store sound object
    private com.badlogic.gdx.audio.Sound sound;
    private float pitch = 0.5f;
    private boolean mute = false;

    public Sound(String filePath) {
        this.sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
    }

    public void play() {
        sound.play(pitch);
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