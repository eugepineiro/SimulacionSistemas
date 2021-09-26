package ar.edu.itba.ss.integrations;

import ar.edu.itba.ss.models.AcceleratedParticle;

public interface Integration {

    AcceleratedParticle update(AcceleratedParticle current, AcceleratedParticle previous, double dt);

}
