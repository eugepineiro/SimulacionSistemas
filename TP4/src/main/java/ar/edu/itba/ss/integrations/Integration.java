package ar.edu.itba.ss.integrations;

import ar.edu.itba.ss.models.AcceleratedParticle;
import ar.edu.itba.ss.models.ParticleHistory;

import java.util.List;

public interface Integration {

    default void setup(AcceleratedParticle past, AcceleratedParticle present, AcceleratedParticle future, double dt) {
        // Do nothing
    }

    AcceleratedParticle update(List<AcceleratedParticle> allParticles, AcceleratedParticle current, AcceleratedParticle previous, double dt);

}
