package tetris.tetriminos;

import tetris.Coordinates;
import tetris.PlayThread;

/**
 *
 * @author Konstantin Garkusha
 */
class Z extends Tetriminos{
    public Z(int position) {
        super(position, '6');
        this.originX = 6;
        this.originY = 0;
        coordinates = new Coordinates[COUNT_OF_SQUARE_BLOCKS];
        for (int i = 0; i < COUNT_OF_SQUARE_BLOCKS; i++) {
            coordinates[i] = new Coordinates(originY,originX);
        }
        rotate();
    }

    @Override
    protected synchronized void setPosition0(){
        if (!(PlayThread.matrix[originY][originX + 2] != ' ' && PlayThread.matrix[originY][originX] != ' ')) {
            if (PlayThread.matrix[originY][originX + 2] != ' ') {
                this.moveToLeft();
            }
            if (PlayThread.matrix[originY][originX] != ' ') {
                this.moveToRight();
            }
            if (!this.isTouch()) {
                coordinates[0].setY(originY + 2);
                    coordinates[0].setX(originX + 2);                                //  ....
                coordinates[1].setY(originY + 2);                                    //  ##..
                    coordinates[1].setX(originX + 1);                                //  .##.
                coordinates[2].setY(originY + 1);                                    //  ....
                    coordinates[2].setX(originX + 1);
                coordinates[3].setY(originY + 1);
                    coordinates[3].setX(originX);
            }
        }
    }

    @Override
    protected synchronized void setPosition1(){
        if (!(PlayThread.matrix[originY][originX + 1] != ' ' && PlayThread.matrix[originY][originX - 1] != ' ')) {
            if (PlayThread.matrix[originY][originX + 1] != ' ') {
                this.moveToLeft();
            }
            if (PlayThread.matrix[originY][originX - 1] != ' ') {
                this.moveToRight();
            }
            if (!this.isTouch()) {
                coordinates[0].setY(originY);
                    coordinates[0].setX(originX + 1);                                //  .#..
                coordinates[1].setY(originY + 1);                                    //  ##..
                    coordinates[1].setX(originX + 1);                                //  #...
                coordinates[2].setY(originY + 1);                                    //  ....
                    coordinates[2].setX(originX);
                coordinates[3].setY(originY + 2);
                    coordinates[3].setX(originX);
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