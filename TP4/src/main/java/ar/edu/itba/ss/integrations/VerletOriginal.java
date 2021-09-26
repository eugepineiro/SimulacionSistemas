package ar.edu.itba.ss.integrations;

import ar.edu.itba.ss.models.AcceleratedParticle;

public class VerletOriginal implements Integration {

    @Override
    public AcceleratedParticle update(AcceleratedParticle current, AcceleratedParticle previous, double dt) {

        double nextPositionX    = 2 * current.getX() - previous.getX()+ (Math.pow(dt,2)/current.getMass())*current.getForceX(); // rx(t+dt)
        double currentVelocityX = (nextPositionX - previous.getX())/ (2*dt); // vx(t)

        double nextPositionY    = 2 * current.getY() - previous.getY()+ (Math.pow(dt,2)/current.getMass())*current.getForceY(); // ry(t+dt)
        double currentVelocityY = (nextPositionY - previous.getY())/ (2*dt);; // vy(t)

        current.setVx(currentVelocityX);
        current.setVy(currentVelocityY);
        AcceleratedParticle next = current.clone();
        next.setX(nextPositionX);
        next.setY(nextPositionY);

        return next;
    }
}
