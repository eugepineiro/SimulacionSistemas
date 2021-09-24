package ar.edu.itba.ss.models;

public enum ParticleType {
    BIG,
    SMALL;

    @Override
    public String toString() {
        return this.name();
    }
}
