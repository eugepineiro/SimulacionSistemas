package ar.edu.itba.ss.integrations;
import ar.edu.itba.ss.models.AcceleratedParticle;
import ar.edu.itba.ss.models.TriFunction;

import java.util.function.BiFunction;

public class Beeman implements Integration {

    @Override
    public AcceleratedParticle update(AcceleratedParticle current, AcceleratedParticle previous, double dt, TriFunction<Double, Double, Double, Double> calculateAcceleration) {

        // Calculate Acceleration
        double mass = current.getMass();
        double previousAccelerationX = calculateAcceleration.apply(previous.getX(), previous.getVx(), mass);
        double previousAccelerationY = calculateAcceleration.apply(previous.getY(), previous.getVy(), mass);
        double currentAccelerationX =  calculateAcceleration.apply(current.getX(), current.getVx(), mass);
        double currentAccelerationY = calculateAcceleration.apply(current.getY(), current.getVy(), mass);

        // Calculate Position
        double nextPositionX = current.getX() + current.getVx() * dt + (2/3.0) * currentAccelerationX * Math.pow(dt,2) - (1/6.0)* previousAccelerationX * Math.pow(dt,2);  // rx(t+dt)
        double nextPositionY = current.getY() + current.getVy() * dt + (2/3.0) * currentAccelerationY * Math.pow(dt,2) - (1/6.0)* previousAccelerationY * Math.pow(dt,2);  // ry(t+dt)

        // Calculate Velocity with Predictor-Corrector Strategy
        double nextPredictedVelocityX = current.getVx() + (3/2.0) * currentAccelerationX * dt - (1/2.0) * previousAccelerationX * dt  ; // vx(t+dt)
        double nextPredictedVelocityY = current.getVy() + (3/2.0) * currentAccelerationY * dt - (1/2.0) * previousAccelerationY * dt  ; // vy(t+dt)

        double nextAccelerationX = calculateAcceleration.apply(nextPositionX, nextPredictedVelocityX, mass);
        double nextAccelerationY = calculateAcceleration.apply(nextPositionY, nextPredictedVelocityY, mass);

        double nextCorrectedVelocityX = current.getVx() + (1/3.0) * nextAccelerationX * dt + (5/6.0) * currentAccelerationX * dt - (1/6.0) * previousAccelerationX * dt;
        double nextCorrectedVelocityY = current.getVy() + (1/3.0) * nextAccelerationY * dt + (5/6.0) * currentAccelerationY * dt - (1/6.0) * previousAccelerationY * dt;

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
