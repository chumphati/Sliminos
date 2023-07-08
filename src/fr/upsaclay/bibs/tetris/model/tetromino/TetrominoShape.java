package fr.upsaclay.bibs.tetris.model.tetromino;

import java.util.ArrayList;
import java.util.Random;

import fr.upsaclay.bibs.tetris.model.grid.TetrisCell;

/**
 * The list of different tetromino shapes
 * <p>
 * Each shape receives its initial tetromino as double array of tetris cells
 * <p>
 * It should then be able to return any rotation of the initial cells
 * <p>
 * Suggestion: compute all rotations at the creation of the shape and keeps the tetromino
 * stored inside the shape object
 * <p>
 * You need to implement the private constructor (as it is an enum) as well as needed
 * methods. You can add methods and fields and all you need.
 * <p>
 * We provide you with the random method
 *
 * @author Viviane Pons
 */
public enum TetrominoShape {
    ISHAPE(new TetrisCell[][]{
            {TetrisCell.EMPTY, TetrisCell.EMPTY, TetrisCell.EMPTY, TetrisCell.EMPTY},
            {TetrisCell.I, TetrisCell.I, TetrisCell.I, TetrisCell.I},
            {TetrisCell.EMPTY, TetrisCell.EMPTY, TetrisCell.EMPTY, TetrisCell.EMPTY},
            {TetrisCell.EMPTY, TetrisCell.EMPTY, TetrisCell.EMPTY, TetrisCell.EMPTY}
    }),
    OSHAPE(new TetrisCell[][]{
            {TetrisCell.O, TetrisCell.O},
            {TetrisCell.O, TetrisCell.O}
    }),
    TSHAPE(new TetrisCell[][]{
            {TetrisCell.EMPTY, TetrisCell.T, TetrisCell.EMPTY},
            {TetrisCell.T, TetrisCell.T, TetrisCell.T},
            {TetrisCell.EMPTY, TetrisCell.EMPTY, TetrisCell.EMPTY}
    }),
    LSHAPE(new TetrisCell[][]{
            {TetrisCell.EMPTY, TetrisCell.EMPTY, TetrisCell.L},
            {TetrisCell.L, TetrisCell.L, TetrisCell.L},
            {TetrisCell.EMPTY, TetrisCell.EMPTY, TetrisCell.EMPTY}
    }),
    JSHAPE(new TetrisCell[][]{
            {TetrisCell.J, TetrisCell.EMPTY, TetrisCell.EMPTY},
            {TetrisCell.J, TetrisCell.J, TetrisCell.J},
            {TetrisCell.EMPTY, TetrisCell.EMPTY, TetrisCell.EMPTY}
    }),
    ZSHAPE(new TetrisCell[][]{
            {TetrisCell.Z, TetrisCell.Z, TetrisCell.EMPTY},
            {TetrisCell.EMPTY, TetrisCell.Z, TetrisCell.Z},
            {TetrisCell.EMPTY, TetrisCell.EMPTY, TetrisCell.EMPTY}
    }),
    SSHAPE(new TetrisCell[][]{
            {TetrisCell.EMPTY, TetrisCell.S, TetrisCell.S},
            {TetrisCell.S, TetrisCell.S, TetrisCell.EMPTY},
            {TetrisCell.EMPTY, TetrisCell.EMPTY, TetrisCell.EMPTY}
    });


    private static final Random RANDOM = new Random();
    public static final int MAX_ROTATION = 4;
    public static final int MIN_ROTATION = 1;

    //generate a random tetromino
    public static Tetromino randomTetromino() {
        TetrominoShape randomShape = values()[RANDOM.nextInt(values().length)];
        return randomShape.getTetromino(RANDOM.nextInt(randomShape.getNumberOfRotations()));
    }

    //list of tetromino that takes all the possible forms
    private final Tetromino[] possibleForms;
    //length
    private final int boxSize;
    private TetrisCell type;
    //max rotation shape can get
    private int maxRotation;

    // TetrominoShape will create a Tetris form from initial shape:
    // Get length, possible form (stock all possible forms): to choose after the good one depending on the number
    // of rotation, max rotation = max rotation the tetromino can do, and type
    private TetrominoShape(TetrisCell[][] initialShape) {
        this.boxSize = initialShape.length;
        this.maxRotation = MAX_ROTATION;

        this.possibleForms = new Tetromino[this.maxRotation];

        boolean haveEmpty = false;

        // search for type. When found, break loop;
        for (int i = 0; i < initialShape.length; i++) {
            for (int j = 0; j < initialShape[0].length; j++){
                if (initialShape[j][j] == TetrisCell.EMPTY) {
                    haveEmpty = true;
                }

                if (initialShape[j][j] != TetrisCell.EMPTY && initialShape[j][j] != TetrisCell.GREY) {
                    this.type = initialShape[j][j];
                }
            }
        }

        // if it doesn't have empty cell, so no rotation is posible
        if (!haveEmpty) {
            this.maxRotation = MIN_ROTATION;
        }

        for (int i = 0; i < this.maxRotation; i++) {
            if (i > 0 ) {
                initialShape = Rotate(initialShape);
            }

            Tetromino tetromino = new TetrominoImpl(this, i, initialShape);
            this.possibleForms[i] = tetromino;
        }

    }


    public TetrisCell getType() {
        return this.type;
    }

    public int getNumberOfRotations() {
        return this.maxRotation;
    }

    public int getBoxSize() {
        return this.boxSize;
    }

    public Tetromino getTetromino(int rotationNumber) {
        return this.possibleForms[rotationNumber];
    }


    /**
     * Do rotation : get transposition / reverse
     */
    public static TetrisCell[][] Rotate(TetrisCell[][] matrixCells)  {
        int size = matrixCells.length;

        // copy the matrix because in Java: the value is passed by value so it can be modified
        TetrisCell[][] copy = new TetrisCell[matrixCells.length][matrixCells.length];
        for (int i = 0; i < matrixCells.length; i++) {
            for (int j = 0; j < matrixCells.length; j++) {
                copy[i][j] = matrixCells[i][j];
            }
        }

        // create the transposition of the matrix.
        // the rotation is done for shape, but not for the values
        for (int i = 0; i < size; i++){
            for (int j = i; j < size; j++){
                TetrisCell tmpCell = copy[i][j];
                copy[i][j] = copy[j][i];
                copy[j][i] = tmpCell;
            }
        }

        // reverse the row to have the right values at the good place
        for (int i = 0; i < size; i++){
            for(int j = 0; j < size / 2; j++){
                TetrisCell tmpCell = copy[i][j];
                copy[i][j] = copy[i][size - 1 - j];
                copy[i][size - 1 - j] = tmpCell;
            }
        }

        return copy;
    }

}
