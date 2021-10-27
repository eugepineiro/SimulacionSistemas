package ar.edu.itba.ss.models;

public class WallContact extends Point2D {

    public WallContact(double x, double y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "WallContact{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}
