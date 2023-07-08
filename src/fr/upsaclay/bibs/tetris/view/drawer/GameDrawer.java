package fr.upsaclay.bibs.tetris.view.drawer;

import fr.upsaclay.bibs.tetris.control.manager.GameManager;
import java.awt.*;

/**
 * Draw the game grid
 * @author Celine and Fiona
 */
public class GameDrawer extends TetrominoDrawer {
    private GameManager gameManager;
    public GameDrawer(GameManager gm) {
        this.gameManager = gm;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Dimension size = getSize();
        int nbCols = this.gameManager.getNumberOfCols();
        int nbLines = this.gameManager.getNumberOfLines();

        int boardTop = (int) size.getHeight() - nbLines * squareHeight(nbLines);

        for (int i = 0; i < this.gameManager.getNumberOfLines(); i++) {
            for (int j = 0; j < this.gameManager.getNumberOfCols(); ++j) {
                this.drawTetromino(g, j * squareWidth(nbCols), boardTop + i * squareHeight(nbLines), this.gameManager.getPlayer().getGridView().visibleCell(i,j), nbCols, nbLines);
            }
        }

    }

    public void updatePaint() {
        this.repaint();
    }
}
