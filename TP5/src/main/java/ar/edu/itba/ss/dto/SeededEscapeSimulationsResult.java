package ar.edu.itba.ss.dto;

import ar.edu.itba.ss.models.EscapeRoomSimulationResult;

public class SeededEscapeSimulationsResult extends EscapeRoomSimulationResult {
    private double seed;

    public SeededEscapeSimulationsResult(double seed) {
        this.seed = seed;
    }

    public double getSeed() {
        return seed;
    }
    public void setSeed(double seed) {
        this.seed = seed;
    }
    public SeededEscapeSimulationsResult withSeed(double seed) {
        setSeed(seed);
        return this;
    }
}
