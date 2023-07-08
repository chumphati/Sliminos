package fr.upsaclay.bibs.tetris.view;

/**
 * Frame used by game and home
 * Created to deal with focus loss due to keyboard actions and frame changes
 * @author Celine and Fiona
 */
public interface Frame {
    /**
     * Quit the game frame
     */
    void quitFrame();

    /**
     * Restores focus to link actions to keys
     */
    void requestFocus();
}
