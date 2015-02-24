package tetris.tetriminos;

/**
 * Created by Konstantin Garkusha on 18.2.2015.
 *
 */
interface Movable {
    public void rotateDirection(String direction);

    public void moveDownPerCell();
    public void moveToLeft();
    public void moveToRight();
}
