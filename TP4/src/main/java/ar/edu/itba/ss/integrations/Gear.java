package ar.edu.itba.ss.integrations;
import ar.edu.itba.ss.models.AcceleratedParticle;
import ar.edu.itba.ss.models.TriFunction;

public class Gear implements Integration {

    @Override
    public AcceleratedParticle update(AcceleratedParticle current, AcceleratedParticle previous, double dt, TriFunction<Double, Double, Double, Double> calculateAcceleration) {
        return current.clone();
    }

}
