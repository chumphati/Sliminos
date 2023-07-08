package fr.upsaclay.bibs.tetris.control.player;

import fr.upsaclay.bibs.tetris.TetrisAction;
import fr.upsaclay.bibs.tetris.model.grid.TetrisCoordinates;
import fr.upsaclay.bibs.tetris.model.grid.TetrisGrid;
import fr.upsaclay.bibs.tetris.model.grid.TetrisGridView;
import fr.upsaclay.bibs.tetris.model.score.ScoreComputer;
import fr.upsaclay.bibs.tetris.model.tetromino.Tetromino;
import fr.upsaclay.bibs.tetris.model.tetromino.TetrominoProvider;
import fr.upsaclay.bibs.tetris.model.tetromino.TetrominoProviderImpl;

import java.io.PrintStream;

public class SimpleGamePlayer implements GamePlayer {
    private PlayerType type;
    private TetrisGrid grid;
    private ScoreComputer score;
    private TetrominoProvider provider;
    private boolean isActive;
    private PrintStream printer;
    private boolean isStarted;

    private Tetromino heldTetromino;

    private boolean isSoftDropping = false;
    private boolean canHold = true;

    @Override
    public void initialize(TetrisGrid grid, ScoreComputer scoreComputer, TetrominoProvider provider) {
        this.grid = grid;
        this.provider = provider;
        this.score = scoreComputer;
        this.type = PlayerType.HUMAN;
        this.isActive = false;
        this.printer = System.out;
        this.isStarted = false;
        this.heldTetromino = null;
    }

    @Override
    public PlayerType getType() {
        return this.type;
    }

    @Override
    public void setLogPrintStream(PrintStream out) {
        this.printer = out;
    }

    @Override
    public int getLevel() {
        return this.score.getLevel();
    }

    @Override
    public int getScore() {
        return this.score.getScore();
    }

    @Override
    public int getLineScore() {
        return this.score.getLines();
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public void start() {
        this.isActive = true;

        if (!this.isStarted) {
            if(this.grid.getTetromino() == null) {
                Tetromino t = this.provider.next();

                this.grid.setTetromino(t);
                this.grid.setAtStartingCoordinates();

                this.isStarted = true;
            }
        }
    }

    @Override
    public void pause() {
        this.isActive = false;
    }

    @Override
    public boolean isOver() {
        boolean over = (!this.provider.hasNext() && this.grid.getTetromino() == null) || this.grid.hasConflicts();

        if (over) {
            this.isActive = false;
        }

        return over;
    }

    @Override
    public TetrisGridView getGridView() {
        return this.grid.getView();
    }

    @Override
    public boolean performAction(TetrisAction action) {
        if (this.isOver() || !this.isActive) {
            throw new IllegalStateException("pause or finished");
        }

        this.score.registerBeforeAction(action, this.grid.getView());

        boolean actionDone = false;

        switch (action){
            case MOVE_LEFT -> actionDone = this.grid.tryMove(TetrisCoordinates.LEFT);
            case MOVE_RIGHT -> actionDone = this.grid.tryMove(TetrisCoordinates.RIGHT);
            case HARD_DROP -> {
                this.grid.hardDrop();

                this.score.registerAfterAction(this.grid.getView());

                this.grid.merge();
                this.score.registerMergePack(this.grid.pack(), this.grid.getView());

                this.canHold = true;

                if (this.provider.hasNext()) {
                    this.grid.setTetromino(this.provider.next());
                    this.grid.setAtStartingCoordinates();
                }

                actionDone = true;
            }
            case ROTATE_RIGHT -> actionDone = this.grid.tryRotateRight();
            case ROTATE_LEFT -> actionDone = this.grid.tryRotateLeft();
            case START_SOFT_DROP -> {
                this.isSoftDropping = true;
                actionDone = true;
            }
            case END_SOFT_DROP -> {
                this.isSoftDropping = false;
                actionDone = true;
            }
            case DOWN -> {
                actionDone = this.grid.tryMove(TetrisCoordinates.DOWN);

                if (this.isSoftDropping) {
                    this.score.registerAfterAction(this.grid.getView());
                }

                if (!actionDone) {
                    this.grid.merge();
                    this.score.registerMergePack(this.grid.pack(), this.grid.getView());
                    this.canHold = true;
                    if (this.provider.hasNext()) {
                        this.grid.setTetromino(this.provider.next());
                        this.grid.setAtStartingCoordinates();
                    }
                    if (this.isSoftDropping) {
                        this.isSoftDropping = false;
                    }
                }

            }
            case HOLD -> {
                if (!this.canHold) {
                    break;
                }

                if (this.heldTetromino == null) {
                    if (this.provider.hasNext()) {
                        this.heldTetromino = this.grid.getTetromino(); //met le tétromino en hold
                        this.grid.setTetromino(this.provider.next()); //prend prochain
                        this.grid.setAtStartingCoordinates(); //recommence aux coordonnées du début
                        this.canHold = false;

                        actionDone = true;
                    }

                } else { // si environnement hold pas null et qu'il
                    Tetromino tmp = this.heldTetromino;
                    this.heldTetromino = this.grid.getTetromino();
                    this.grid.setTetromino(tmp);

                    this.canHold = false;

                    actionDone = true;
                }
            }
        }


        return actionDone;
    }

    @Override
    public Tetromino getHeldTetromino() {
        return this.heldTetromino;
    }
}