package ar.edu.itba.ss.models;

import ar.edu.itba.ss.Particle;

import java.util.HashMap;
import java.util.Map;

public class VelocityParticle extends Particle {

    private ParticleType type;
    private double vx;
    private double vy;
    private double mass;

    public VelocityParticle() {
    }

    public VelocityParticle(ParticleType type, Particle particle, double speed, double angle, double mass) {
        this(type, particle.getId(), particle.getX(), particle.getY(), particle.getRadius(), speed, angle, mass);
    }

    public VelocityParticle(ParticleType type, long id, double x, double y, double radius, double speed, double angle, double mass) {
        super(id, x, y, radius);
        this.type = type;
        this.vx = speed * Math.cos(angle);
        this.vy = speed * Math.sin(angle);
        this.mass = mass;
    }

    public double getSpeed() {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
    }

    public double getAngle() {
        return (vx == 0)? ((vy >= 0)? Math.PI/2.0 : Math.PI*(3/2.0)): Math.tan(vy/vx);
    }

    public VelocityParticle clone() {
        VelocityParticle vp = new VelocityParticle();
        vp.setType(type);
        vp.setId(id);
        vp.setX(x);
        vp.setY(y);
        vp.setRadius(radius);
        vp.setVx(vx);
        vp.setVy(vy);
        vp.setMass(mass);

        return vp;
    }

    ////////////////////////////// Bureaucracy //////////////////////////////

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public ParticleType getType() {
        return type;
    }

    public void setType(ParticleType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ar.edu.itba.ss.ar.edu.itba.ss.models.VelocityParticle{" +
            "type=" + type +
            ", id=" + id +
            ", x=" + x +
            ", y=" + y +
            ", radius=" + radius +
            ", vx=" + vx +
            ", vy=" + vy +
            ", mass=" + mass +
            '}';
    }
}
