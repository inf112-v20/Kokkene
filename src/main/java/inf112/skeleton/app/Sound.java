package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;

import java.util.Random;

public class Sound {
    //store sound object
    private com.badlogic.gdx.audio.Sound sound;

    //store id
    private long id;
    private float pitch;

    //May be in unpacked file format.
    private static String soundFilePath;

    Sound(String filePath) {
        soundFilePath = filePath;
        sound = Gdx.audio.newSound(Gdx.files.internal(soundFilePath));
        long id = sound.play((float) 0);
        pitch = 1;
    }

    public void randomPitch(int bound) {
        Random random = new Random();
        Integer p = random.nextInt(bound);
        p = (100+p)/100;
        setPitch((float) p);
    }

    public void play() {
        sound.play(pitch);
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * reset with the chosen song
     * @param filePath filepath to the new song.
     */
    private void putNewSound (String filePath) {
        if (filePath == soundFilePath) return;
        dispose();
        soundFilePath = filePath;
        sound = Gdx.audio.newSound(Gdx.files.internal(soundFilePath));
    }

    public void dispose() {
        sound.dispose();
    }
}