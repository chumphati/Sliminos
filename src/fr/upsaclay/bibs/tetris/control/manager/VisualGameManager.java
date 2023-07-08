package fr.upsaclay.bibs.tetris.control.manager;

import fr.upsaclay.bibs.tetris.control.player.PlayerType;
import fr.upsaclay.bibs.tetris.control.player.VisualGamePlayer;
import fr.upsaclay.bibs.tetris.model.grid.TetrisGrid;
import fr.upsaclay.bibs.tetris.model.grid.TetrisGridImpl;
import fr.upsaclay.bibs.tetris.view.MainFrame;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class VisualGameManager extends Game{
    @Override
    public void initialize() {
        super.initialize();

        // launch game frame
        new MainFrame(this);
    }

    @Override
    public void createPlayer() {
        if (this.playerType == null) {
            throw new UnsupportedOperationException("unknown player type");
        }

        if (this.playerType == PlayerType.AI)
            throw new UnsupportedOperationException("unimplemented");


        TetrisGrid grid = new TetrisGridImpl(this.nbLines, this.nbCols);

        if (this.playerType == PlayerType.HUMAN) {
            this.player = new VisualGamePlayer();
            this.player.initialize(grid,  this.scoreComputer, this.provider);
        }
    }

}