package tetris.tetriminos;

import tetris.Coordinates;
import tetris.PlayThread;

/**
 *
 * @author Konstantin Garkusha
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
    protected synchronized void setPosition0(){
        if (!(PlayThread.matrix[originY][this.originX + 2] != ' ' && PlayThread.matrix[originY][originX] != ' ')) {
            if (PlayThread.matrix[originY][this.originX + 2] != ' ') {
                this.moveToLeft();
            }
            if (PlayThread.matrix[originY][originX] != ' ') {
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
    protected synchronized void setPosition1(){
        if (!(PlayThread.matrix[originY][originX + 3] != ' ' && PlayThread.matrix[originY][originX] != ' ')) {
            if (PlayThread.matrix[originY][originX + 3] != ' ') {
                this.moveToLeft();
                if (PlayThread.matrix[originY][originX + 3] != ' ') {
                    this.moveToLeft();
                }
            }
            if (PlayThread.matrix[originY][originX] != ' ') {
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
        setPosition0();
    }

    @Override
    public void setPosition3() {
        setPosition1();
    }
}
