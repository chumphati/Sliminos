package fr.upsaclay.bibs.tetris.view.drawer;

import fr.upsaclay.bibs.tetris.model.grid.TetrisCell;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract class to avoid duplicating common code between GameDrawer, Hold and Provider
 * Allows you to draw the grid and put colours on it
 * @author Celine and Fiona
 */
public abstract class TetrominoDrawer extends JPanel implements Drawer{

    /**
     * Get the width of the grid boxes according to the space allocated to the grid
     */
    protected int squareWidth(int size) {
        return (int) this.getSize().getWidth() / size;
    }

    /**
     * Get the height of the grid boxes according to the space allocated to the grid
     */
    protected int squareHeight(int size) {
        return (int) this.getSize().getHeight() / size;
    }

    /**
     * Put color on boxes & draw
     */
    protected void drawTetromino(Graphics g, int x, int y, TetrisCell cell, int sizeWidth, int sizeHeight) {
        Color color = new Color(255, 241, 252);

        switch (cell){
            case I -> {
                color = new Color(240, 128, 128);
            }
            case O -> {
                color = new Color(255, 222, 173);
            }
            case T -> {
                color = new Color(152, 251, 152);
            }
            case L -> {
                color = new Color(175, 238, 238);
            }
            case J -> {
                color = new Color(216, 191, 216);
            }
            case Z -> {
                color = new Color(176, 196, 222);
            }
            case S -> {
                color = new Color(218, 112, 214);
            }
        }
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth(sizeWidth) - 2, squareHeight(sizeHeight) - 2);
        // add small "3d effect"
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight(sizeHeight) - 1, x, y);
        g.drawLine(x, y, x + squareWidth(sizeWidth) - 1, y);
        g.setColor(color.darker());

        // add lines between tetromino
        g.drawLine(x + 1, y + squareHeight(sizeHeight) - 1, x + squareWidth(sizeWidth) - 1, y + squareHeight(sizeHeight) - 1);
        g.drawLine(x + squareWidth(sizeWidth) - 1, y + squareHeight(sizeHeight) - 1, x + squareWidth(sizeWidth) - 1, y + 1);
    }

    /**
     * Repaint boxes
     */
    public void updatePaint() {
        this.repaint();
    }

    /**
     * Back color
     */
    public void setBackground(Color color) {
        super.setBackground(color);
    }
}
