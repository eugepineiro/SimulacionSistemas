package ar.edu.itba.ss.algorithms;

import ar.edu.itba.ss.CellIndexMethod;
import ar.edu.itba.ss.Grid;
import ar.edu.itba.ss.Particle;
import ar.edu.itba.ss.Utils;
import ar.edu.itba.ss.models.*;

import java.util.*;
import java.util.stream.Collectors;

public abstract class EscapeRoomSimulation {

    // Status bar
    protected final int                         STATUS_BAR_SIZE         = 31;

    protected       double                      dt;
    protected       double                      maxTime;
    protected       long                        saveFactor;
    protected       boolean                     statusBarActivated;

    protected       Random                      random;
    protected       double                      minRadius;
    protected       double                      maxRadius;
    protected       double                      escapeSpeed;
    protected       double                      maxDesiredSpeed;
    protected       double                      beta;
    protected       double                      tau;
    protected       double                      lowerMargin;
    protected       double                      roomWidth;
    protected       double                      roomHeight;
    protected       double                      targetWidth;
    protected       double                      outerTargetDistance;
    protected       double                      outerTargetWidth;
    protected       List<ContractileParticle>   particles;

    public boolean stop(List<ContractileParticle> particles) {
        return particles.stream().allMatch(particle -> (particle.getY() + particle.getRadius()) < (lowerMargin + outerTargetDistance));
    }

    public void printStatusBar(double time) {
        if (statusBarActivated) Utils.printLoadingBar(time/maxTime, STATUS_BAR_SIZE);
    }

    public abstract ContractileParticle getNextParticle(ContractileParticle particle, List<ContractileParticle> otherParticles);

    public EscapeRoomSimulationResult simulate() {

        List<ContractileParticle> currentParticles = particles;
        List<ContractileParticle> nextParticles    = new LinkedList<>();
        List<ContractileParticle> aux;

        final List<Frame<ContractileParticle>> frames = new LinkedList<>();
        final Map<Long, Double> escapeTimes           = new HashMap<>();

        long count;
        double time;
        boolean stopped = false;

        final int realRoomHeight = (int) Math.ceil(lowerMargin + outerTargetDistance + roomHeight);
        final int realRoomWidth = (int) Math.ceil(roomWidth);
        final int minDimension = Math.min(realRoomWidth, realRoomHeight);

        /** optimizamos usando "L/M > rc + 2 * max_particle_radius (L/50.0)" --> "cellSize (minDimension/M) > 0 + 2 * maxRadius" --> "M < minDimension / (2*maxRadius)" */
        final int gridDimension = (int) Math.ceil(minDimension / (2*maxRadius)) - 1; // TODO(matias): si a es entero, ceil = a, a-1 < a, valida. si a no es entero, ceil-1 = floor(a) < a, valida.

        for (time = 0, count = 0; time <= maxTime && !(stopped = stop(currentParticles)); time += dt, count++) {
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

            List<ContractileParticle>[][] matrix = Grid.build(currentParticles, gridDimension, realRoomWidth, realRoomHeight);
            Map<ContractileParticle, List<ContractileParticle>> nearParticles = CellIndexMethod.search(matrix, 0, gridDimension, realRoomWidth, realRoomHeight, false);

            // Update particles
            for (int i = 0; i < currentParticles.size(); i++) {
                ContractileParticle particle = currentParticles.get(i);
                ContractileParticle nextParticle = getNextParticle(particle, nearParticles.getOrDefault(particle, Collections.emptyList()));

                nextParticles.add(nextParticle);
                if ((particle.getY() + particle.getRadius()) >= (lowerMargin + outerTargetDistance) && (nextParticle.getY() + nextParticle.getRadius()) < (lowerMargin + outerTargetDistance) && !escapeTimes.containsKey(particle.getId())) {
                    escapeTimes.put(particle.getId(), time);
                }
            }

            aux = currentParticles;
            currentParticles = nextParticles;
            nextParticles = aux;
            nextParticles.clear();
        }

        if (stopped) {
            frames.add(new Frame<ContractileParticle>()
                .withParticles(currentParticles.stream()
                    .map(ContractileParticle::clone)
                    .collect(Collectors.toList())
                )
                .withTime(time)
            );
        }

        // Status bar
        printStatusBar(maxTime+0.01);

        return new EscapeRoomSimulationResult()
            .withSimulationUsed(this)
            .withFrames(frames)
            .withEscapeTimes(escapeTimes.values().stream().sorted().collect(Collectors.toList()))
            ;
    }

    protected List<WallContact> getWallContacts(ContractileParticle particle) {

        final List<WallContact> possibleWallContacts = Arrays.asList(
            new WallContact(0            , particle.getY()), // left wall
            new WallContact(roomWidth       , particle.getY()), // right wall
            new WallContact(particle.getX() , lowerMargin + outerTargetDistance + roomHeight)  // top wall
        );

        final List<WallContact> wallContacts = possibleWallContacts.stream()
            .filter(particle::overlapsWith)
            .collect(Collectors.toList());

        double targetLeftCornerX  = (roomWidth/2) - (targetWidth/2);
        double targetRightCornerX = (roomWidth/2) + (targetWidth/2);

        if (particle.getX() < targetLeftCornerX || particle.getX() > targetRightCornerX) { // outside target
            final WallContact bottomWallContact = new WallContact(particle.getX(), lowerMargin + outerTargetDistance);
            if (particle.overlapsWith(bottomWallContact)) {
                wallContacts.add(bottomWallContact);
            }
        }
        else if (particle.getX() < (roomWidth/2)) { // inside target, left
            final WallContact leftCorner = new WallContact(targetLeftCornerX, lowerMargin + outerTargetDistance);
            if(particle.overlapsWith(leftCorner))
                wallContacts.add(leftCorner);
        }
        else { // particle.getX() >= (roomWidth/2)  // inside target, right
            final WallContact rightCorner = new WallContact(targetRightCornerX, lowerMargin + outerTargetDistance);
            if(particle.overlapsWith(rightCorner))
                wallContacts.add(rightCorner);
        }

        return  wallContacts;
    }

    protected Point2D getTarget(ContractileParticle particle) {
        double particleX, particleY;
        particleX = particle.getX();
        particleY = particle.getY();

        double targetX;
        double targetY;

        double left;
        double right;

        if (particleY > (lowerMargin + outerTargetDistance)) {
            double targetLeftUpperCornerX  = (roomWidth/2) - (targetWidth/2);

            left = targetLeftUpperCornerX + 0.2 * targetWidth;
            right = targetLeftUpperCornerX + 0.8 * targetWidth;

            targetY = lowerMargin + outerTargetDistance;
        }
        else {
            double targetLeftLowerCornerX  = (roomWidth/2) - (outerTargetWidth/2);
            double targetRightLowerCornerX = (roomWidth/2) + (outerTargetWidth/2);

            left = targetLeftLowerCornerX;
            right = targetRightLowerCornerX;

            targetY = lowerMargin;
        }

        if (particleX < left || particleX > right) {
            targetX = random.nextDouble() * (right - left) + left;
        }
        else {
            targetX = particleX;
        }

        return new Point2D(targetX, targetY);
    }

    //////////////// Autogenerated /////////////////


    public double getLowerMargin() {
        return lowerMargin;
    }
    public void setLowerMargin(double lowerMargin) {
        this.lowerMargin = lowerMargin;
    }
    public EscapeRoomSimulation withLowerMargin(double lowerMargin) {
        setLowerMargin(lowerMargin);
        return this;
    }

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

    public Double getMinRadius() {
        return minRadius;
    }
    public void setMinRadius(Double minRadius) {
        this.minRadius = minRadius;
    }
    public EscapeRoomSimulation withMinRadius(Double minRadius) {
        setMinRadius(minRadius);
        return this;
    }

    public Double getMaxRadius() {
        return maxRadius;
    }
    public void setMaxRadius(Double maxRadius) {
        this.maxRadius = maxRadius;
    }
    public EscapeRoomSimulation withMaxRadius(Double maxRadius) {
        setMaxRadius(maxRadius);
        return this;
    }

    public Double getBeta() {
        return beta;
    }
    public void setBeta(Double beta) {
        this.beta = beta;
    }
    public EscapeRoomSimulation withBeta(Double beta) {
        setBeta(beta);
        return this;
    }

    public Double getTau() {
        return tau;
    }
    public void setTau(Double tau) {
        this.tau = tau;
    }
    public EscapeRoomSimulation withTau(Double tau) {
        setTau(tau);
        return this;
    }

    public Double getEscapeSpeed() {
        return escapeSpeed;
    }
    public void setEscapeSpeed(Double escapeSpeed) {
        this.escapeSpeed = escapeSpeed;
    }
    public EscapeRoomSimulation withEscapeSpeed(Double escapeSpeed) {
        setEscapeSpeed(escapeSpeed);
        return this;
    }

    public Double getMaxDesiredSpeed() {
        return maxDesiredSpeed;
    }
    public void setMaxDesiredSpeed(Double maxDesiredSpeed) {
        this.maxDesiredSpeed = maxDesiredSpeed;
    }
    public EscapeRoomSimulation withMaxDesiredSpeed(Double maxDesiredSpeed) {
        setMaxDesiredSpeed(maxDesiredSpeed);
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
