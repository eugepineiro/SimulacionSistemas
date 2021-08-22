import java.util.Objects;

public class Particle {
    private long id;
    private double x;
    private double y;
    private double radius;

    public Particle() {}

    public Particle(long id, double x, double y, double radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public long getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                '}';
    }
}

