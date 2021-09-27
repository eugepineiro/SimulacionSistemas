package ar.edu.itba.ss.models;

public enum ParticleType {
    OSCILLABLE, SUN, EARTH, MARS, SPACESHIP;

    @Override
    public String toString() {
        return this.name();
    }
}
