package ar.edu.itba.ss;

import ar.edu.itba.ss.models.AcceleratedParticle;
import ar.edu.itba.ss.models.ParticleType;
import ar.edu.itba.ss.models.TriFunction;

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

        m                               = particle.getMass();

        // x

        velX                            = particle.getVx();
        accX                            = particle.getPositionDerivativeX(2, Collections.singletonList(particle));
        double[] furtherDerivativesX    = particle.getFurtherDerivativesX();
        furtherDerivativesX[0]          = (- k * velX - gamma * accX)/m;
        furtherDerivativesX[1]          = (- k * accX - gamma * furtherDerivativesX[0])/m;
        furtherDerivativesX[2]          = (- k * furtherDerivativesX[0] - gamma * furtherDerivativesX[1])/m;
        particle.setFurtherDerivativesX(furtherDerivativesX);

        // y

        velY                            = particle.getVy();
        accY                             = particle.getPositionDerivativeY(2, Collections.singletonList(particle));
        double[] furtherDerivativesY    = particle.getFurtherDerivativesY();
        furtherDerivativesY[0]          = (- k * velY - gamma * accY)/m;
        furtherDerivativesY[1]          = (- k * accY - gamma * furtherDerivativesY[0])/m;
        furtherDerivativesY[2]          = (- k * furtherDerivativesY[0] - gamma * furtherDerivativesY[1])/m;
        particle.setFurtherDerivativesY(furtherDerivativesY);

        particle.setForceX(mass * accX);
        particle.setForceY(mass * accY);

        return Collections.singletonList(particle);
    }

}
