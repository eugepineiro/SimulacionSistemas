package ar.edu.itba.ss.integrations;

import ar.edu.itba.ss.models.AcceleratedParticle;
import ar.edu.itba.ss.models.ParticleHistory;

import java.util.List;

public class VerletOriginal implements Integration {

    @Override
    public void setup(AcceleratedParticle past, AcceleratedParticle present, AcceleratedParticle future, double dt) {
        past.setX(present.getX() - dt * present.getVx() + (Math.pow(dt,2)/(2*present.getMass()))*present.getForceX()); // rx(t)
        past.setY(present.getY() - dt * present.getVy() + (Math.pow(dt,2)/(2*present.getMass()))*present.getForceY()); // ry(t)
    }

    @Override
    public AcceleratedParticle update(List<AcceleratedParticle> allParticles, AcceleratedParticle current, AcceleratedParticle previous, double dt) {
        double mass = current.getMass();
        double currentAccelerationX = current.getPositionDerivativeX(2, allParticles); // calculateAcceleration.apply(current.getX(), current.getVx(), mass);
        double currentAccelerationY = current.getPositionDerivativeY(2, allParticles); //calculateAcceleration.apply(current.getY(), current.getVy(), mass);

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
