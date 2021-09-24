package ar.edu.itba.ss.integrations;

import ar.edu.itba.ss.models.VelocityParticle;

public interface Integration {
    
    VelocityParticle update(VelocityParticle p, double dt);

}
