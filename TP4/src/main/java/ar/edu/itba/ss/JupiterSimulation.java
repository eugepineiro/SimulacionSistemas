package ar.edu.itba.ss;

import ar.edu.itba.ss.integrations.Integration;
import ar.edu.itba.ss.models.AcceleratedParticle;
import ar.edu.itba.ss.models.ParticleHistory;
import ar.edu.itba.ss.models.ParticleType;

import java.util.List;

public class JupiterSimulation extends MarsSimulation {

    static final private double MAX_DISTANCE_FROM_SUN       = 778.5 * Math.pow(10, 6);      // TODO: Check landing planet orbit
    static final private double MAX_PLANET_ORBIT_TOLERANCE  = 10000;                       // TODO: find a real one

    private AcceleratedParticle jupiter;

    @Override
    public boolean stop(List<ParticleHistory> histories) {
        final AcceleratedParticle currentSpaceship = histories.stream()
            .map(ParticleHistory::getPresent)
            .filter(p -> p.getType().equals(ParticleType.SPACESHIP))
            .findAny()
            .orElse(null);

        if (currentSpaceship == null) {
            return false;
        }
        else {
            final double distance = sun.distance(currentSpaceship);
            return distance > MAX_DISTANCE_FROM_SUN + jupiter.getRadius() + MAX_PLANET_ORBIT_TOLERANCE;
        }
    }

    @Override
    protected List<AcceleratedParticle> getBaseParticles() {
        List<AcceleratedParticle> particles =  super.getBaseParticles();

        /* Jupiter */
        double jupiterMass             = jupiter != null ? jupiter.getMass() : 189818722 * Math.pow(10, 19);            // kg
        double jupiterRadius           = jupiter != null ? jupiter.getRadius() : 69911;                                 // km
        double jupiterX                = jupiter != null ? jupiter.getX() : 6.500280253784848 * Math.pow(10, 8);        // km/s
        double jupiterY                = jupiter != null ? jupiter.getY() : -3.745881860038198 * Math.pow(10, 8);       // km/s
        double jupiterVx               = jupiter != null ? jupiter.getVx() : 6.373758360629619;                         // km/s
        double jupiterVy               = jupiter != null ? jupiter.getVy() : 1.194722075367566 * Math.pow(10, 1);       // km/s

        jupiter = jupiter != null ? jupiter : new AcceleratedParticle()
            .withType(ParticleType.JUPITER)
            .withMass(jupiterMass)
            .withRadius(jupiterRadius)
            .withVx(jupiterVx)
            .withVy(jupiterVy)
            .withX(jupiterX)
            .withY(jupiterY)
        ;

        particles.add(jupiter);
        return particles;
    }

    public JupiterSimulation withIntegration(Integration integration) {
        setIntegration(integration);
        return this;
    }
    public JupiterSimulation withDt(double dt) {
        setDt(dt);
        return this;
    }
    public JupiterSimulation withMaxTime(double maxTime) {
        setMaxTime(maxTime);
        return this;
    }
    public JupiterSimulation withSaveFactor(long saveFactor) {
        setSaveFactor(saveFactor);
        return this;
    }
    public JupiterSimulation withStatusBarActivated(boolean statusBarActivated) {
        setStatusBarActivated(statusBarActivated);
        return this;
    }

    public JupiterSimulation withSpaceshipPresent(boolean spaceshipPresent) {
        setSpaceshipPresent(spaceshipPresent);
        return this;
    }

    public void setJupiter(AcceleratedParticle jupiter) {
        this.jupiter = jupiter;
    }
}
