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
    boolean mute;

    AudioInputStream audioInputStream;
    FloatControl volume;

    //May be in unpacked file format.
    static String themeSong = "assets/S31-CrackedOutRobot.wav";
    private String status;

    Music() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        mute = false;
        SimpleAudioPlayer();
        volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    }

    public void SimpleAudioPlayer()
        throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(themeSong).getAbsoluteFile());

        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    /**
     * start the music
     */
    public void play() {
        clip.start();

        status = "play";
    }

    /**
     * pause the music
     */
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
        resetAudioStream();
        clip.setMicrosecondPosition(currentFrame);
        this.play();
    }

    public void restart() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        clip.stop();
        clip.close();
        resetAudioStream();
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
        if (mute) {
            volume.setValue(volume.getMaximum());
            mute = !mute;
            return;
        }
        volume.setValue(volume.getMinimum());
        mute = !mute;
    }

    public void resetAudioStream() throws IOException,
            LineUnavailableException, UnsupportedAudioFileException {
        audioInputStream = AudioSystem.getAudioInputStream(
                new File(themeSong).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

}
