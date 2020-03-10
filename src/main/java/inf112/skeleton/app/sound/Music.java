package inf112.skeleton.app.sound;

import com.badlogic.gdx.Gdx;

/**
 * Controls the background music
 */
public class Music {

    //store current position
    com.badlogic.gdx.audio.Music music;

    //May be in unpacked file format.
    static String musicFilePath = "assets/sound/S31-CrackedOutRobot.wav";

    public Music() {
        music = Gdx.audio.newMusic(Gdx.files.internal(musicFilePath));
        music.setVolume(0.1f);
    }

    public void play() {
        music.play();
        music.setLooping(true);
    }

    public void  pause() {
        music.pause();
    }

    public void muteToggle() {
        if (music.getVolume()==0f) music.setVolume(0.1f);
        else music.setVolume(0f);
    }

    public void pauseToggle() {
        if(music.isPlaying()) pause();
        else play();
    }

    public void dispose() {
        music.dispose();
    }
}
