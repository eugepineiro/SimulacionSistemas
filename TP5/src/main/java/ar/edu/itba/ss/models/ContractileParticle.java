package ar.edu.itba.ss.models;

import ar.edu.itba.ss.Particle;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ContractileParticle extends Particle {

    private double vx;
    private double vy;
    private double minRadius;
    private double maxRadius;
    private double escapeSpeed;
    private double maxDesiredSpeed;
    private double beta;
    private double tau;

    public ContractileParticle() {
        this.id = 0;
        this.x = 0;
        this.y = 0;
        this.radius = 0;
        this.vx = 0;
        this.vy = 0;
        this.minRadius = 0;
        this.maxRadius = 0;
        this.escapeSpeed = 0;
        this.maxDesiredSpeed = 0;
        this.beta = 0;
        this.tau = 0;
    }

    public ContractileParticle(Particle particle, double speed, double angle, double minRadius, double maxRadius, double escapeSpeed, double maxDesiredSpeed, double beta, double tau) {
        this(particle.getId(), particle.getX(), particle.getY(), particle.getRadius(), speed, angle, minRadius, maxRadius, escapeSpeed, maxDesiredSpeed, beta, tau);
    }

    public ContractileParticle(long id, double x, double y, double radius, double speed, double angle,  double minRadius, double maxRadius, double escapeSpeed, double maxDesiredSpeed, double beta, double tau) {
        super(id, x, y, radius);
        this.vx = speed * Math.cos(angle);
        this.vy = speed * Math.sin(angle);
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.escapeSpeed = escapeSpeed;
        this.maxDesiredSpeed = maxDesiredSpeed;
        this.beta = beta;
        this.tau = tau;
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
            .withMinRadius(minRadius)
            .withMaxRadius(maxRadius)
            .withEscapeSpeed(escapeSpeed)
            .withMaxDesiredSpeed(maxDesiredSpeed)
            .withBeta(beta)
            .withTau(tau)
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

    ////////////////////////////// Bureaucracy //////////////////////////////

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

    public double getMinRadius() {
        return minRadius;
    }
    public void setMinRadius(double minRadius) {
        this.minRadius = minRadius;
    }
    public ContractileParticle withMinRadius(double minRadius) {
        setMinRadius(minRadius);
        return this;
    }

    public double getMaxRadius() {
        return maxRadius;
    }
    public void setMaxRadius(double maxRadius) {
        this.maxRadius = maxRadius;
    }
    public ContractileParticle withMaxRadius(double maxRadius) {
        setMaxRadius(maxRadius);
        return this;
    }

    public double getEscapeSpeed() {
        return escapeSpeed;
    }
    public void setEscapeSpeed(double escapeSpeed) {
        this.escapeSpeed = escapeSpeed;
    }
    public ContractileParticle withEscapeSpeed(double escapeSpeed) {
        setEscapeSpeed(escapeSpeed);
        return this;
    }

    public double getMaxDesiredSpeed() {
        return maxDesiredSpeed;
    }
    public void setMaxDesiredSpeed(double maxDesiredSpeed) {
        this.maxDesiredSpeed = maxDesiredSpeed;
    }
    public ContractileParticle withMaxDesiredSpeed(double maxDesiredSpeed) {
        setMaxDesiredSpeed(maxDesiredSpeed);
        return this;
    }

    public double getBeta() {
        return beta;
    }
    public void setBeta(double beta) {
        this.beta = beta;
    }
    public ContractileParticle withBeta(double beta) {
        setBeta(beta);
        return this;
    }

    public double getTau() {
        return tau;
    }
    public void setTau(double tau) {
        this.tau = tau;
    }
    public ContractileParticle withTau(double tau) {
        setTau(tau);
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
            ", minRadius=" + minRadius +
            ", maxRadius=" + maxRadius +
            ", escapeSpeed=" + escapeSpeed +
            ", maxDesiredSpeed=" + maxDesiredSpeed +
            ", beta=" + beta +
            ", tau=" + tau +
            '}';
    }
}
