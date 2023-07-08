package fr.upsaclay.bibs.tetris.control.manager;

import fr.upsaclay.bibs.tetris.TetrisMode;
import fr.upsaclay.bibs.tetris.control.player.GamePlayer;
import fr.upsaclay.bibs.tetris.control.player.PlayerType;
import fr.upsaclay.bibs.tetris.model.grid.TetrisCell;
import fr.upsaclay.bibs.tetris.model.grid.TetrisCoordinates;
import fr.upsaclay.bibs.tetris.model.grid.TetrisGrid;
import fr.upsaclay.bibs.tetris.model.grid.TetrisGridImpl;
import fr.upsaclay.bibs.tetris.model.score.ScoreComputer;
import fr.upsaclay.bibs.tetris.model.score.ScoreComputerImpl;
import fr.upsaclay.bibs.tetris.model.tetromino.Tetromino;
import fr.upsaclay.bibs.tetris.model.tetromino.TetrominoProvider;
import fr.upsaclay.bibs.tetris.model.tetromino.TetrominoShape;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

abstract class Game implements GameManager {
    protected TetrisMode gameMode;
    protected int nbLines;
    protected int nbCols;
    protected TetrominoProvider provider;
    protected PlayerType playerType;
    protected GamePlayer player;

    protected TetrisGrid grid;

    protected ScoreComputer scoreComputer;

    @Override
    public void initialize() {
        this.gameMode = DEFAULT_MODE;
        this.nbLines = DEFAULT_LINES;
        this.nbCols = DEFAULT_COLS;
        this.provider = DEFAULT_PROVIDER;
        this.playerType = DEFAULT_PLAYER_TYPE;
        this.grid = TetrisGrid.getEmptyGrid(this.nbLines, this.nbCols);
    }

    @Override
    public void setGameMode(TetrisMode mode) {
        this.gameMode = mode;
    }

    @Override
    public TetrisMode getGameMode() {
        return this.gameMode;
    }

    @Override
    public void setTetrominoProvider(TetrominoProvider provider) {
        this.provider = provider;
    }

    @Override
    public TetrominoProvider getTetrominoProvider() {
        return this.provider;
    }

    @Override
    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    @Override
    public PlayerType getPlayerType() {
        return this.playerType;
    }

    @Override
    public int getNumberOfLines() {
        return this.nbLines;
    }

    @Override
    public int getNumberOfCols() {
        return this.nbCols;
    }

    @Override
    public GamePlayer getPlayer() {
        return this.player;
    }

    @Override
    public void loadNewGame() {
        this.createPlayer();

        this.scoreComputer = ScoreComputer.getScoreComputer(this.gameMode);

        this.player.initialize(this.grid, this.scoreComputer, this.provider);
    }

    @Override
    public void loadFromFile(File file) throws IOException {
        int score = 0;
        int scoreLevel = 0;
        int scoreLines = 0;
        int tetroRotation = 0;
        int coordX = 0;
        int coordY = 0;

        int sizeCol = 0;
        TetrominoShape tetrominoShape = null;
        ArrayList<String[]> matrice = new ArrayList<>();

        Scanner scan = new Scanner(file);
        try {

            int cptline = 1;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                switch (cptline) {
                    case 1 -> {
                        this.gameMode = TetrisMode.valueOf(line);
                    }
                    case 2 -> {
                        score = Integer.parseInt(line);
                    }
                    case 3 -> {
                        scoreLevel = Integer.parseInt(line);
                    }
                    case 4 -> {
                        scoreLines = Integer.parseInt(line);
                    }
                    case 5 -> {
                        tetrominoShape = TetrominoShape.valueOf(line);
                    }
                    case 6 -> {
                        tetroRotation = Integer.parseInt(line);
                    }
                    case 7 -> {
                        coordX = Integer.parseInt(line);
                    }
                    case 8 -> {
                        coordY = Integer.parseInt(line);
                    }
                    default -> {
                        if (Objects.equals(line, ""))
                            break;

                        String[] data = line.split(" ");
                        if (sizeCol == 0) {
                            sizeCol = data.length;
                        }

                        if (data.length != sizeCol) {
                            throw new IOException("matrix size lines are not same");
                        }


                        matrice.add(data);
                    }
                }

                cptline++;
            }
            scan.close();
        } catch (Exception e) {
            scan.close();
            throw new IOException(e);
        }

        this.scoreComputer = ScoreComputer.getScoreComputer(this.gameMode, score, scoreLevel, scoreLines);
        Tetromino tetromino = tetrominoShape.getTetromino(tetroRotation);
        TetrisCoordinates coord = new TetrisCoordinates(coordX, coordY);
        this.provider = DEFAULT_PROVIDER;
        this.playerType = DEFAULT_PLAYER_TYPE;

        TetrisCell[][] cells = new TetrisCell[matrice.size()][sizeCol];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = TetrisCell.valueOf(matrice.get(i)[j]);
            }
        }

        this.grid = TetrisGrid.getEmptyGrid(matrice.size(), sizeCol);
        this.grid.initiateCells(cells);
        this.grid.setTetromino(tetromino);
        this.grid.setCoordinates(coord);
        this.createPlayer();
        this.player.initialize(this.grid, this.scoreComputer, this.provider);
    }

    @Override
    public void startPlayer() {
        this.player.start();
    }

    @Override
    public void pausePlayer() {
        this.player.pause();
    }

    @Override
    public void save(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(file + " not exist");
        }
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(this.gameMode.toString() + "\n");

            output.write(this.scoreComputer.getScore() + "\n");
            output.write(this.scoreComputer.getLevel() + "\n");
            output.write(this.scoreComputer.getLines() + "\n");

            output.write(this.grid.getTetromino().getShape().toString() + "\n");
            output.write(this.grid.getTetromino().getRotationNumber() + "\n");

            output.write(this.grid.getCoordinates().getLine() + "\n");
            output.write(this.grid.getCoordinates().getCol() + "\n");

            for (int i = 0; i < this.grid.numberOfLines(); i++) {
                for (int j = 0; j < this.grid.numberOfCols(); j++) {
                    output.write(this.grid.gridCell(i, j) + " ");
                }
                output.write("\n");
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( output != null ) {
                try {
                    output.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}