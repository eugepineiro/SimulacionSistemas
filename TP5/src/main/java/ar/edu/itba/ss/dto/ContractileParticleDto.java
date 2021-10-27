package ar.edu.itba.ss.dto;

import ar.edu.itba.ss.Particle;
import ar.edu.itba.ss.models.ContractileParticle;

public class ContractileParticleDto extends Particle {

    private double                                                      vx;
    private double                                                      vy;

    public ContractileParticleDto(ContractileParticle cp) {
        this.setId      (cp.getId());
        this.setX       (cp.getX());
        this.setY       (cp.getY());
        this.setRadius  (cp.getRadius());
        this.setVx      (cp.getVx());
        this.setVy      (cp.getVy());
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
}
