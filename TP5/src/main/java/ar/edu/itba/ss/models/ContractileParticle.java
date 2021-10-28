package ar.edu.itba.ss.models;

import ar.edu.itba.ss.Particle;

public class ContractileParticle extends Particle {

    private double vx;
    private double vy;

    public ContractileParticle() {
        this.id = 0;
        this.x = 0;
        this.y = 0;
        this.radius = 0;
        this.vx = 0;
        this.vy = 0;
    }

    public ContractileParticle(Particle particle, double speed, double angle) {
        this(particle.getId(), particle.getX(), particle.getY(), particle.getRadius(), speed, angle);
    }

    public ContractileParticle(long id, double x, double y, double radius, double speed, double angle) {
        super(id, x, y, radius);
        this.vx = speed * Math.cos(angle);
        this.vy = speed * Math.sin(angle);
    }

    public double getSpeed() {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
    }

    public double getAngle() {
        return (vx == 0)? ((vy >= 0)? Math.PI/2.0 : Math.PI*(3/2.0)): Math.tan(vy/vx);
    }

    public ContractileParticle clone() {
        return new ContractileParticle()
            .withId(id)
            .withX(x)
            .withY(y)
            .withRadius(radius)
            .withVx(vx)
            .withVy(vy)
            ;
    }

    public double distance(double x, double y) {
        return Math.sqrt(Math.pow(x - getX(),2) + Math.pow(y - getY(),2));
    }

    public double distance(ContractileParticle other) {
        return distance(other.getX(), other.getY());
    }

    public double distance(Point2D point2D) {
        return distance(point2D.getX(), point2D.getY());
    }

    public boolean overlapsWith(ContractileParticle other) {
        return distance(other) < (radius + other.getRadius());
    }

    public boolean overlapsWith(WallContact wallContact) {
        return distance(wallContact) < radius;
    }

    //////////////// Autogenerated /////////////////

    public ContractileParticle withId(long id) {
        setId(id);
        return this;
    }
    public ContractileParticle withX(double x) {
        setX(x);
        return this;
    }
    public ContractileParticle withY(double y) {
        setY(y);
        return this;
    }
    public ContractileParticle withRadius(double radius) {
        setRadius(radius);
        return this;
    }

    public double getVx() {
        return vx;
    }
    public void setVx(double vx) {
        this.vx = vx;
    }
    public ContractileParticle withVx(double vx) {
        setVx(vx);
        return this;
    }

    public double getVy() {
        return vy;
    }
    public void setVy(double vy) {
        this.vy = vy;
    }
    public ContractileParticle withVy(double vy) {
        setVy(vy);
        return this;
    }

    @Override
    public String toString() {
        return "ContractileParticle{" +
            "id=" + id +
            ", x=" + x +
            ", y=" + y +
            ", radius=" + radius +
            ", vx=" + vx +
            ", vy=" + vy +
            '}';
    }
}
