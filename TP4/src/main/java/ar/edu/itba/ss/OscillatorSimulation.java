package ar.edu.itba.ss;

import ar.edu.itba.ss.models.AcceleratedParticle;
import ar.edu.itba.ss.models.ParticleType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class OscillatorSimulation extends AbstractSimulation {

    @Override
    public List<AcceleratedParticle> getParticles() {

        // Initial Conditions
        double k        = Math.pow(10,4);       // N/m
        double r        = 1;                    // m
        double gamma    = 100;                  // kg/s
        double mass     = 70;                   // kg
        double vy       = -gamma/(2*mass);      // m/s

        BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionX = (current, all) -> {
            final double pos = current.getX();
            final double vel = current.getVx();
            final double m = current.getMass();

            return (- k * pos - gamma * vel)/m;
        };

        BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionY = (current, all) -> {
            final double pos = current.getY();
            final double vel = current.getVy();
            final double m = current.getMass();

            return (- k * pos - gamma * vel)/m;
        };

        final AcceleratedParticle particle = new AcceleratedParticle()
            .withType(ParticleType.OSCILLABLE)
            .withY(r)
            .withVy(vy)
            .withMass(mass)
            .withAccelerationFunctionX(accelerationFunctionX)
            .withAccelerationFunctionY(accelerationFunctionY)
            ;

        double velX, accX, velY, accY, m;

        m                                       = particle.getMass();

        // x
        velX                                    = particle.getVx();
        accX                                    = particle.getPositionDerivativeX(2, Collections.singletonList(particle));
        ArrayList<Double> furtherDerivativesX   = particle.getFurtherDerivativesX();
        furtherDerivativesX.set(0, (- k * velX - gamma * accX)/m);
        furtherDerivativesX.set(1, (- k * accX - gamma * furtherDerivativesX.get(0))/m);
        furtherDerivativesX.set(2, (- k * furtherDerivativesX.get(0) - gamma * furtherDerivativesX.get(1))/m);
        particle.setFurtherDerivativesX(furtherDerivativesX);

        // y
        velY                                    = particle.getVy();
        accY                                    = particle.getPositionDerivativeY(2, Collections.singletonList(particle));
        ArrayList<Double> furtherDerivativesY   = particle.getFurtherDerivativesY();
        furtherDerivativesY.set(0, (- k * velY - gamma * accY)/m);
        furtherDerivativesY.set(1, (- k * accY - gamma * furtherDerivativesY.get(0))/m);
        furtherDerivativesY.set(2, (- k * furtherDerivativesY.get(0) - gamma * furtherDerivativesY.get(1))/m);
        particle.setFurtherDerivativesY(furtherDerivativesY);

        particle.setForceX(m * accX);
        particle.setForceY(m * accY);

        return Collections.singletonList(particle);
    }

}
