package ar.edu.itba.ss;

import ar.edu.itba.ss.integrations.Integration;
import ar.edu.itba.ss.models.*;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class OscillatorSimulation extends AbstractSimulation {

    @Override
    public List<AcceleratedParticle> getParticles() {

        // Initial Conditions
        double k        = Math.pow(10,4);       // N/m
        double r        = 1;                    // m
        double gamma    = 100;                  // kg/s
        double mass     = 70;                   // kg
        double vy       = -gamma/(2*mass);      // m/s

        TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> calculateDerivativeX = new TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double>() {
            @Override
            public Double apply(Integer order, AcceleratedParticle current, List<AcceleratedParticle> all) {
                double pos = current.getX();
                double vel = current.getVx();
                double m = current.getMass();

                switch (order) {
                    case 0:
                        return pos;
                    case 1:
                        return vel;
                    case 2:
                        return (- k * pos - gamma * vel)/m;
                    case 3:
                        return (- k * vel - gamma * this.apply(2, current, all))/m;
                    case 4:
                        return (- k * this.apply(2, current, all) - gamma * this.apply(3, current, all))/m;
                    case 5:
                        return (- k * this.apply(3, current, all) - gamma * this.apply(4, current, all))/m;
                }
                return (double) 0;
            }
        };

        TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> calculateDerivativeY = new TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double>() {
            @Override
            public Double apply(Integer order, AcceleratedParticle current, List<AcceleratedParticle> all) {
                double pos = current.getY();
                double vel = current.getVy();
                double m = current.getMass();

                switch (order) {
                    case 0:
                        return pos;
                    case 1:
                        return vel;
                    case 2:
                        return (- k * pos - gamma * vel)/m;
                    case 3:
                        return (- k * vel - gamma * this.apply(2, current, all))/m;
                    case 4:
                        return (- k * this.apply(2, current, all) - gamma * this.apply(3, current, all))/m;
                    case 5:
                        return (- k * this.apply(3, current, all) - gamma * this.apply(4, current, all))/m;
                }
                return (double) 0;
            }
        };

        final AcceleratedParticle particle = new AcceleratedParticle()
            .withType(ParticleType.OSCILLABLE)
            .withY(r)
            .withVy(vy)
            .withMass(mass)
            .withDerivativeFunctionX(calculateDerivativeX)
            .withDerivativeFunctionY(calculateDerivativeY)
            ;

        particle.setForceY(mass*calculateDerivativeY.apply(2, particle, Collections.singletonList(particle)));

        return Collections.singletonList(particle);
    }

    @Override
    public boolean stop(List<ParticleHistory> histories) {
        return false;
    }

}
