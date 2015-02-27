package tetris.tetriminos;

import tetris.Coordinates;

/**
 * Created by Konstantin Garkusha on 2/6/15.
 *
 */
class O extends Tetriminos {
    public O() {
        super(0, '3');
        this.originX = 5;
        this.originY = 0;
        this.coordinates = new Coordinates[COUNT_OF_SQUARE_BLOCKS];
        coordinates[0] = new Coordinates(originY + 1, originX + 1);
        coordinates[1] = new Coordinates(originY + 1, originX + 2);
        coordinates[2] = new Coordinates(originY + 2, originX + 1);
        coordinates[3] = new Coordinates(originY + 2, originX + 2);
    }

    @Override
    public void rotateDirection(String direction) {
        // do nothing this tetriminos can't rotate
    }

    @Override
    public void setPosition0() {
        // do nothing this tetriminos can't rotate
    }

    @Override
    public void setPosition1() {
        // do nothing this tetriminos can't rotate
    }

    @Override
    public void setPosition2() {
        // do nothing this tetriminos can't rotate
    }

    @Override
    public void setPosition3() {
        // do nothing this tetriminos can't rotate
    }
}
