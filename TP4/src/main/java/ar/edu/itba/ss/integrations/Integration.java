package ar.edu.itba.ss.integrations;

import ar.edu.itba.ss.models.AcceleratedParticle;
import ar.edu.itba.ss.models.TriFunction;

import java.util.function.BiFunction;

public interface Integration {

    default void setup(AcceleratedParticle previous, AcceleratedParticle current, AcceleratedParticle next, double dt) {
        // Do nothing
    }

    AcceleratedParticle update(AcceleratedParticle current, AcceleratedParticle previous, double dt, TriFunction<Double, Double, Double, Double> calculateAcceleration);

}
