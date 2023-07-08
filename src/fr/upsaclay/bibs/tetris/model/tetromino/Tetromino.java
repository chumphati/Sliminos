package fr.upsaclay.bibs.tetris.model.tetromino;

import java.util.List;

import fr.upsaclay.bibs.tetris.model.grid.TetrisCell;
import fr.upsaclay.bibs.tetris.model.grid.TetrisCoordinates;

/**
 * The interface describing a Tetromino
 * <p>
 * A tetromino is identified by its shape and rotation number
 * from their initial states
 * <p>
 * 2 tetrominos with similar shape and rotation numbers should
 * be equals
 * <p>
 * Tetrominos are always created using TetrominoShape
 * <p>
 * Tetromino cannot be modified. Ideally there should be
 * only one instance of each tetromino
 * (i.e. each time a tetromino is "created", it receives a ref
 * to the corresponding constant tetromino)
 * <p>
 * Tetrominos are reprensented by square boxes of tetris cells
 * <p>
 * the boxes are always squares (of size 2, 3 or 4): even if the
 * tetromino can fit in a smaller non square box.
 * <p>
 * In particular, some tetrominos have empty lines / columns
 * in their squaure: that is ok and follows Tetris rules
 * guidelines.
 *
 * @author Viviane Pons
 */
public interface Tetromino {

    /**
     * Return the Tetromino shape
     *
     * @return the shape of the tetromino
     */
    public TetrominoShape getShape();

    /**
     * Return the rotation number
     *
     * @return the number of rotation
     */
    public int getRotationNumber();

    /**
     * Return the tetromino obtained by
     * rotation to the right
     *
     * @return a tetromino
     */
    public Tetromino rotateRight();

    /**
     * Return the tetromino obtained by
     * rotation to the left
     *
     * @return a tetromino
     */
    public Tetromino rotateLeft();

    /**
     * The cell at position line, col
     * the top line is 0
     * the left colmun is 0
     *
     * @param line
     * @param col
     * @return a tetris cell (can be empty tetris cell)
     */
    public TetrisCell cell(int line, int col);

    /**
     * The box size
     * <p>
     * a tetromino has cells for
     * 0 <= line < boxSize
     * 0 <= col < boxSize
     *
     * @return the box size
     */
    public int getBoxSize();

    /**
     * Wall kicks are certain "trick" to handle tetromino
     * rotations inside a tetris grid
     * <p>
     * See Tetris wiki for more information
     * <p>
     * wallKicks can be hard coded within the Tetromino class
     * (see the tests for accurate values to be returned)
     *
     * @return a list of TetrisCoordinates to be applied when arriving at this tetromino after
     * a right rotation
     */
    public List<TetrisCoordinates> wallKicksFromRight();

    /**
     * Wall kicks are certain "trick" to handle tetromino
     * rotations inside a tetris grid
     * <p>
     * See Tetris wiki for more information
     * <p>
     * wallKicks can be hard coded within the Tetromino class
     * (see the tests for accurate values to be returned)
     *
     * @return a list of TetrisCoordinates to be applied when arriving at this tetromino after
     * a left rotation
     */
    public List<TetrisCoordinates> wallKicksFromLeft();


}