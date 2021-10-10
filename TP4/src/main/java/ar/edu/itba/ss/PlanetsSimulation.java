package ar.edu.itba.ss;

import ar.edu.itba.ss.integrations.Integration;
import ar.edu.itba.ss.models.AcceleratedParticle;
import ar.edu.itba.ss.models.ParticleType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlanetsSimulation extends AbstractSimulation {

    static final protected double GRAVITY_CONSTANT          = 6.693 * Math.pow(10, -20);    // km³/(kg*s²)

    private boolean spaceshipPresent;
    private double spaceshipInitialSpeed = 8;

    protected AcceleratedParticle earth, sun, spaceship;

    @Override
    public List<AcceleratedParticle> getParticles() {
        List<AcceleratedParticle> particles = getBaseParticles();
        addDerivativesToParticles(particles);

        return particles;
    }

    protected List<AcceleratedParticle> getBaseParticles() {
        /* Earth */
        double earthRadius          = earth != null ? earth.getRadius() : 6378.137;                             // km
        double earthMass            = earth != null ? earth.getMass() : 5.97219 * Math.pow(10, 24);             // kg
        double earthX               = earth != null ? earth.getX() : 1.500619962348151 * Math.pow(10,8);        // km
        double earthY               = earth != null ? earth.getY() : 2.288499248197072 * Math.pow(10,6);        // km
        double earthVx              = earth != null ? earth.getVx() : -9.322979134387409 * Math.pow(10,-1);     // km/s
        double earthVy              = earth != null ? earth.getVy() : 2.966365033636722 * Math.pow(10,1);       // km/s
        double earthVelocity        = Math.sqrt(Math.pow(earthVx, 2) + Math.pow(earthVy, 2));                   // TODO elegir un
        /* Sun */
        double sunX                 = sun != null ? sun.getX() : 0;
        double sunY                 = sun != null ? sun.getY() : 0;
        double sunVx                = sun != null ? sun.getVx() : 0;
        double sunVy                = sun != null ? sun.getVy() : 0;
        double sunRadius            = sun != null ? sun.getRadius() : 696000;                                   // km  TODO o va el vol. mean radius ?? 695700
        double sunMass              = sun != null ? sun.getMass() : 1988500 * Math.pow(10, 24);                 // kg

        /* Spaceship */
        double stationHeight        = 1500;                                                                     // km
        double earthInitialAngle    = getAngle(earthX, earthY);
        double spaceshipX           = earthX + (earthRadius + stationHeight) * Math.cos(earthInitialAngle);
        double spaceshipY           = earthY + (earthRadius + stationHeight) * Math.sin(earthInitialAngle);
        double tanAngle             = (earthInitialAngle + (Math.PI/2)) % (2*Math.PI);
        double orbitalVelocity      = 7.12;                                                                     // km/s
        double initSpeed            = spaceshipInitialSpeed;
        double initialVelocity      = initSpeed + orbitalVelocity + earthVelocity;                              // km // TODO elegir un momento  para earthVelocity
        double spaceshipMass        = 2 * Math.pow(10, 5);                                                      // kg
        double spaceshipVx          = initialVelocity * Math.cos(tanAngle);
        double spaceshipVy          = initialVelocity * Math.sin(tanAngle);

        earth = earth != null ? earth : new AcceleratedParticle()
            .withType(ParticleType.EARTH)
            .withMass(earthMass)
            .withRadius(earthRadius)
            .withVx(earthVx)
            .withVy(earthVy)
            .withX(earthX)
            .withY(earthY)
        ;

        sun = sun != null ? sun : new AcceleratedParticle() // inicia en (0,0)
            .withType(ParticleType.SUN)
            .withMass(sunMass)
            .withRadius(sunRadius)
            .withX(sunX)
            .withY(sunY)
            .withVx(sunVx)
            .withVy(sunVy)
        ;

        spaceship = new AcceleratedParticle()
            .withType(ParticleType.SPACESHIP)
            .withMass(spaceshipMass)
            .withVx(spaceshipVx)
            .withVy(spaceshipVy)
            .withX(spaceshipX)
            .withY(spaceshipY)
        ;

        final List<AcceleratedParticle> particles = Stream.of(earth, sun).collect(Collectors.toList());
        if (spaceshipPresent) particles.add(spaceship);

        return particles;
    }

    private void addDerivativesToParticles(List<AcceleratedParticle> particles) {
        BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionX = getAccelerationFunctionX();
        BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionY = getAccelerationFunctionY();

        double accX, accY, m;

        for (AcceleratedParticle particle : particles) {
            particle.withAccelerationFunctionX(accelerationFunctionX);
            particle.withAccelerationFunctionY(accelerationFunctionY);

            m                                       = particle.getMass();

            // x
            accX                                    = particle.getPositionDerivativeX(2, particles);
            ArrayList<Double> furtherDerivativesX   = particle.getFurtherDerivativesX();
            furtherDerivativesX.set(0, 0.0);
            furtherDerivativesX.set(1, 0.0);
            furtherDerivativesX.set(2, 0.0);
            particle.setFurtherDerivativesX(furtherDerivativesX);

            // y
            accY                                    = particle.getPositionDerivativeY(2, particles);
            ArrayList<Double> furtherDerivativesY   = particle.getFurtherDerivativesY();
            furtherDerivativesY.set(0, 0.0);
            furtherDerivativesY.set(1, 0.0);
            furtherDerivativesY.set(2, 0.0);
            particle.setFurtherDerivativesY(furtherDerivativesY);

            particle.setForceX(m * accX);
            particle.setForceY(m * accY);
        }
    }

    protected BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> getAccelerationFunctionY() {
        return (current, all) -> {
            double m = current.getMass();

            final List<AcceleratedParticle> others = removedFromList(all, current);

            double forceY = 0;

            for (AcceleratedParticle other : others) {
                double distance = current.distance(other);
                double gravityForceNorm = GRAVITY_CONSTANT * ((current.getMass()*other.getMass())/(Math.pow(distance, 2)));

                forceY += gravityForceNorm * (other.getY() - current.getY()) / distance; // TODO: Chequear con el profe que |rj - ri| es la norma 2 y que j es la otra partícula y i es la actual
            }

            return forceY/m;
        };
    }

    protected BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> getAccelerationFunctionX() {
        return (current, all) -> {
            double m = current.getMass();

            final List<AcceleratedParticle> others = removedFromList(all, current);

            double forceX = 0;

            for (AcceleratedParticle other : others) {
                double distance = current.distance(other);
                double gravityForceNorm = GRAVITY_CONSTANT * ((current.getMass()*other.getMass())/(Math.pow(distance, 2)));

                forceX += gravityForceNorm * (other.getX() - current.getX()) / distance; // TODO: Chequear con el profe que |rj - ri| es la norma 2 y que j es la otra partícula y i es la actual
            }

            return forceX/m;
        };
    }

    private static double getAngle(double x, double y) {

        double d = x == 0 ? (y > 0 ? Math.PI/2 : 3*Math.PI/2) : Math.atan(y/x);
        if (d < 0) {
            if (x < 0) {
                d = d + Math.PI;
            }
            else {
                d = d + 2*Math.PI;
            }
        }
        else { // d >= 0
            if (y < 0) {
                d = d + Math.PI;
            }
        }

        return d;
    }

    private static List<AcceleratedParticle> removedFromList(List<AcceleratedParticle> all, AcceleratedParticle particle) {
        return all.stream()
            .filter(p -> !p.getType().equals(particle.getType()))
            .collect(Collectors.toList());
    }


    // Autogenerated //

    public PlanetsSimulation withIntegration(Integration integration) {
        setIntegration(integration);
        return this;
    }
    public PlanetsSimulation withDt(double dt) {
        setDt(dt);
        return this;
    }
    public PlanetsSimulation withMaxTime(double maxTime) {
        setMaxTime(maxTime);
        return this;
    }
    public PlanetsSimulation withSaveFactor(long saveFactor) {
        setSaveFactor(saveFactor);
        return this;
    }
    public PlanetsSimulation withStatusBarActivated(boolean statusBarActivated) {
        setStatusBarActivated(statusBarActivated);
        return this;
    }

    public boolean isSpaceshipPresent() {
        return spaceshipPresent;
    }
    public void setSpaceshipPresent(boolean spaceshipPresent) {
        this.spaceshipPresent = spaceshipPresent;
    }
    public PlanetsSimulation withSpaceshipPresent(boolean spaceshipPresent) {
        setSpaceshipPresent(spaceshipPresent);
        return this;
    }

    public double getSpaceshipInitialSpeed() {
        return spaceshipInitialSpeed;
    }
    public void setSpaceshipInitialSpeed(double initialSpeed) {
        this.spaceshipInitialSpeed = initialSpeed;
    }
    public PlanetsSimulation withSpaceshipInitialSpeed(double initialSpeed) {
        setSpaceshipInitialSpeed(initialSpeed);
        return this;
    }

    public void setEarth(AcceleratedParticle earth) {
        this.earth = earth;
    }
    public void setSun(AcceleratedParticle sun) {
        this.sun = sun;
    }
}

