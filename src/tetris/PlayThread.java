package tetris;


import tetris.tetriminos.Tetriminos;
import tetris.tetriminos.TetriminosFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Konstantin Garkusha on 2/6/15.
 * ===============================
 * | Game is under construction! |
 * ===============================
 */
public class PlayThread extends Thread {
    private static final Map<Integer, Integer> LEVEL_SPEED = new HashMap<>(20);
    static {
        for (int level = 0; level < 20; level++) {
            LEVEL_SPEED.put(level, 790 - level * 40);
        }
    }
    public static final int SIDE_X = 10 + 4;                                 // 4 for walls
    public static final int SIDE_Y = 20 + 2 + 1;                             // 2 are invisible and 1 to bottom
    public static final char[][] MATRIX = new char[SIDE_Y][SIDE_X];

    private static boolean isPlaying = true;
    private static Tetriminos falling;
    private static int score = 0;
    private static int level = 0;
    private static long speed;                                       // GameSpeed
    private static final long DROP_SPEED = 1;                       // Drop speed

    private static final TetriminosFactory TETRIMINOS_FACTORY = new TetriminosFactory();
    private static final char[][] NEXT = new char[4][4];

    public static char[][] getNext() {
        return NEXT;
    }

    public static int getLevel() {
        return level;
    }

    public static int getScore() {
        return score;
    }

    public static Tetriminos getFalling() {
        return falling;
    }

    public void run() {

        // make right matrix
        fillTheMatrix();
        // Object to call TetriminosFactory methods
        TetriminosFactory tetriminosFactory = new TetriminosFactory();
        //add first Tetriminos to queue
        tetriminosFactory.addFirstTetriminos();

        // main logic
        while(isPlaying){
            // get next Tetriminos from queue
            falling = tetriminosFactory.getTetriminosFromTop();
            fillNextTetriminos();
            //check game level
            level = getNewLevel();
            // update game speed by level
            speed = LEVEL_SPEED.get(level);

            // tetriminos is falling
            while (true){
                // game speed (waiting between step)
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // If tetriminos on the bottom or on another tetriminos
                if (falling.isTouch()) {
                    falling.setDown(true);
                    break;
                }
                // fall tetriminos down per cell (one step)
                falling.moveDownPerCell();
                Tetris.basicPanel.repaint();
            }
            //add fallen tetriminos to the MATRIX
            for (int coord = 0; coord < falling.coordinates.length; coord++) {
                int x = falling.coordinates[coord].getX();
                int y = falling.coordinates[coord].getY();
                MATRIX[y][x] = falling.TETRIMINOS_CHAR;
            }
            falling = null;
            //Check the MATRIX for the filled lines
            try {
                checkTheMatrix();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // If top line have tetriminos -> stop the game
            for (int x = 2; x < SIDE_X - 2; x++) {
                if (MATRIX[2][x] != ' ') {
                    // if have found stop the game and break the loop
                    isPlaying = false;
                    break;
                }
            }
        }
    }

    //right fill the MATRIX
    private static void fillTheMatrix(){                            //    ||    ||
        for (int y = 0; y < SIDE_Y; y++) {                          //    ||    ||
            for (int x = 2; x < SIDE_X - 2; x++) {                  //    ||    ||
                MATRIX[y][x] = ' ';                                 //    ||    ||
            }                                                       //    --------
            MATRIX[y][0] = '|';
            MATRIX[y][1] = '|';
            MATRIX[y][SIDE_X - 1] = '|';
            MATRIX[y][SIDE_X - 2] = '|';
        }

        for (int x = 0; x < SIDE_X; x++) {
            MATRIX[SIDE_Y -1][x] = '-';
        }
    }

    //Check the MATRIX for the fill line and fill it with transitive symbols (for delete animation)
    private static void checkTheMatrix() throws InterruptedException {
        // line counter for count how many scores you get
        int countOfFilledLine = 0;
        for (int y = SIDE_Y - 2; y >= 0;) {
            boolean isLineFill = true;
            // Check the line from bottom to top line
            for (int x = 2; x < SIDE_X - 2; x++) {
                // If find void square -> skip whole line
                if (MATRIX[y][x] == ' ') {
                    isLineFill = false;
                    y--;
                    break;
                }
            }
            // if found filled line
            if (isLineFill) {
                // Fill whit transitive symbols (for animation)
                for (int x = 2; x < SIDE_X - 2; x++) {
                    MATRIX[y][x] = '.';
                }
                countOfFilledLine++;
                // Waiting (for animation)
                Tetris.basicPanel.repaint();
                Thread.sleep(700);
                //  Delete the line and fall down all above this line
                deleteTheLine(y);
            }
        }
        //increase scores depending on countOfFilledLine
        switch (countOfFilledLine) {
            case 1:
                score = score + 100;
                break;
            case 2:
                score = score + 200;
                break;
            case 3:
                score = score + 600;
                break;
            case 4:
                score = score + 800;
                break;
            default:
                // do nothing
        }
    }

    //Delete the line and fall down all above this line
    private static void deleteTheLine(int line) {
        for (int y = line; y > 0 ; y--) {
            // Falling one square down a line
            System.arraycopy(MATRIX[y - 1], 2, MATRIX[y], 2, SIDE_X - 2);
            Tetris.basicPanel.repaint();
            // Check next line for the void, if true = break
            int count = 0;
            for (int x = 2; x < SIDE_X - 2; x++) {
                if (MATRIX[y - 1][x] != ' ') {
                    break;
                } else
                    count++;
            }
            // If line is void = break falling down loop (Do not need to let down a void line)
            if (count == SIDE_X - 4)
                y = 0;
        }
    }

    private static void fillNextTetriminos(){
        char cross = ' ';
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (TETRIMINOS_FACTORY.hasTetriminos()) {
                    Tetriminos tetriminos = TETRIMINOS_FACTORY.getNextTetriminos();
                    for (Coordinates c : tetriminos.coordinates) {
                        int tetX = c.getX() - tetriminos.getOriginX();
                        int tetY = c.getY() - tetriminos.getOriginY();
                        if (x == tetX && y == tetY) {
                            cross = tetriminos.TETRIMINOS_CHAR;
                            break;
                        } else cross = ' ';
                    }
                }
                NEXT[y][x] = cross;
            }
        }
    }

    // change game speed to DROP_SPEED
    public static void dropDown(){ speed = DROP_SPEED; }

    // return level depending on scores
    private static int getNewLevel(){ return score / 1000; }

}



