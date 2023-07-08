package fr.upsaclay.bibs.tetris.view.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Sound settings
 * Thread = launch process in parallel
 * @author Celine and Fiona
 */
public class Sound extends Thread {
    private AudioInputStream url;
    Clip clip = AudioSystem.getClip();
    private boolean isSound = true;

    /**
     * Constructor : link to sound file
     */
    public Sound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String soundName = "resources/Boing.wav";
        this.url = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
        this.clip.open(this.url);
    }

    /**
     * Play sound once
     */
    public void playSound() throws LineUnavailableException, IOException {
        if (isSound) {
            this.clip.start();
            this.clip.loop(0);
            this.clip.setFramePosition(0);
        }
    }

    /**
     * Doesn't play sound
     */
    public void stopSound() {
        isSound = false;
    }

    /**
     * Check if sound is activated
     */
    public void activateSound() {
        isSound = true;
    }

    /**
     * Return if sound is activated
     */
    public boolean isSoundActive() {
        return this.isSound;
    }

}
