package ar.edu.itba.ss.integrations;
import ar.edu.itba.ss.models.AcceleratedParticle;

import java.util.List;

public class Beeman implements Integration {

    @Override
    public void setup(AcceleratedParticle previous, AcceleratedParticle current, AcceleratedParticle next, double dt) {
        previous.setX(current.getX() - dt * current.getVx() + (Math.pow(dt,2)/(2*current.getMass()))*current.getForceX()); // rx(t)
        previous.setY(current.getY() - dt * current.getVy() + (Math.pow(dt,2)/(2*current.getMass()))*current.getForceY()); // ry(t)
    }

    @Override
    public AcceleratedParticle update(List<AcceleratedParticle> allParticles, AcceleratedParticle current, AcceleratedParticle previous, double dt) {

        // Calculate Acceleration
        double mass = current.getMass();
        double previousAccelerationX    = previous.getPositionDerivativeX(2, allParticles);
        double previousAccelerationY    = previous.getPositionDerivativeY(2, allParticles); //calculateAcceleration.apply(previous.getY(), previous.getVy(), mass);
        double currentAccelerationX     = current.getPositionDerivativeX(2, allParticles);
        double currentAccelerationY     = current.getPositionDerivativeY(2, allParticles);
        // Calculate Position
        double nextPositionX            = current.getX() + current.getVx() * dt + (2/3.0) * currentAccelerationX * Math.pow(dt,2) - (1/6.0)* previousAccelerationX * Math.pow(dt,2);  // rx(t+dt)
        double nextPositionY            = current.getY() + current.getVy() * dt + (2/3.0) * currentAccelerationY * Math.pow(dt,2) - (1/6.0)* previousAccelerationY * Math.pow(dt,2);  // ry(t+dt)

        // Calculate Velocity with Predictor-Corrector Strategy
        double nextPredictedVelocityX   = current.getVx() + (3/2.0) * currentAccelerationX * dt - (1/2.0) * previousAccelerationX * dt  ; // vx(t+dt)
        double nextPredictedVelocityY   = current.getVy() + (3/2.0) * currentAccelerationY * dt - (1/2.0) * previousAccelerationY * dt  ; // vy(t+dt)

        AcceleratedParticle nextPredicted = current.clone();
        nextPredicted.setX(nextPositionX);
        nextPredicted.setY(nextPositionY);
        nextPredicted.setVx(nextPredictedVelocityX);
        nextPredicted.setVy(nextPredictedVelocityY);

        double nextAccelerationX        = nextPredicted.getPositionDerivativeX(2, allParticles);//calculateAcceleration.apply(nextPositionX, nextPredictedVelocityX, mass);
        double nextAccelerationY        = nextPredicted.getPositionDerivativeY(2, allParticles);

        double nextCorrectedVelocityX   = current.getVx() + (1/3.0) * nextAccelerationX * dt + (5/6.0) * currentAccelerationX * dt - (1/6.0) * previousAccelerationX * dt;
        double nextCorrectedVelocityY   = current.getVy() + (1/3.0) * nextAccelerationY * dt + (5/6.0) * currentAccelerationY * dt - (1/6.0) * previousAccelerationY * dt;

        // Update Particle
        AcceleratedParticle next = current.clone();

        next.setX(nextPositionX);
        next.setY(nextPositionY);

        next.setVx(nextCorrectedVelocityX);
        next.setVy(nextCorrectedVelocityY);

        next.setForceX(mass*nextAccelerationX);
        next.setForceY(mass*nextAccelerationY);

        return next;
    }

}
