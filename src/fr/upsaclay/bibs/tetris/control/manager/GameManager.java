package fr.upsaclay.bibs.tetris.control.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import fr.upsaclay.bibs.tetris.TetrisMode;
import fr.upsaclay.bibs.tetris.control.player.GamePlayer;
import fr.upsaclay.bibs.tetris.control.player.PlayerType;
import fr.upsaclay.bibs.tetris.model.tetromino.TetrominoProvider;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * An interface for the main controller
 * <p>
 * This controller is in charge of game options and game creation
 *
 * @author Viviane Pons
 */

public interface GameManager {

    int DEFAULT_LINES = 20;
    int DEFAULT_COLS = 10;

    TetrisMode DEFAULT_MODE = TetrisMode.MARATHON;
    TetrominoProvider DEFAULT_PROVIDER = TetrominoProvider.randomTetrominoProvider();
    PlayerType DEFAULT_PLAYER_TYPE = PlayerType.HUMAN;

    // Configuration

    /**
     * Initialize the game Manager
     * <p>
     * Should set the necessary fields with their default values
     * <p>
     * Note: the player is not created at initilization
     * <p>
     * In visual mode, this is where the game frame can be launched
     */
    void initialize();

    /**
     * Sets the game mode
     *
     * @param mode a TetrisMode
     */
    void setGameMode(TetrisMode mode);

    /**
     * Return the game mode
     *
     * @return a TetrisMode
     */
    TetrisMode getGameMode();

    /**
     * Sets the tetromino provider
     *
     * @param provider a TetrominoProvider
     */
    void setTetrominoProvider(TetrominoProvider provider);

    /**
     * Return the current tetromino povider
     *
     * @return the TetrominoProvider
     */
    TetrominoProvider getTetrominoProvider();

    /**
     * Sets the player type
     *
     * @param playerType a PlayerType
     */
    void setPlayerType(PlayerType playerType);

    /**
     * Return the current player type
     *
     * @return a PlayerType
     */
    PlayerType getPlayerType();

    /**
     * Return the number of lines
     *
     * @return the number of lines
     */
    int getNumberOfLines();

    /**
     * Return the number of cols
     *
     * @return the number of cols
     */
    int getNumberOfCols();

    // Actions


    /**
     * Creates a player with the correct player type
     * <p>
     * The specific class of the player will depend of the GameType: Simple or Visual
     * <p>
     * If there is no implementation for player type in this game type, throws an
     * UnsupportedOperationException
     */
    void createPlayer();

    /**
     * Return the player
     *
     * @return a GamePlayer
     */
    GamePlayer getPlayer();

    /**
     * Load a new empty game
     * <p>
     * This creates a new player if needed and initialize the player with the new game
     * <p>
     * It does not start the player, so the player should be on "pause" i.e. not active
     */
    void loadNewGame();

    /**
     * Loads a game read from a file
     * <p>
     * This creates a new player if needed and initialize the player with the new game
     * <p>
     * It does not start the player, so the player should be on "pause" i.e. not active
     * <p>
     * A game file contains information about the game state (grid, score, level, etc.)
     * <p>
     * It does not contain: the tetromino provider, the held tetromino saved in game
     * <p>
     * See project description for file format
     *
     * @param file a file
     * @throws FileNotFoundException if the file cannot be read
     * @throws IOException           if there is an error while scanning the file following the file format
     */
    void loadFromFile(File file) throws FileNotFoundException, IOException;

    /**
     * starts the player (i.e. the actual game)
     */
    void startPlayer();

    /**
     * pause the player
     */
    void pausePlayer();

    /**
     * Save the game
     * <p>
     * A game file contains information about the game state (grid, score, level, etc.)
     * <p>
     * It does not contain: the tetromino provider, the held tetromino saved in game
     * <p>
     * See project description for file format
     *
     * @param file a file
     * @throws FileNotFoundException if one cannot write in the file
     */
    void save(File file) throws FileNotFoundException;

    /**
     * Return an instance of GameManager depending on GameType
     *
     * @param type the gameType (SIMPLE or VISUAL)
     * @return a GameManager
     */
    static GameManager getGameManager(GameType type) {
        switch (type) {
            case SIMPLE -> {
                return new SimpleGameManager();
            }
            case VISUAL -> {
                return new VisualGameManager();
            }
        }

        return null;
    }


}