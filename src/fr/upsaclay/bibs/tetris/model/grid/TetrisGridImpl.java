package fr.upsaclay.bibs.tetris.model.grid;

import fr.upsaclay.bibs.tetris.model.tetromino.Tetromino;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class TetrisGridImpl implements TetrisGrid{

    private TetrisCell[][] tetrisCells;
    private Tetromino tetromino;
    private TetrisCoordinates coords;
    private final int nbLines;
    private final int nbCols;

    public TetrisGridImpl(int nbLines, int nbCols){
        this.nbLines = nbLines;
        this.nbCols = nbCols;
        this.tetrisCells = new TetrisCell[nbLines][nbCols];
    }

    @Override
    public SynchronizedView getView() {
        return new SynchronizedView(this);
    }

    @Override
    public void initiateCells(TetrisCell[][] cells) {
        if (cells.length != this.nbLines || cells[0].length != this.nbCols) {
            throw new IllegalArgumentException("Number of line or/and cols are not coherent");
        }

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                this.tetrisCells[i][j] = cells[i][j];
            }
        }
    }

    @Override
    public void setTetromino(Tetromino tetromino) {
        this.tetromino = tetromino;
    }

    @Override
    public void setCoordinates(TetrisCoordinates coordinates) {
        this.coords = coordinates;
    }

    @Override
    public void setAtStartingCoordinates() {
        this.coords = new TetrisCoordinates(0, (this.nbCols - this.getTetromino().getBoxSize()) / 2 );
    }

    @Override
    public boolean tryMove(TetrisCoordinates dir) {
        if (this.tetromino == null || this.coords == null) {
            throw new IllegalStateException("invalid state: coord/tetromino does not exist");
        }

        TetrisCoordinates oldCoords = new TetrisCoordinates(this.coords.getLine(), this.coords.getCol());

        if (dir == TetrisCoordinates.UP) {
            this.coords = new TetrisCoordinates(this.coords.getLine() - 1, this.coords.getCol());
        } else if (dir == TetrisCoordinates.DOWN) {
            this.coords = new TetrisCoordinates(this.coords.getLine() + 1, this.coords.getCol());
        } else if (dir == TetrisCoordinates.LEFT) {
            this.coords = new TetrisCoordinates(this.coords.getLine(), this.coords.getCol() - 1);
        } else if (dir == TetrisCoordinates.RIGHT) {
            this.coords = new TetrisCoordinates(this.coords.getLine(), this.coords.getCol() + 1);
        } else {
            this.coords = new TetrisCoordinates(dir.getLine() + this.coords.getLine(), dir.getCol() + this.coords.getCol());
        }

        if (this.hasConflicts()) {
            this.coords = oldCoords;
            return false;
        }

        return true;
    }


    @Override
    public boolean tryRotateRight() {
        if (this.tetromino == null || this.coords == null) {
            throw new IllegalStateException("invalid state: coord/tetromino does not exist");
        }

        Tetromino initialTetro = this.tetromino;
        TetrisCoordinates oldCoords = new TetrisCoordinates(this.coords.getLine(), this.coords.getCol());

        int maxRotation = initialTetro.getShape().getNumberOfRotations();

        int n = 0;
        this.tetromino = this.tetromino.rotateRight();

        if (!hasConflicts()) {
            return true;
        }

        while (hasConflicts() && n < 4) {
            int newX = 0;
            int newY = 0;

            newX = this.coords.getLine() + this.tetromino.wallKicksFromRight().get(n).getLine();
            newY = this.coords.getCol() + this.tetromino.wallKicksFromRight().get(n).getCol();

            TetrisCoordinates newCoordWallQuick = new TetrisCoordinates(newX, newY);
            this.coords = newCoordWallQuick;

            if (!hasConflicts()) {
                return true;
            }

            if (!gridCell(newX,newY).equals(TetrisCell.GREY)) {
                this.coords = oldCoords;
                n = n + 1;
                continue;
            }

            this.coords = oldCoords;
            n = n + 1;
        }


        this.tetromino = initialTetro;

        return false;
    }

    @Override
    public boolean tryRotateLeft() {
        if (this.tetromino == null || this.coords == null) {
            throw new IllegalStateException("invalid state: coord/tetromino does not exist");
        }

        Tetromino initialTetro = this.tetromino;
        TetrisCoordinates oldCoords = new TetrisCoordinates(this.coords.getLine(), this.coords.getCol());

        int maxRotation = initialTetro.getShape().getNumberOfRotations();

        int n = 0;
        this.tetromino = this.tetromino.rotateLeft();

        if (!hasConflicts()) {
            return true;
        }

        while (hasConflicts() && n < 4) {
            int newX = 0;
            int newY = 0;

            newX = this.coords.getLine() + this.tetromino.wallKicksFromLeft().get(n).getLine();
            newY = this.coords.getCol() + this.tetromino.wallKicksFromLeft().get(n).getCol();

            TetrisCoordinates newCoordWallQuick = new TetrisCoordinates(newX, newY);
            this.coords = newCoordWallQuick;

            if (!hasConflicts()) {
                return true;
            }

            if (!gridCell(newX,newY).equals(TetrisCell.GREY)) {
                this.coords = oldCoords;
                n = n + 1;
                continue;
            }

            this.coords = oldCoords;
            n = n + 1;
        }


        this.tetromino = initialTetro;

        return false;
    }

    @Override
    public void merge() {
        if (this.tetromino != null && this.coords == null) {
            throw new IllegalStateException("tetromino present but not coords");
        }

        for (int i = 0; i < this.nbLines; i++) {
            for (int j = 0; j < this.nbCols; j++) {
                tetrisCells[i][j] = this.visibleCell(i, j);
            }
        }

        this.tetromino = null;
        this.coords = null;
    }

    @Override
    public void hardDrop() {
        if (this.tetromino == null || this.coords == null) {
            throw new IllegalStateException("invalid state: coord/tetromino does not exist");
        }

        while (true) {
            boolean hasMoved = this.tryMove(TetrisCoordinates.DOWN);
            if (!hasMoved) {
                break;
            }
        }

    }

    @Override
    public List<Integer> pack() {
        List<Integer> listFullLines = fullLines();
        List<Integer> listPackedLines = new ArrayList<>();

        for (int line : listFullLines) {
            for (int j = 0; j < this.nbCols; j++) {
                this.tetrisCells[line][j] = TetrisCell.EMPTY;
            }

            listPackedLines.add(line);
        }

        for (int i = this.nbLines-1; i >= 0; i--) {
            if (isEmpty(i)) {
                // get next line not empty
                for (int j = i-1; j >= 0; j--) {
                    if (isEmpty(j)) {
                        continue;
                    }

                    for (int k = 0; k < this.nbCols; k++) {
                        TetrisCell tmp = this.tetrisCells[j][k];
                        this.tetrisCells[i][k] = tmp;
                        this.tetrisCells[j][k] = TetrisCell.EMPTY;
                    }

                    break;
                }
            }
        }

        return listPackedLines;
    }

    @Override
    public int numberOfLines() {
        return this.nbLines;
    }

    @Override
    public int numberOfCols() {
        return this.nbCols;
    }

    @Override
    public TetrisCell gridCell(int i, int j) {
        if (i < 0 || j < 0 || i >= this.nbLines || j >= this.nbCols) {
            return TetrisCell.GREY;
        }

        return this.tetrisCells[i][j];
    }

    @Override
    public void printGrid(PrintStream out) {
        for (int i = 0; i < this.nbLines; i++) {
            for (int j = 0; j < this.nbCols; j++) {
                out.print(this.tetrisCells[i][j] + " ");
            }

            out.print("\n");
        }
    }

    @Override
    public boolean hasTetromino() {
        return this.tetromino != null;
    }

    @Override
    public Tetromino getTetromino() {
        return this.tetromino;
    }

    @Override
    public TetrisCoordinates getCoordinates() {
        return this.coords;
    }

    @Override
    public TetrisCell visibleCell(int i, int j) {
        if (this.tetromino != null && this.coords == null) {
            throw new IllegalStateException("tetromino present but not coords");
        }

        TetrisCell[][] tetrisCellsVisible = new TetrisCell[this.nbLines][this.nbCols];
        for (int k = 0; k < this.nbLines; k++) {
            for (int l = 0; l < this.nbCols; l++) {
                tetrisCellsVisible[i][j] = this.tetrisCells[i][j];
            }
        }

        if (this.tetromino != null) {

            int limitLine = this.coords.getLine() + this.tetromino.getBoxSize();
            int limitCols = this.coords.getCol() + this.tetromino.getBoxSize();

            if (i >= this.coords.getLine() && j >= this.coords.getCol() && i < limitLine && j < limitCols) {
                int indexX = i;
                int indexY = j;

                if (i >= this.coords.getLine()) {
                    indexX = i - this.coords.getLine();
                }

                if (j >= this.coords.getCol()) {
                    indexY = j - this.coords.getCol();
                }

                TetrisCell cell = this.tetromino.cell(indexX, indexY);
                if (cell != TetrisCell.EMPTY) {
                    tetrisCellsVisible[i][j] = this.tetromino.cell(indexX, indexY);
                }

            }
        }

        return tetrisCellsVisible[i][j];
    }

    @Override
    public boolean hasConflicts() {
        if (this.tetromino == null)
            return false;
        if (this.coords == null)
            throw new IllegalStateException("tetromino without coords");

        int initialLine = this.coords.getLine();
        int initialCol = this.coords.getCol();

        for (int i = initialLine; i < initialLine+this.tetromino.getBoxSize(); i++){
            for (int j = initialCol; j < initialCol+this.tetromino.getBoxSize(); j++){
                if (this.tetromino.cell(i-initialLine, j-initialCol) != TetrisCell.EMPTY && gridCell(i, j) != TetrisCell.EMPTY)
                    return true;
            }
        }

        return false;
    }

    @Override
    public boolean isFull(int lineNumber) {
        for (int i = 0; i < this.nbCols; i++) {
            if (this.tetrisCells[lineNumber][i] == TetrisCell.EMPTY) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty(int lineNumber) {
        for (int i = 0; i < this.nbCols; i++) {
            if (this.tetrisCells[lineNumber][i] != TetrisCell.EMPTY) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Integer> fullLines() {
        List<Integer> listLines = new ArrayList<>();
        boolean isFull;

        for (int i = 0; i < this.nbLines; i++) {
            isFull = true;

            for (int j = 0; j < this.nbCols; j++) {
                if (this.tetrisCells[i][j] == TetrisCell.EMPTY) {
                    isFull = false;

                    break;
                }
            }

            if (isFull) {
                listLines.add(i);
            }
        }

        return listLines;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.nbLines; i++) {
            for (int j = 0; j < this.nbCols; j++) {
                if (this.tetrisCells[i][j] != TetrisCell.EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }
}
