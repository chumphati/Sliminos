package fr.upsaclay.bibs.tetris.view.drawer;

import java.awt.*;

/**
 * The interface has been created for the classes of the drawer package (same signature)
 * Created to draw the squares and tetrominos for the main game and the next and hold display
 * @author Celine and Fiona
 */
public interface Drawer {

    /**
     * Update color of the boxes
     */
    public void updatePaint();

    /**
     * Set background color
     */
    public void setBackground(Color color);
}
