package tetris;

import java.io.Serializable;

/**
 *
 * @author Konstantin Garkusha
 */
public class Coordinates implements Serializable{
    private int y;
    private int x;

    public Coordinates(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    @Override
    public int hashCode() {
        return 31 + ( y / x);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinates))
            return false;
        Coordinates c = (Coordinates) obj;
        return (this.y == c.y) && (this.x == c.x);
    }
}
