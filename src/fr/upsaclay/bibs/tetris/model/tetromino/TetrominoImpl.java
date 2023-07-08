package fr.upsaclay.bibs.tetris.model.tetromino;

import fr.upsaclay.bibs.tetris.model.grid.TetrisCell;
import fr.upsaclay.bibs.tetris.model.grid.TetrisCoordinates;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static fr.upsaclay.bibs.tetris.model.tetromino.TetrominoShape.MAX_ROTATION;

//TetrominoImpl = one shape
class TetrominoImpl implements Tetromino{
    //proprieties : tetromino shape = when tetromino is turned, out = get tetromino from tetromino shape
    public final TetrominoShape shape;
    //id rotation
    public final int nbRotation;
    public final TetrisCell[][] matrix;


    public TetrominoImpl(TetrominoShape shape, int nbRotation, TetrisCell[][] matrix) {
        this.shape = shape;
        this.nbRotation = nbRotation;
        this.matrix = matrix;
    }

    @Override
    public TetrominoShape getShape() {
        return this.shape;
    }

    @Override
    public int getRotationNumber() {
        return this.nbRotation;
    }

    //rotate to right
    @Override
    public Tetromino rotateRight() {
        int futurRotation = this.nbRotation + 1;
        if ( futurRotation >= this.shape.getNumberOfRotations()) {
            futurRotation = 0;
        }

        return this.shape.getTetromino(futurRotation);
    }

    //same rotate to left
    @Override
    public Tetromino rotateLeft() {
        int futurRotation = this.nbRotation - 1;
        if ( futurRotation < 0) {
            futurRotation = this.shape.getNumberOfRotations() - 1;
        }

        return this.shape.getTetromino(futurRotation);
    }

    //get content cell empty or not
    @Override
    public TetrisCell cell(int line, int col) {
        return this.matrix[line][col];
    }

    //length of the tetromino
    @Override
    public int getBoxSize() {
        return this.shape.getBoxSize();
    }

    @Override
    public List<TetrisCoordinates> wallKicksFromRight() {

        List<TetrisCoordinates> listToReturn = List.of();

        if (this.shape.getType() != TetrisCell.I && this.shape.getType() != TetrisCell.O){
            if (this.nbRotation == 0) {
                listToReturn = Arrays.asList(TetrisCoordinates.LEFT, new TetrisCoordinates(1, -1), new TetrisCoordinates(-2, 0), new TetrisCoordinates(-2, -1));
            }
            if (this.nbRotation == 1) {
                listToReturn = Arrays.asList(TetrisCoordinates.LEFT, new TetrisCoordinates(-1, -1), new TetrisCoordinates(2, 0), new TetrisCoordinates(2, -1));
            }
            if (this.nbRotation == 2) {
                listToReturn = Arrays.asList(TetrisCoordinates.RIGHT, new TetrisCoordinates(1, 1), new TetrisCoordinates(-2, 0), new TetrisCoordinates(-2, 1));
            }
            if (this.nbRotation == 3) {
                listToReturn = Arrays.asList(TetrisCoordinates.RIGHT, new TetrisCoordinates(-1, 1), new TetrisCoordinates(2, 0), new TetrisCoordinates(2, 1));
            }
        }

        if (this.shape.getType() == TetrisCell.O) {
            listToReturn = List.of();
        }

        if (this.shape.getType() == TetrisCell.I) {
            if (this.nbRotation == 0) {
                listToReturn = Arrays.asList(new TetrisCoordinates(0, 1), new TetrisCoordinates(0, -2), new TetrisCoordinates(2, 1), new TetrisCoordinates(-1, -2));
            }
            if (this.nbRotation == 1) {
                listToReturn = Arrays.asList(new TetrisCoordinates(0, -2), new TetrisCoordinates(0, 1), new TetrisCoordinates(1, -2), new TetrisCoordinates(-2, 1));
            }
            if (this.nbRotation == 2) {
                listToReturn = Arrays.asList(new TetrisCoordinates(0, -1), new TetrisCoordinates(0, 2), new TetrisCoordinates(-2, -1), new TetrisCoordinates(1, 2));
            }
            if (this.nbRotation == 3) {
                listToReturn = Arrays.asList(new TetrisCoordinates(0, 2), new TetrisCoordinates(0, -1), new TetrisCoordinates(-1, 2), new TetrisCoordinates(2, -1));
            }
        }

        return listToReturn;
    }

    @Override
    public List<TetrisCoordinates> wallKicksFromLeft() {

        List<TetrisCoordinates> listToReturn = List.of();

        if (this.shape.getType() != TetrisCell.I && this.shape.getType() != TetrisCell.O){
            if (this.nbRotation == 0) {
                listToReturn = Arrays.asList(TetrisCoordinates.RIGHT, new TetrisCoordinates(1, 1), new TetrisCoordinates(-2, 0), new TetrisCoordinates(-2, 1));
            }
            if (this.nbRotation == 1) {
                listToReturn = Arrays.asList(TetrisCoordinates.LEFT, new TetrisCoordinates(-1, -1), new TetrisCoordinates(2, 0), new TetrisCoordinates(2, -1));
            }
            if (this.nbRotation == 2) {
                listToReturn = Arrays.asList(TetrisCoordinates.LEFT, new TetrisCoordinates(1, -1), new TetrisCoordinates(-2, 0), new TetrisCoordinates(-2, -1));
            }
            if (this.nbRotation == 3) {
                listToReturn = Arrays.asList(TetrisCoordinates.RIGHT, new TetrisCoordinates(-1, 1), new TetrisCoordinates(2, 0), new TetrisCoordinates(2, 1));
            }
        }

        if (this.shape.getType() == TetrisCell.O) {
            listToReturn = List.of();
        }

        if (this.shape.getType() == TetrisCell.I) {
            if (this.nbRotation == 0) {
                listToReturn = Arrays.asList(new TetrisCoordinates(0, 2), new TetrisCoordinates(0, -1), new TetrisCoordinates(-1, 2), new TetrisCoordinates(2, -1));
            }
            if (this.nbRotation == 1) {
                listToReturn = Arrays.asList(new TetrisCoordinates(0, 1), new TetrisCoordinates(0, -2), new TetrisCoordinates(2, 1), new TetrisCoordinates(-1, -2));
            }
            if (this.nbRotation == 2) {
                listToReturn = Arrays.asList(new TetrisCoordinates(0, -2), new TetrisCoordinates(0, 1), new TetrisCoordinates(1, -2), new TetrisCoordinates(-2, 1));
            }
            if (this.nbRotation == 3) {
                listToReturn = Arrays.asList(new TetrisCoordinates(0, -1), new TetrisCoordinates(0, 2), new TetrisCoordinates(-2, -1), new TetrisCoordinates(1, 2));
            }
        }

        return listToReturn;
    }
}
