package ar.edu.itba.ss;

public enum ParticleType {
    BIG,
    SMALL;

    @Override
    public String toString() {
        return this.name();
    }
}
