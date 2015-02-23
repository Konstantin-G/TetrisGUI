package tetris.tetriminos;

/**
 * Created by Konstantin Garkusha on 18.2.2015.
 *
 */
interface Movable {
    public void setPosition0();
    public void setPosition1();
    public void setPosition2();
    public void setPosition3();

    public void moveDownPerCell();
    public void moveToLeft();
    public void moveToRight();
}
