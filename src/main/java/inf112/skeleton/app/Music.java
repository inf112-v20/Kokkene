package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;

/**
 * Controls the background music
 */
public class Music {

    //store current position
    com.badlogic.gdx.audio.Music music;

    //May be in unpacked file format.
    static String musicFilePath = "assets/S31-CrackedOutRobot.wav";

    Music() {
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

    public void restart() {
        music.stop();
        music.play();
    }

    public void stop() {
        music.stop();
    }

    public void muteToggle() {
        if (music.getVolume()==0f) music.setVolume(0.1f);
        else music.setVolume(0f);
    }

    /**
     * reset with the chosen song
     * @param filePath filepath to the new song.
     */
    private void resetWithNewSong (String filePath) {
        if (filePath == musicFilePath) return;
        dispose();
        musicFilePath = filePath;
        music = Gdx.audio.newMusic(Gdx.files.internal(musicFilePath));
    }

    public void pauseToggle() {
        if(music.isPlaying()) pause();
        else play();
    }

    public void dispose() {
        music.dispose();
    }
}
