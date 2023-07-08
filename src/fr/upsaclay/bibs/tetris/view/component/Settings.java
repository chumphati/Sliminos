package fr.upsaclay.bibs.tetris.view.component;

/**
 * Manage if audio must be on or off between the frames
 * @author Celine and Fiona
 */
public class Settings {
    private boolean audioOn;
    private boolean musicOn;

    public Settings() {
        this.audioOn = true;
        this.musicOn = true;
    }

    /**
     * Check if audio is on
     */
    public void setAudioOn(boolean b) {
        this.audioOn = b;
    }
    /**
     * Check if music is on
     */
    public void setMusicOn(boolean b) {
        this.musicOn = b;
    }
    /**
     * Return if music is on
     */
    public boolean isMusicOn() {
        return this.musicOn;
    }
    /**
     * Return if sound is on
     */
    public boolean isAudioOn() {
        return this.audioOn;
    }
}
