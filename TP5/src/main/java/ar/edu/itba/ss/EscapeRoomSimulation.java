package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.CPM;
import ar.edu.itba.ss.config.Config;
import ar.edu.itba.ss.models.ContractileParticle;
import ar.edu.itba.ss.models.Frame;

import java.util.*;
import java.util.stream.Collectors;

public class EscapeRoomSimulation {

    public static class Results {
        private List<Frame<ContractileParticle>> frames;
        private List<Double> escapeTimes;

        public List<Frame<ContractileParticle>> getFrames() {
            return frames;
        }
        public void setFrames(List<Frame<ContractileParticle>> frames) {
            this.frames = frames;
        }
        public Results withFrames(List<Frame<ContractileParticle>> frames) {
            setFrames(frames);
            return this;
        }

        public List<Double> getEscapeTimes() {
            return escapeTimes;
        }
        public void setEscapeTimes(List<Double> escapeTimes) {
            this.escapeTimes = escapeTimes;
        }
        public Results withEscapeTimes(List<Double> escapeTimes) {
            setEscapeTimes(escapeTimes);
            return this;
        }
    }

    // Status bar
    protected final int                         STATUS_BAR_SIZE         = 31;

    protected       double                      dt;
    protected       double                      maxTime;
    protected       long                        saveFactor;
    protected       boolean                     statusBarActivated;

    protected       Random                      random;
    protected       double                      roomWidth;
    protected       double                      roomHeight;
    protected       double                      targetWidth;
    protected       double                      outerTargetDistance;
    protected       double                      outerTargetWidth;
    protected       List<ContractileParticle>   particles;

    public boolean stop(List<ContractileParticle> particles) {
        return particles.stream().allMatch(particle -> (particle.getY() + particle.getRadius()) < 0);
    }

    public void printStatusBar(double time) {
        if (statusBarActivated) Utils.printLoadingBar(time/maxTime, STATUS_BAR_SIZE);
    }

    public Results simulate() {
        List<ContractileParticle> currentParticles = particles;
        List<ContractileParticle> nextParticles    = new LinkedList<>();
        List<ContractileParticle> aux;

        final List<Frame<ContractileParticle>> frames = new LinkedList<>();
        final Map<Long, Double> escapeTimes           = new HashMap<>();

        long count;
        double time;

        for (time = 0, count = 0; time <= maxTime && !stop(currentParticles); time += dt, count++) {
            // Status bar
            printStatusBar(time);

            // Save particles
            if (count % saveFactor == 0) {
                frames.add(new Frame<ContractileParticle>()
                    .withParticles(currentParticles.stream()
                        .map(ContractileParticle::clone)
                        .collect(Collectors.toList())
                    )
                    .withTime(time)
                );
            }

            // Update particles
            for (int i = 0; i < currentParticles.size(); i++) {
                ContractileParticle particle = currentParticles.get(i);
                ContractileParticle nextParticle = CPM.getNextParticle(particle, currentParticles.stream().filter(other -> other != particle).collect(Collectors.toList()), roomHeight, roomWidth, targetWidth, outerTargetDistance, outerTargetWidth, dt, random);

                nextParticles.add(nextParticle);
                if ((particle.getY() + particle.getRadius()) >= 0 && (nextParticle.getY() + nextParticle.getRadius()) < 0 && !escapeTimes.containsKey(particle.getId())) {
                    escapeTimes.put(particle.getId(), time);
                }
            }

            aux = currentParticles;
            currentParticles = nextParticles;
            nextParticles = aux;
            nextParticles.clear();
        }

        // Status bar
        printStatusBar(maxTime+0.01);
        System.out.println();

        return new Results()
            .withFrames(frames)
            .withEscapeTimes(new LinkedList<>(escapeTimes.values()))
            ;
    }

    //////////////// Autogenerated /////////////////


    public Random getRandom() {
        return random;
    }
    public void setRandom(Random random) {
        this.random = random;
    }
    public EscapeRoomSimulation withRandom(Random random) {
        setRandom(random);
        return this;
    }

    public double getDt() {
        return dt;
    }
    public void setDt(double dt) {
        this.dt = dt;
    }
    public EscapeRoomSimulation withDt(double dt) {
        setDt(dt);
        return this;
    }

    public double getMaxTime() {
        return maxTime;
    }
    public void setMaxTime(double maxTime) {
        this.maxTime = maxTime;
    }
    public EscapeRoomSimulation withMaxTime(double maxTime) {
        setMaxTime(maxTime);
        return this;
    }

    public long getSaveFactor() {
        return saveFactor;
    }
    public void setSaveFactor(long saveFactor) {
        this.saveFactor = saveFactor;
    }
    public EscapeRoomSimulation withSaveFactor(long saveFactor) {
        setSaveFactor(saveFactor);
        return this;
    }

    public boolean isStatusBarActivated() {
        return statusBarActivated;
    }
    public void setStatusBarActivated(boolean statusBarActivated) {
        this.statusBarActivated = statusBarActivated;
    }
    public EscapeRoomSimulation withStatusBarActivated(boolean statusBarActivated) {
        setStatusBarActivated(statusBarActivated);
        return this;
    }

    public double getRoomWidth() {
        return roomWidth;
    }
    public void setRoomWidth(double roomWidth) {
        this.roomWidth = roomWidth;
    }
    public EscapeRoomSimulation withRoomWidth(double roomWidth) {
        setRoomWidth(roomWidth);
        return this;
    }

    public double getRoomHeight() {
        return roomHeight;
    }
    public void setRoomHeight(double roomHeight) {
        this.roomHeight = roomHeight;
    }
    public EscapeRoomSimulation withRoomHeight(double roomHeight) {
        setRoomHeight(roomHeight);
        return this;
    }

    public double getTargetWidth() {
        return targetWidth;
    }
    public void setTargetWidth(double targetWidth) {
        this.targetWidth = targetWidth;
    }
    public EscapeRoomSimulation withTargetWidth(double targetWidth) {
        setTargetWidth(targetWidth);
        return this;
    }

    public double getOuterTargetDistance() {
        return outerTargetDistance;
    }
    public void setOuterTargetDistance(double outerTargetDistance) {
        this.outerTargetDistance = outerTargetDistance;
    }
    public EscapeRoomSimulation withOuterTargetDistance(double outerTargetDistance) {
        setOuterTargetDistance(outerTargetDistance);
        return this;
    }

    public double getOuterTargetWidth() {
        return outerTargetWidth;
    }
    public void setOuterTargetWidth(double outerTargetWidth) {
        this.outerTargetWidth = outerTargetWidth;
    }
    public EscapeRoomSimulation withOuterTargetWidth(double outerTargetWidth) {
        setOuterTargetWidth(outerTargetWidth);
        return this;
    }

    public List<ContractileParticle> getParticles() {
        return particles;
    }
    public void setParticles(List<ContractileParticle> particles) {
        this.particles = particles;
    }
    public EscapeRoomSimulation withParticles(List<ContractileParticle> particles) {
        setParticles(particles);
        return this;
    }
}
