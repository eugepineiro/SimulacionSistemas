package ar.edu.itba.ss.models;

import ar.edu.itba.ss.Particle;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class AcceleratedParticle extends Particle {

    private ParticleType                                                                    type;
    private double                                                                          vx;
    private double                                                                          vy;
    private double                                                                          mass;
    private double                                                                          forceX;
    private double                                                                          forceY;
    @JsonIgnore
    private TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double>    derivativeFunctionX;
    @JsonIgnore
    private TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double>    derivativeFunctionY;

    public AcceleratedParticle() {
        this.id = 0;
        this.x = 0;
        this.y = 0;
        this.radius = 0;
        this.vx = 0;
        this.vy = 0;
        this.mass = 0;
        this.forceX = 0;
        this.forceY = 0;
        this.derivativeFunctionX = (order, curr, all) -> {
            switch (order) {
                case 0:
                    return curr.getX();
                case 1:
                    return curr.getVx();
            }
            return (double) 0;
        };
        this.derivativeFunctionY = (order, curr, all) -> {
            switch (order) {
                case 0:
                    return curr.getY();
                case 1:
                    return curr.getVy();
            }
            return (double) 0;
        };
    }

    public AcceleratedParticle(ParticleType type, Particle particle, double vx, double vy, double mass, double forceX, double forceY, TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> derivativeFunctionX, TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> derivativeFunctionY) {
        this(type, particle.getId(), particle.getX(), particle.getY(), particle.getRadius(), vx, vy, mass, forceX, forceY, derivativeFunctionX, derivativeFunctionY);
    }

    public AcceleratedParticle(ParticleType type, long id, double x, double y, double radius, double vx, double vy, double mass, double forceX, double forceY, TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> derivativeFunctionX, TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> derivativeFunctionY) {
        super(id, x, y, radius);
        this.type = type;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.forceX = forceX;
        this.forceY = forceY;
        this.derivativeFunctionX = derivativeFunctionX;
        this.derivativeFunctionY = derivativeFunctionY;
    }

    public double getSpeed() {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
    }

    public double getAngle() {
        return (vx == 0)? ((vy >= 0)? Math.PI/2.0 : Math.PI*(3/2.0)): Math.tan(vy/vx);
    }

    public double getPositionDerivativeX(int order, List<AcceleratedParticle> others) {
        return derivativeFunctionX.apply(order, this, others);
    }

    public double getPositionDerivativeY(int order, List<AcceleratedParticle> others) {
        return derivativeFunctionY.apply(order, this, others);
    }

    public AcceleratedParticle clone() {
        AcceleratedParticle vp = new AcceleratedParticle();
        vp.setType(type);
        vp.setId(id);
        vp.setX(x);
        vp.setY(y);
        vp.setRadius(radius);
        vp.setVx(vx);
        vp.setVy(vy);
        vp.setMass(mass);
        vp.setForceX(forceX);
        vp.setForceY(forceY);
        vp.setDerivativeFunctionX(derivativeFunctionX);
        vp.setDerivativeFunctionY(derivativeFunctionY);

        return vp;
    }

    public double distance(AcceleratedParticle other) {
        return Math.sqrt(Math.pow(other.getX() - getX(),2) + Math.pow(other.getY() - getY(),2));
    }

    ////////////////////////////// Bureaucracy //////////////////////////////

    public AcceleratedParticle withId(long id) {
        setId(id);
        return this;
    }
    public AcceleratedParticle withX(double x) {
        setX(x);
        return this;
    }
    public AcceleratedParticle withY(double y) {
        setY(y);
        return this;
    }
    public AcceleratedParticle withRadius(double radius) {
        setRadius(radius);
        return this;
    }

    public double getVx() {
        return vx;
    }
    public void setVx(double vx) {
        this.vx = vx;
    }
    public AcceleratedParticle withVx(double vx) {
        setVx(vx);
        return this;
    }

    public double getVy() {
        return vy;
    }
    public void setVy(double vy) {
        this.vy = vy;
    }
    public AcceleratedParticle withVy(double vy) {
        setVy(vy);
        return this;
    }

    public double getMass() {
        return mass;
    }
    public void setMass(double mass) {
        this.mass = mass;
    }
    public AcceleratedParticle withMass(double mass) {
        setMass(mass);
        return this;
    }

    public double getForceX() {
        return forceX;
    }
    public void setForceX(double forceX) {
        this.forceX = forceX;
    }
    public AcceleratedParticle withForceX(double forceX) {
        setForceX(forceX);
        return this;
    }

    public double getForceY() {
        return forceY;
    }
    public void setForceY(double forceY) {
        this.forceY = forceY;
    }
    public AcceleratedParticle withForceY(double forceY) {
        setForceY(forceY);
        return this;
    }

    public TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> getDerivativeFunctionX() {
        return derivativeFunctionX;
    }
    public void setDerivativeFunctionX(TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> derivativeFunctionX) {
        this.derivativeFunctionX = derivativeFunctionX;
    }
    public AcceleratedParticle withDerivativeFunctionX(TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> derivativeFunctionX) {
        setDerivativeFunctionX(derivativeFunctionX);
        return this;
    }

    public TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> getDerivativeFunctionY() {
        return derivativeFunctionY;
    }
    public void setDerivativeFunctionY(TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> derivativeFunctionY) {
        this.derivativeFunctionY = derivativeFunctionY;
    }
    public AcceleratedParticle withDerivativeFunctionY(TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> derivativeFunctionY) {
        setDerivativeFunctionY(derivativeFunctionY);
        return this;
    }


    public ParticleType getType() {
        return type;
    }
    public void setType(ParticleType type) {
        this.type = type;
    }
    public AcceleratedParticle withType(ParticleType type) {
        setType(type);
        return this;
    }

    @Override
    public String toString() {
        return "AcceleratedParticle{" +
            "id=" + id +
            ", x=" + x +
            ", y=" + y +
            ", radius=" + radius +
            ", type=" + type +
            ", vx=" + vx +
            ", vy=" + vy +
            ", mass=" + mass +
            ", forceX=" + forceX +
            ", forceY=" + forceY +
            '}';
    }
}
