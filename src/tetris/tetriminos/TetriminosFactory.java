package tetris.tetriminos;

import tetris.PlayThread;

import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Konstantin Garkusha on 12.2.2015.
 *
 */
public class TetriminosFactory {
    private static final TetriminosFactory TETRIMINOS_FACTORY = new TetriminosFactory();

    //Storing Tetriminos here
    private static final Queue<Tetriminos> TETRIMINOS_QUEUE = new ArrayBlockingQueue<>(3);

    private TetriminosFactory() {
    }

    public static TetriminosFactory getTetriminosInstance() {
        return TETRIMINOS_FACTORY;
    }

    // Do we have Tetriminos in
    public boolean hasTetriminos() {
        return !TETRIMINOS_QUEUE.isEmpty();
    }

    // get instance of Tetriminos
    public Tetriminos getTetriminosFromTop() {
        // add new Tetriminos, who will be "next" on display
        TETRIMINOS_QUEUE.add(getRandomFigure());
        return TETRIMINOS_QUEUE.poll();
    }

    //add first Tetriminos to queue
    public void addFirstTetriminos(){
        TETRIMINOS_QUEUE.add(getRandomFigure());
    }

    //add loaded Tetriminos to queue
    public void addLoadedTetriminos(Tetriminos ... tetriminos){
        TETRIMINOS_QUEUE.clear();
        // add Tetriminos to queue
        Collections.addAll(TETRIMINOS_QUEUE, tetriminos);
        PlayThread.setFalling(getTetriminosFromTop());
        // refresh "next" field
        PlayThread.fillNextTetriminos();
    }

    // get next Tetriminos, which you can seen on display
    public Tetriminos getNextTetriminos(){
        return TETRIMINOS_QUEUE.element();
    }

    //make random instance of Tetriminos
    private Tetriminos getRandomFigure() {
        int randomTetriminos = (int) (Math.random()*7);
        int randomPosition = (int) (Math.random()*4);
        if (randomTetriminos > 3) {
            //Tetriminos S, Z, I haven't position 2 and 3, so here we will change 2 to 0 and 3 to 1
            randomPosition = randomPosition == 2 ? 0 : randomPosition == 3 ? 1 : randomPosition;
        }
        switch (randomTetriminos) {
            case 0:
                return new T(randomPosition);
            case 1:
                return new J(randomPosition);
            case 2:
                return new L(randomPosition);
            case 3:
                return new O();
            case 4:
                return new S(randomPosition);
            case 5:
                return new I(randomPosition);
            case 6:
                return new Z(randomPosition);
            default:
                throw new IllegalArgumentException("wrong random figure") ;
        }
    }


}
