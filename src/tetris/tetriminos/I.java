package tetris.tetriminos;

import tetris.Coordinates;
import tetris.PlayThread;

/**
 * Created by Konstantin Garkusha on 2/6/15.
 *
 */
class I extends Tetriminos {
    public I(int position) {
        super(position, '5');
        this.originX = 5;
        this.originY = 0;
        coordinates = new Coordinates[COUNT_OF_SQUARE_BLOCKS];
        for (int i = 0; i < COUNT_OF_SQUARE_BLOCKS; i++) {
            coordinates[i] = new Coordinates(originY,originX);
        }
        rotate();
    }

    @Override
    public void rotateDirection(String direction) {
        position = position == 0 ? 1 : 0;
        rotate();
    }

    @Override
    public synchronized void setPosition0(){
        if (!(PlayThread.MATRIX[originY][this.originX + 2] != ' ' && PlayThread.MATRIX[originY][originX] != ' ')) {
            if (PlayThread.MATRIX[originY][this.originX + 2] != ' ') {
                this.moveToLeft();
            }
            if (PlayThread.MATRIX[originY][originX] != ' ') {
                this.moveToRight();
            }
            int index = 0;
            if (!this.isTouch()) {                                                     //  .#..
                for (Coordinates c : coordinates) {                                    //  .#..
                    c.setY(originY + index++);                                         //  .#..
                    c.setX(originX + 1);                                               //  .#..
                }
            }
        }
    }

    @Override
    public synchronized void setPosition1(){
        if (!(PlayThread.MATRIX[originY][originX + 3] != ' ' && PlayThread.MATRIX[originY][originX] != ' ')) {
            if (PlayThread.MATRIX[originY][originX + 3] != ' ') {
                this.moveToLeft();
                if (PlayThread.MATRIX[originY][originX + 3] != ' ') {
                    this.moveToLeft();
                }
            }
            if (PlayThread.MATRIX[originY][originX] != ' ') {
                this.moveToRight();
            }
            int index = 0;
            if (!this.isTouch()) {                                                     //  ....
                for (Coordinates c : coordinates) {                                    //  ####
                    c.setY(originY + 1);                                               //  ....
                    c.setX(originX + index++);                                         //  ....
                }
            }
        }
    }

    @Override
    public void setPosition2() {

    }

    @Override
    public void setPosition3() {

    }
}
