package fr.upsaclay.bibs.tetris.model.score;

import fr.upsaclay.bibs.tetris.TetrisAction;
import fr.upsaclay.bibs.tetris.TetrisMode;
import fr.upsaclay.bibs.tetris.model.grid.TetrisCoordinates;
import fr.upsaclay.bibs.tetris.model.grid.TetrisGridView;
import fr.upsaclay.bibs.tetris.model.tetromino.Tetromino;
import fr.upsaclay.bibs.tetris.model.tetromino.TetrominoProvider;

import java.util.List;

public class ScoreComputerImpl implements ScoreComputer {
    private int Score;
    private int Level;
    private int Lines;
    int ComboCount =  -1;
    boolean LastAction;
    TetrisCoordinates TetrominolastPosition = new TetrisCoordinates(0, 0);;
    boolean IsSoftDrop;
    boolean NoMoreMove;
    boolean IsDown = false;
    boolean IsHardDrop;


    public ScoreComputerImpl(TetrisMode mode, int initialScore, int initialLevel, int initialLines) {
        this.Score = initialScore;
        this.Level = initialLevel;
        this.Lines = initialLines;
        mode = TetrisMode.MARATHON;

    }
    @Override
    public int getLevel() {
        return this.Level;
    }

    @Override
    public int getLines() {
        return this.Lines;
    }

    @Override
    public int getScore() {
        return this.Score;
    }

    @Override
    public int getComboCount() {
        return this.ComboCount;
    }

    @Override
    public void registerBeforeAction(TetrisAction action, TetrisGridView gridView) {
        TetrisCoordinates currentPosition = gridView.getCoordinates();

        if (action == TetrisAction.HARD_DROP) {
            TetrominolastPosition = currentPosition;
            IsSoftDrop = false;
            IsHardDrop = true;
            NoMoreMove = false;
        } else if (action == TetrisAction.START_SOFT_DROP) {
            IsSoftDrop = true;
            IsHardDrop = false;
        } else if (action == TetrisAction.DOWN) {
            IsDown = true;
            if (IsSoftDrop) {
                TetrominolastPosition = currentPosition;
            }
        } else if (action == TetrisAction.END_SOFT_DROP) {
            IsSoftDrop = false;
            IsHardDrop = false;
            NoMoreMove = true;
        }

        LastAction = true;
    }


    @Override
    public void registerAfterAction(TetrisGridView gridView) {
        if (!LastAction) {
            throw new IllegalStateException("No action registered before");
        }

        TetrisCoordinates currentPosition = gridView.getCoordinates();
        int LineDrop = currentPosition.getLine() - TetrominolastPosition.getLine();

        if (IsSoftDrop) {
            Score += LineDrop;
        } else if (IsHardDrop) {
            if(!NoMoreMove){
                Score += LineDrop*2;
            }
        }
    }


    @Override
    public void registerMergePack(List<Integer> packResult, TetrisGridView gridView) {
        int nbLinesCleared = packResult.size();

        if (nbLinesCleared > 0) {
            ComboCount++;
            int lineScore = 0;
            if (nbLinesCleared == 1) {
                lineScore = 100 * this.getLevel();
            } else if (nbLinesCleared == 2) {
                lineScore = 300 * this.getLevel();
            } else if (nbLinesCleared == 3) {
                lineScore = 500 * this.getLevel();
            } else if (nbLinesCleared == 4) {
                lineScore = 800 * this.getLevel();
            }
            Score += lineScore + 50*ComboCount*Level;
            Lines += nbLinesCleared;
            if (this.getLines() >= 10 * this.getLevel()) {
                Level++;
            }
        } else {
            ComboCount = -1;
        }
        LastAction = false;
    }

}
