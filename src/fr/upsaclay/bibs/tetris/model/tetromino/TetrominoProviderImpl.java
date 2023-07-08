package fr.upsaclay.bibs.tetris.model.tetromino;

import java.util.List;

public class TetrominoProviderImpl implements TetrominoProvider {
    public final List<Tetromino> tetrominos;
    public TetrominoProviderImpl(List<Tetromino> tetrominos) {
        this.tetrominos = tetrominos;
    }

    private int currentIndex = 0;
    @Override
    public boolean hasNext() {
        return currentIndex < tetrominos.size();
    }

    @Override
    public Tetromino next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more tetrominos available");
        }
        Tetromino currentTetromino = tetrominos.get(currentIndex);
        currentIndex = currentIndex+1;
        return currentTetromino;
    }

    @Override
    public Tetromino showNext(int n) {
        if (currentIndex + n + 1 > tetrominos.size()) {
            return null;
        }
        return tetrominos.get(currentIndex + n);
    }

}
