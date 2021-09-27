package ar.edu.itba.ss.models;

import ar.edu.itba.ss.Particle;

public class AcceleratedParticle extends Particle {

    private ParticleType                                                type;
    private double                                                      vx;
    private double                                                      vy;
    private double                                                      mass;
    private double                                                      forceX;
    private double                                                      forceY;
    private TetraFunction<Integer, Double, Double, Double, Double>      derivativeFunctionX;
    private TetraFunction<Integer, Double, Double, Double, Double>      derivativeFunctionY;

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
        this.derivativeFunctionX = (order, pos, vel, m) -> {
            switch (order) {
                case 0:
                    return pos;
                case 1:
                    return vel;
            }
            return (double) 0;
        };
        this.derivativeFunctionY = (order, pos, vel, m) -> {
            switch (order) {
                case 0:
                    return pos;
                case 1:
                    return vel;
            }
            return (double) 0;
        };
    }

    public AcceleratedParticle(ParticleType type, Particle particle, double vx, double vy, double mass, double forceX, double forceY, TetraFunction<Integer, Double, Double, Double, Double> derivativeFunctionX, TetraFunction<Integer, Double, Double, Double, Double> derivativeFunctionY) {
        this(type, particle.getId(), particle.getX(), particle.getY(), particle.getRadius(), vx, vy, mass, forceX, forceY, derivativeFunctionX, derivativeFunctionY);
    }

    public AcceleratedParticle(ParticleType type, long id, double x, double y, double radius, double vx, double vy, double mass, double forceX, double forceY, TetraFunction<Integer, Double, Double, Double, Double> derivativeFunctionX, TetraFunction<Integer, Double, Double, Double, Double> derivativeFunctionY) {
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

    public double getPositionDerivativeX(int order) {
        return derivativeFunctionX.apply(order, x, vx, mass);
    }

    public double getPositionDerivativeY(int order) {
        return derivativeFunctionX.apply(order, y, vy, mass);
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

    public TetraFunction<Integer, Double, Double, Double, Double> getDerivativeFunctionX() {
        return derivativeFunctionX;
    }
    public void setDerivativeFunctionX(TetraFunction<Integer, Double, Double, Double, Double> derivativeFunctionX) {
        this.derivativeFunctionX = derivativeFunctionX;
    }
    public AcceleratedParticle withDerivativeFunctionX(TetraFunction<Integer, Double, Double, Double, Double> derivativeFunctionX) {
        setDerivativeFunctionX(derivativeFunctionX);
        return this;
    }

    public TetraFunction<Integer, Double, Double, Double, Double> getDerivativeFunctionY() {
        return derivativeFunctionY;
    }
    public void setDerivativeFunctionY(TetraFunction<Integer, Double, Double, Double, Double> derivativeFunctionY) {
        this.derivativeFunctionY = derivativeFunctionY;
    }
    public AcceleratedParticle withDerivativeFunctionY(TetraFunction<Integer, Double, Double, Double, Double> derivativeFunctionY) {
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
