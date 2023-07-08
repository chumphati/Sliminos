package fr.upsaclay.bibs.tetris.view.component;

import javax.swing.*;
import java.awt.*;

/**
 * Display an image in a graphical interface.
 * @author Celine and Fiona
 */

public class ImagePanel extends JPanel {
    private Image img;
    /**
     * Create an image from the filename
     */

    public ImagePanel(String img) {
        this(new ImageIcon(img).getImage());
    }

    /**
     * Set the panel's size to match the image's dimensions.
     */

    public ImagePanel(Image img) {
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    /**
     * Draw the image onto the panel
     */

    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
}

