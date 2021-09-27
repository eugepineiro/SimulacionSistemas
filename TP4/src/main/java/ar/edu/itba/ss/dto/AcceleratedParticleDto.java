package ar.edu.itba.ss.dto;

import ar.edu.itba.ss.Particle;
import ar.edu.itba.ss.models.AcceleratedParticle;
import ar.edu.itba.ss.models.ParticleType;

public class AcceleratedParticleDto extends Particle {

    private ParticleType type;
    private double                                                      vx;
    private double                                                      vy;
    private double                                                      mass;
    private double                                                      forceX;
    private double                                                      forceY;

    public AcceleratedParticleDto(AcceleratedParticle vp) {
        this.setType(vp.getType());
        this.setId(vp.getId());
        this.setX(vp.getX());
        this.setY(vp.getY());
        this.setRadius(vp.getRadius());
        this.setVx(vp.getVx());
        this.setVy(vp.getVy());
        this.setMass(vp.getMass());
        this.setForceX(vp.getForceX());
        this.setForceY(vp.getForceY());
    }

    public ParticleType getType() {
        return type;
    }
    public void setType(ParticleType type) {
        this.type = type;
    }

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

    public double getForceX() {
        return forceX;
    }
    public void setForceX(double forceX) {
        this.forceX = forceX;
    }

    public double getForceY() {
        return forceY;
    }
    public void setForceY(double forceY) {
        this.forceY = forceY;
    }
}
