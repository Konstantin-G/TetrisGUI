package tetris;

import tetris.tetriminos.Tetriminos;
import tetris.tetriminos.TetriminosFactory;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by garkusha on 26.2.2015.
 *
 */
public class Serialization implements Externalizable {
    static final long serialVersionUID = 1L;
    private static final TetriminosFactory TETRIMINOS_FACTORY = TetriminosFactory.getTetriminosInstance();
    private char[][] matrix;
    private int score;
    private Tetriminos falling;
    private Tetriminos next;

    public Serialization() {
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        matrix = PlayThread.matrix;
        score = PlayThread.getScore();
        falling = PlayThread.getFalling();
        next = TETRIMINOS_FACTORY.getNextTetriminos();

        out.writeObject(falling);
        out.writeObject(next);
        out.writeObject(matrix);
        out.writeInt(score);

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        falling = (Tetriminos)in.readObject();
        next = (Tetriminos)in.readObject();
        matrix = (char[][]) in.readObject();
        score = in.readInt();

        PlayThread.matrix = matrix;
        PlayThread.setScore(score);
        TETRIMINOS_FACTORY.addLoadedTetriminos(falling, next);
    }
}
