package fr.upsaclay.bibs.tetris.view.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Music settings
 * Thread = launch process in parallel
 * @author Celine and Fiona
 */
public class Music extends Thread
{
    private AudioInputStream url;
    private boolean musicIsActive = true;
    Clip clip = AudioSystem.getClip();

    /**
     * Constructor : link to music
     */
    public Music() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
            String soundName = "resources/main_music_theme.wav";
            this.url = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
    }

    /**
     * Activates music in loop mode
     */
    public void loop() throws LineUnavailableException, IOException {
        if (musicIsActive) {
            this.clip.open(this.url);
            this.clip.start();
            this.clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stops to play the music
     */
    public void stopLoop() {
        this.clip.stop();
        this.clip.close();
        musicIsActive = false;
    }

    /**
     * Check if music is activated
     */
    public void activateMusic() {
        musicIsActive = true;
    }

    /**
     * Return if music is activated
     */
    public boolean isMusicActive() {
        return this.musicIsActive;
    }
}