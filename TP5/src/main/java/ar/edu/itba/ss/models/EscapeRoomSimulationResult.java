package ar.edu.itba.ss.models;

import ar.edu.itba.ss.algorithms.EscapeRoomSimulation;

import java.util.List;

public class EscapeRoomSimulationResult {
    private double dt;
    private double minRadius;
    private double maxRadius;
    private double escapeSpeed;
    private double maxDesiredSpeed;
    private double beta;
    private double tau;
    private double roomWidth;
    private double roomHeight;
    private double targetWidth;
    private double outerTargetDistance;
    private double outerTargetWidth;
    private List<Frame<ContractileParticle>> frames;
    private List<Double> escapeTimes;

    public EscapeRoomSimulationResult withSimulationUsed(EscapeRoomSimulation simulation) {
        setDt                 (simulation.getDt());
        setRoomWidth          (simulation.getRoomWidth());
        setRoomHeight         (simulation.getRoomHeight());
        setTargetWidth        (simulation.getTargetWidth());
        setOuterTargetDistance(simulation.getOuterTargetDistance());
        setOuterTargetWidth   (simulation.getOuterTargetWidth());
        setMinRadius          (simulation.getMinRadius());
        setMaxRadius          (simulation.getMaxRadius());
        setBeta               (simulation.getBeta());
        setTau                (simulation.getTau());
        setEscapeSpeed        (simulation.getEscapeSpeed());
        setMaxDesiredSpeed    (simulation.getMaxDesiredSpeed());

        return this;
    }

    //////////////// Autogenerated /////////////////

    public List<Frame<ContractileParticle>> getFrames() {
        return frames;
    }
    public void setFrames(List<Frame<ContractileParticle>> frames) {
        this.frames = frames;
    }
    public EscapeRoomSimulationResult withFrames(List<Frame<ContractileParticle>> frames) {
        setFrames(frames);
        return this;
    }

    public List<Double> getEscapeTimes() {
        return escapeTimes;
    }
    public void setEscapeTimes(List<Double> escapeTimes) {
        this.escapeTimes = escapeTimes;
    }
    public EscapeRoomSimulationResult withEscapeTimes(List<Double> escapeTimes) {
        setEscapeTimes(escapeTimes);
        return this;
    }

    public double getDt() {
        return dt;
    }
    public void setDt(double dt) {
        this.dt = dt;
    }
    public EscapeRoomSimulationResult withDt(double dt) {
        setDt(dt);
        return this;
    }

    public double getRoomWidth() {
        return roomWidth;
    }
    public void setRoomWidth(double roomWidth) {
        this.roomWidth = roomWidth;
    }
    public EscapeRoomSimulationResult withRoomWidth(double roomWidth) {
        setRoomWidth(roomWidth);
        return this;
    }

    public double getRoomHeight() {
        return roomHeight;
    }
    public void setRoomHeight(double roomHeight) {
        this.roomHeight = roomHeight;
    }
    public EscapeRoomSimulationResult withRoomHeight(double roomHeight) {
        setRoomHeight(roomHeight);
        return this;
    }

    public double getTargetWidth() {
        return targetWidth;
    }
    public void setTargetWidth(double targetWidth) {
        this.targetWidth = targetWidth;
    }
    public EscapeRoomSimulationResult withTargetWidth(double targetWidth) {
        setTargetWidth(targetWidth);
        return this;
    }

    public double getOuterTargetDistance() {
        return outerTargetDistance;
    }
    public void setOuterTargetDistance(double outerTargetDistance) {
        this.outerTargetDistance = outerTargetDistance;
    }
    public EscapeRoomSimulationResult withOuterTargetDistance(double outerTargetDistance) {
        setOuterTargetDistance(outerTargetDistance);
        return this;
    }

    public double getOuterTargetWidth() {
        return outerTargetWidth;
    }
    public void setOuterTargetWidth(double outerTargetWidth) {
        this.outerTargetWidth = outerTargetWidth;
    }
    public EscapeRoomSimulationResult withOuterTargetWidth(double outerTargetWidth) {
        setOuterTargetWidth(outerTargetWidth);
        return this;
    }

    public Double getMinRadius() {
        return minRadius;
    }
    public void setMinRadius(Double minRadius) {
        this.minRadius = minRadius;
    }
    public EscapeRoomSimulationResult withMinRadius(Double minRadius) {
        setMinRadius(minRadius);
        return this;
    }

    public Double getMaxRadius() {
        return maxRadius;
    }
    public void setMaxRadius(Double maxRadius) {
        this.maxRadius = maxRadius;
    }
    public EscapeRoomSimulationResult withMaxRadius(Double maxRadius) {
        setMaxRadius(maxRadius);
        return this;
    }

    public Double getBeta() {
        return beta;
    }
    public void setBeta(Double beta) {
        this.beta = beta;
    }
    public EscapeRoomSimulationResult withBeta(Double beta) {
        setBeta(beta);
        return this;
    }

    public Double getTau() {
        return tau;
    }
    public void setTau(Double tau) {
        this.tau = tau;
    }
    public EscapeRoomSimulationResult withTau(Double tau) {
        setTau(tau);
        return this;
    }

    public Double getEscapeSpeed() {
        return escapeSpeed;
    }
    public void setEscapeSpeed(Double escapeSpeed) {
        this.escapeSpeed = escapeSpeed;
    }
    public EscapeRoomSimulationResult withEscapeSpeed(Double escapeSpeed) {
        setEscapeSpeed(escapeSpeed);
        return this;
    }

    public Double getMaxDesiredSpeed() {
        return maxDesiredSpeed;
    }
    public void setMaxDesiredSpeed(Double maxDesiredSpeed) {
        this.maxDesiredSpeed = maxDesiredSpeed;
    }
    public EscapeRoomSimulationResult withMaxDesiredSpeed(Double maxDesiredSpeed) {
        setMaxDesiredSpeed(maxDesiredSpeed);
        return this;
    }

}