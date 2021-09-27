package ar.edu.itba.ss.integrations;

import ar.edu.itba.ss.models.AcceleratedParticle;

public class VerletOriginal implements Integration {

    @Override
    public void setup(AcceleratedParticle previous, AcceleratedParticle current, AcceleratedParticle next, double dt) {
        previous.setX(current.getX() - dt * current.getVx() + (Math.pow(dt,2)/(2*current.getMass()))*current.getForceX()); // rx(t)
        previous.setY(current.getY() - dt * current.getVy() + (Math.pow(dt,2)/(2*current.getMass()))*current.getForceY()); // ry(t)
    }

    @Override
    public AcceleratedParticle update(AcceleratedParticle current, AcceleratedParticle previous, double dt) {
        double mass = current.getMass();
        double currentAccelerationX = current.getPositionDerivativeX(2); // calculateAcceleration.apply(current.getX(), current.getVx(), mass);
        double currentAccelerationY = current.getPositionDerivativeY(2); //calculateAcceleration.apply(current.getY(), current.getVy(), mass);

        current.setForceX(mass*currentAccelerationX);
        current.setForceY(mass*currentAccelerationY);

        double nextPositionX    = 2 * current.getX() - previous.getX()+ (Math.pow(dt,2)/mass)*current.getForceX(); // rx(t+dt)
        double currentVelocityX = (nextPositionX - previous.getX())/ (2*dt); // vx(t)

        double nextPositionY    = 2 * current.getY() - previous.getY()+ (Math.pow(dt,2)/mass)*current.getForceY(); // ry(t+dt)
        double currentVelocityY = (nextPositionY - previous.getY())/ (2*dt);; // vy(t)

        current.setVx(currentVelocityX);
        current.setVy(currentVelocityY);

        AcceleratedParticle next = current.clone();
        next.setX(nextPositionX);
        next.setY(nextPositionY);

        return next;
    }
}
