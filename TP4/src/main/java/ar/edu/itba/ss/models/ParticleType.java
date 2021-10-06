package ar.edu.itba.ss.models;

public enum ParticleType {
    OSCILLABLE, SUN, EARTH, MARS, JUPITER, SPACESHIP;

    @Override
    public String toString() {
        return this.name();
    }
}
