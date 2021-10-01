package ar.edu.itba.ss.models;

import ar.edu.itba.ss.Particle;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.function.BiFunction;

public class AcceleratedParticle extends Particle {

    static private final    int                                                                 MAX_DERIVATIVE_ORDER = 5;

    private                 ParticleType                                                        type;
    private                 double                                                              vx;
    private                 double                                                              vy;
    private                 double                                                              mass;
    private                 double                                                              forceX;
    private                 double                                                              forceY;
    private                 double[]                                                            furtherDerivativesX;
    private                 double[]                                                            furtherDerivativesY;

    @JsonIgnore
    private                 BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double>  accelerationFunctionX;
    @JsonIgnore
    private                 BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double>  accelerationFunctionY;

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
        this.furtherDerivativesX = new double[MAX_DERIVATIVE_ORDER+1-3];
        this.furtherDerivativesY = new double[MAX_DERIVATIVE_ORDER+1-3];
    }

    public AcceleratedParticle(ParticleType type, Particle particle, double vx, double vy, double mass, double forceX, double forceY, BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionX, BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionY, double[] derivativesX, double[] derivativesY) {
        this(type, particle.getId(), particle.getX(), particle.getY(), particle.getRadius(), vx, vy, mass, forceX, forceY, accelerationFunctionX, accelerationFunctionY, derivativesX, derivativesY);
    }

    public AcceleratedParticle(ParticleType type, long id, double x, double y, double radius, double vx, double vy, double mass, double forceX, double forceY, BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionX, BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionY, double[] derivativesX, double[] derivativesY) {
        super(id, x, y, radius);
        this.type = type;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.forceX = forceX;
        this.forceY = forceY;
        this.accelerationFunctionX = accelerationFunctionX;
        this.accelerationFunctionY = accelerationFunctionY;
        this.furtherDerivativesX = derivativesX;
        this.furtherDerivativesY = derivativesY;
    }

    public double getSpeed() {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
    }

    public double getAngle() {
        return (vx == 0)? ((vy >= 0)? Math.PI/2.0 : Math.PI*(3/2.0)): Math.tan(vy/vx);
    }

    public double getPositionDerivativeX(int order, List<AcceleratedParticle> others) {
        if (order <= 5) {
            switch (order) {
                case 0:
                    return x;
                case 1:
                    return vx;
                case 2:
                    return accelerationFunctionX.apply(this, others);
                default:
                    return furtherDerivativesX[order - 3];
            }
        }
        return 0;
    }

    public double getPositionDerivativeY(int order, List<AcceleratedParticle> others) {
        if (order <= 5) {
            switch (order) {
                case 0:
                    return y;
                case 1:
                    return vy;
                case 2:
                    return accelerationFunctionY.apply(this, others);
                default:
                    return furtherDerivativesY[order - 3];
            }
        }
        return 0;
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
        vp.setAccelerationFunctionX(accelerationFunctionX);
        vp.setAccelerationFunctionY(accelerationFunctionY);
        vp.setFurtherDerivativesX(furtherDerivativesX);
        vp.setFurtherDerivativesY(furtherDerivativesY);

        return vp;
    }

    public double distance(AcceleratedParticle other) {
        return Math.sqrt(Math.pow(other.getX() - getX(),2) + Math.pow(other.getY() - getY(),2));
    }

    ////////////////////////////// Bureaucracy //////////////////////////////

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

    public double[] getFurtherDerivativesX() {
        return furtherDerivativesX;
    }
    public void setFurtherDerivativesX(double[] furtherDerivativesX) {
        System.arraycopy(this.furtherDerivativesX, 0, furtherDerivativesX, 0, MAX_DERIVATIVE_ORDER+1-3);
    }
    public AcceleratedParticle withDerivativesX(double[] derivativesX) {
        setFurtherDerivativesX(derivativesX);
        return this;
    }

    public double[] getFurtherDerivativesY() {
        return furtherDerivativesY;
    }
    public void setFurtherDerivativesY(double[] furtherDerivativesY) {
        System.arraycopy(this.furtherDerivativesY, 0, furtherDerivativesY, 0, MAX_DERIVATIVE_ORDER+1-3);
    }
    public AcceleratedParticle withDerivativesY(double[] derivativesY) {
        setFurtherDerivativesY(derivativesY);
        return this;
    }

    public BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> getAccelerationFunctionX() {
        return accelerationFunctionX;
    }
    public void setAccelerationFunctionX(BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionX) {
        this.accelerationFunctionX = accelerationFunctionX;
    }
    public AcceleratedParticle withAccelerationFunctionX(BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionX) {
        setAccelerationFunctionX(accelerationFunctionX);
        return this;
    }

    public BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> getAccelerationFunctionY() {
        return accelerationFunctionY;
    }
    public void setAccelerationFunctionY(BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionY) {
        this.accelerationFunctionY = accelerationFunctionY;
    }
    public AcceleratedParticle withAccelerationFunctionY(BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionY) {
        setAccelerationFunctionY(accelerationFunctionY);
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
