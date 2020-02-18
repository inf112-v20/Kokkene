package inf112.skeleton.app;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Controls the background music
 */
public class Music {

    //store current position
    Long currentFrame;
    Clip clip;

    AudioInputStream audioInputStream;
    FloatControl volume; //actually gain
    BooleanControl muter;

    //May be in unpacked file format.
    static String themeSong = "assets/S31-CrackedOutRobot.wav";
    private String status;

    Music() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        SimpleAudioPlayer();
        volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volume.setValue(-10.0f);
        muter = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
    }

    /**
     * Only used in constructor.
     */
    private void SimpleAudioPlayer()
            throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(themeSong).getAbsoluteFile());

        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    public void play() {
        clip.start();

        status = "play";
    }

    public void  pause() {
        if (status=="paused") return;

        this.currentFrame = this.clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
    }

    public void resumeAudio() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        if (status=="play") return;

        clip.close();
        resetAudioStream(themeSong);
        clip.setMicrosecondPosition(currentFrame);
        this.play();
    }

    public void restart() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        clip.stop();
        clip.close();
        resetAudioStream(themeSong);
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
        this.play();
    }

    public void stop() {
        clip.stop();
        clip.close();
        currentFrame = 0L;
    }

    public void muteToggle() {
        muter.setValue(!muter.getValue());
    }

    /**
     * reset with the song chosen song
     */
    private void resetAudioStream(String filePath) throws IOException,
            LineUnavailableException, UnsupportedAudioFileException {
        audioInputStream = AudioSystem.getAudioInputStream(
                new File(filePath).getAbsoluteFile());
        themeSong = filePath;
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void pauseToggle() {
        if (status=="paused") play();

        else pause();
    }
}
