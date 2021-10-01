package ar.edu.itba.ss.integrations;

import ar.edu.itba.ss.models.AcceleratedParticle;

import java.util.List;

public interface Integration {

    default void setup(AcceleratedParticle previous, AcceleratedParticle current, AcceleratedParticle next, double dt) {
        // Do nothing
    }

    AcceleratedParticle update(List<AcceleratedParticle> allParticles, AcceleratedParticle current, AcceleratedParticle previous, double dt);

}
