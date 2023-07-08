package fr.upsaclay.bibs.tetris.view.component;

import fr.upsaclay.bibs.tetris.view.Frame;
import fr.upsaclay.bibs.tetris.view.audio.Music;
import fr.upsaclay.bibs.tetris.view.audio.Sound;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Manage the on/off option for the music and the sound
 * Avoids duplicating the code twice in the main and game frames
 * @author Celine and Fiona
 */
public class SoundMenu extends JPanel implements ActionListener {
    private JButton buttonMusic;
    private JButton buttonSound;
    private Music music;
    private Sound sound;
    private Frame originalFrame;
    private Settings settings;

    /**
     * Constructor : activates/stop music/sound
     */
    public SoundMenu(Frame frame, Settings settings) {
        this.originalFrame = frame;
        this.settings = settings;

        try {
            this.sound = new Sound();
            this.music = new Music();
            if (this.settings.isAudioOn()) {
                this.sound.activateSound();
            } else {
                this.sound.stopSound();
            }

            if (this.settings.isMusicOn()) {
                this.music.activateMusic();
                this.music.loop();
            } else {
                this.music.stopLoop();
            }



        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        String textMusic = "MUSIC OFF";
        if (this.settings.isMusicOn()) {
            textMusic = "MUSIC ON";
        }
        this.buttonMusic = new JButton(textMusic);
        this.buttonMusic.addActionListener(this);

        String textSound = "SOUND OFF";
        if (this.settings.isAudioOn()) {
            textSound = "SOUND ON";
        }
        this.buttonSound = new JButton(textSound);
        this.buttonSound.addActionListener(this);

        this.add(this.buttonMusic);
        this.add(this.buttonSound);

    }

    /**
     * Performs actions on buttons music/sound
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.buttonMusic) {

            if (this.music.isMusicActive()) {
                this.music.stopLoop();
                this.buttonMusic.setText("MUSIC OFF");
            } else {
                try {
                    this.music = new Music();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                }
                this.music.activateMusic();
                try {
                    this.music.loop();
                } catch (LineUnavailableException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                this.buttonMusic.setText("MUSIC ON");
            }
            this.settings.setMusicOn(this.music.isMusicActive());

        } else if (e.getSource() == this.buttonSound) {

            if (this.sound.isSoundActive()) {
                this.sound.stopSound();
                this.buttonSound.setText("SOUND OFF");
            } else {
                this.sound.activateSound();
                this.buttonSound.setText("SOUND ON");
            }
            this.settings.setAudioOn(this.sound.isSoundActive());

        }

        this.originalFrame.requestFocus();
    }

    /**
     * Play sound
     */
    public void playEffect() {
        try {
            this.sound.playSound();
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stops sound and music
     */
    public void stop() {
        this.sound.stopSound();
        this.music.stopLoop();
    }

}
