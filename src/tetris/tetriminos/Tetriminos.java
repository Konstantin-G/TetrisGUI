package tetris.tetriminos;

import tetris.Coordinates;
import tetris.PlayThread;

import java.io.Serializable;

/**
 * Created by Konstantin Garkusha on 2/6/15.
 *
 */
public abstract class Tetriminos implements Serializable{
    static final long serialVersionUID = 1L;
    final int COUNT_OF_SQUARE_BLOCKS = 4;
    public final char TETRIMINOS_CHAR;
    private boolean isDown;
    public Coordinates[] coordinates;

    int originX;
    int originY;
    private int position;

    Tetriminos(int position, char tetriminosChar) {
        this.position = position;
        this.TETRIMINOS_CHAR = tetriminosChar;
        this.isDown = false;
    }

    public int getPosition() {
        return position;
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public void setDown(boolean isDown) {
        this.isDown = isDown;
    }

    public boolean getDown() {
        return isDown;
    }

    // rotate Tetriminos CLOCKWISE or ANTICLOCKWISE
    public void rotateDirection(String direction){
        if ("CLOCKWISE".equals(direction)) {
            if (this.position != 3) {
                this.position++;
            } else
                this.position = 0;
        } else if("COUNTERCLOCKWISE".equals(direction)) {
            if (this.position != 0) {
                this.position--;
            } else
                this.position = 3;
        }
        //set new position
        rotate();
    }
    protected abstract void setPosition0();
    protected abstract void setPosition1();
    protected abstract void setPosition2();
    protected abstract void setPosition3();

    //set new position (rotate to new or previous position)
    synchronized void rotate(){

        switch (this.position) {
            case 0:
                this.setPosition0();
                break;
            case 1:
                this.setPosition1();
                break;
            case 2:
                this.setPosition2();
                break;
            case 3:
                this.setPosition3();
                break;
            default:
                throw new IllegalArgumentException("wrong position");
        }
    }

    // Drops per one cell
    public void moveDownPerCell(){
        for (int i = 0; i < this.COUNT_OF_SQUARE_BLOCKS; i++) {
            this.coordinates[i].setY(this.coordinates[i].getY() + 1);
        }
        this.originY++;
    }

    // Does Tetriminos can move to right?
    boolean canTetriminosMoveToRight(){
        for (int i = 0; i < this.COUNT_OF_SQUARE_BLOCKS; i++) {
            int x = this.coordinates[i].getX();
            int y = this.coordinates[i].getY();
            if (PlayThread.matrix[y][x + 1] != ' ') {
                return false;
            }
        }
        return true;
    }

    // Does Tetriminos can move to left?
    boolean canTetriminosMoveToLeft(){

        for (int i = 0; i < this.COUNT_OF_SQUARE_BLOCKS; i++) {
            int x = this.coordinates[i].getX();
            int y = this.coordinates[i].getY();
            if (PlayThread.matrix[y][x - 1] != ' ') {
                return false;
            }
        }
        return true;
    }

    // move to left per cell
    public void moveToLeft(){
        if (this.canTetriminosMoveToLeft()) {
            for (int i = 0; i < this.COUNT_OF_SQUARE_BLOCKS; i++) {
                int x = this.coordinates[i].getX();
                this.coordinates[i].setX(x - 1);
            }
            this.originX--;
        }
    }

    // move to left per cell
    public void moveToRight(){
        if (this.canTetriminosMoveToRight()) {
            for (int i = 0; i < this.COUNT_OF_SQUARE_BLOCKS; i++) {
                int x = this.coordinates[i].getX();
                this.coordinates[i].setX(x + 1);
            }
            this.originX++;
        }
    }

    // does Tetriminos touch bottom or have fallen Tetriminos?
    public boolean isTouch(){
        for (Coordinates coordinate : this.coordinates) {
            int x = coordinate.getX();
            int y = coordinate.getY();
            if (PlayThread.matrix[y + 1][x] != ' ') {
                return true;
            }
        }
        return false;
    }
}
