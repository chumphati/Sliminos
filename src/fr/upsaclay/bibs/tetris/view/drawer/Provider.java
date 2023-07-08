package fr.upsaclay.bibs.tetris.view.drawer;

import fr.upsaclay.bibs.tetris.control.manager.GameManager;
import fr.upsaclay.bibs.tetris.model.tetromino.Tetromino;
import java.awt.*;

/**
 * Draw the next tetromino grid
 * @author Celine and Fiona
 */
public class Provider extends TetrominoDrawer {
    private GameManager gameManager;

    public Provider(GameManager gameManager) {
        this.gameManager = gameManager;
        setPreferredSize(new Dimension(100, 100));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Dimension size = getSize();
        Tetromino tetro = this.gameManager.getTetrominoProvider().showNext(0);
        if (tetro == null) {
            return;
        }

        int boxSize = tetro.getBoxSize();

        int boardTop = (int) size.getHeight() - tetro.getBoxSize() * squareHeight(boxSize);

        for (int i = 0; i < tetro.getBoxSize(); i++) {
            for (int j = 0; j < tetro.getBoxSize(); j++) {
                this.drawTetromino(g, j * squareWidth(boxSize), boardTop + i * squareHeight(boxSize), tetro.cell(i, j), boxSize, boxSize);
            }
        }
    }
}

