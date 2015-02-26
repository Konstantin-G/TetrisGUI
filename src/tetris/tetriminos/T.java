package tetris.tetriminos;

import tetris.Coordinates;
import tetris.PlayThread;

/**
 * Created by Konstantin Garkusha on 2/6/15.
 *
 */
class T extends Tetriminos {
    public T(int position) {
        super(position, '0');
        this.originX = 6;
        this.originY = 0;
        coordinates = new Coordinates[COUNT_OF_SQUARE_BLOCKS];
        for (int i = 0; i < COUNT_OF_SQUARE_BLOCKS; i++) {
            coordinates[i] = new Coordinates(originY,originX);
        }
        rotate();
    }

    @Override
    public synchronized void setPosition0(){
        if (!(PlayThread.matrix[originY][this.originX + 2] != ' ' && PlayThread.matrix[originY][originX] != ' ')) {
            if (PlayThread.matrix[originY][this.originX + 2] != ' ') {
                this.moveToLeft();
            }
            if (PlayThread.matrix[originY][originX] != ' ') {
                this.moveToRight();
            }

            if (!this.isTouch()) {
                coordinates[0].setY(originY);
                    coordinates[0].setX(originX + 1);                                //  .#..
                coordinates[1].setY(originY + 1);                                    //  .##.
                    coordinates[1].setX(originX + 1);                                //  .#..
                coordinates[2].setY(originY + 2);                                    //  ....
                    coordinates[2].setX(originX + 1);
                coordinates[3].setY(originY + 1);
                    coordinates[3].setX(originX + 2);
            }
        }
    }

    @Override
    public synchronized void setPosition1(){
        if (!(PlayThread.matrix[originY][originX + 2] != ' ' && PlayThread.matrix[originY][originX] != ' ')) {
            if (PlayThread.matrix[originY][originX + 2] != ' ') {
                this.moveToLeft();
            }
            if (PlayThread.matrix[originY][originX] != ' ') {
                this.moveToRight();
            }

            if (!this.isTouch()) {
                coordinates[0].setY(originY + 1);
                    coordinates[0].setX(originX + 2);                                //  ....
                coordinates[1].setY(originY + 1);                                    //  ###.
                    coordinates[1].setX(originX + 1);                                //  .#..
                coordinates[2].setY(originY + 1);                                    //  ....
                    coordinates[2].setX(originX);
                coordinates[3].setY(originY + 2);
                    coordinates[3].setX(originX + 1);
            }
        }
    }

    @Override
    public synchronized void setPosition2(){
        if (!(PlayThread.matrix[originY][originX + 1] != ' ' && PlayThread.matrix[originY][originX - 1] != ' ')) {
            if (PlayThread.matrix[originY][originX + 1] != ' ') {
                this.moveToLeft();
            }
            if (PlayThread.matrix[originY][originX - 1] != ' ') {
                this.moveToRight();
            }

            if (!this.isTouch()) {
                coordinates[0].setY(originY + 2);
                    coordinates[0].setX(originX + 1);                                //  .#..
                coordinates[1].setY(originY + 1);                                    //  ##..
                    coordinates[1].setX(originX + 1);                                //  .#..
                coordinates[2].setY(originY);                                        //  ....
                    coordinates[2].setX(originX + 1);
                coordinates[3].setY(originY + 1);
                    coordinates[3].setX(originX);
            }
        }
    }

    @Override
    public synchronized void setPosition3(){
        if (!(PlayThread.matrix[originY][originX + 2] != ' ' && PlayThread.matrix[originY][originX] != ' ')) {
            if (PlayThread.matrix[originY][originX + 2] != ' ') {
                this.moveToLeft();
            }
            if (PlayThread.matrix[originY][originX] != ' ') {
                this.moveToRight();
            }

            if (!this.isTouch()) {
                coordinates[0].setY(originY + 1);
                    coordinates[0].setX(originX);                                    //  .#..
                coordinates[1].setY(originY + 1);                                    //  ###.
                    coordinates[1].setX(originX + 1);                                //  ....
                coordinates[2].setY(originY + 1);                                    //  ....
                    coordinates[2].setX(originX + 2);
                coordinates[3].setY(originY);
                    coordinates[3].setX(originX + 1);
            }
        }
    }
}
