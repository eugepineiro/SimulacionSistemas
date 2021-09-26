package ar.edu.itba.ss;

import java.util.Objects;

public class Particle {
    protected long id;
    protected double x;
    protected double y;
    protected double radius;

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
    public void setId(long id) {
        this.id = id;
    }
    public Particle withId(long id) {
        setId(id);
        return this;
    }

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public Particle withX(double x) {
        setX(x);
        return this;
    }

    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public Particle withY(double y) {
        setY(y);
        return this;
    }

    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }
    public Particle withRadius(double radius) {
        setRadius(radius);
        return this;
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

