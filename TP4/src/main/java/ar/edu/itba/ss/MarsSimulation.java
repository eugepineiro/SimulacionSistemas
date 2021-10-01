package ar.edu.itba.ss;

import ar.edu.itba.ss.integrations.Integration;
import ar.edu.itba.ss.models.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MarsSimulation extends AbstractSimulation {

    private LocalDateTime launchDate;

    @Override
    public List<AcceleratedParticle> getParticles() {

//        double gravityConstant  = Math.pow(6.693*10, -11);                // m³/(kg*s²)   // TODO change units
        double gravityConstant  = Math.pow(6.693*10, -20);                  // km³/(kg*s²)
        /* Earth */
        double earthRadius      = 6378.137;                                 // km
        double earthMass        = 5.97219 * Math.pow(10, 24);               // kg
        double earthVelocity    = 0;                                        // TODO elegir un
        double earthVx          = -9.322979134387409 * Math.pow(10,-1);     // km/s
        double earthVy          = 2.966365033636722 * Math.pow(10,1);       // km/s
        double earthX           = 1.500619962348151 * Math.pow(10,8);       // km
        double earthY           = 2.288499248197072 * Math.pow(10,6);       // km
        /* Sun */
        double sunRadius        = 696000;                                   // km  TODO o va el vol. mean radius ?? 695700
        double sunMass          = 1988500 * Math.pow(10, 24);               // kg
        /* Mars */
        double marsMass         = 6.4171 * Math.pow(10, 23);                // kg
        double marsRadius       = 3389.92;                                  // km
        double marsVx           = 4.435907910045917;                        // km/s
        double marsVy           = -2.190044178514185 * Math.pow(10,1);      // km/s
        double marsX            = -2.426617401833969 * Math.pow(10,8);      // km/s
        double marsY            = -3.578836154354768 * Math.pow(10,7) ;     // km/s

        /* Spaceship */
        double stationHeight    = 1500;                                     // km
        double orbitalVelocity  = 7.12;                                     // km/s
        double initialVelocity  = 8 + orbitalVelocity + earthVelocity;      // km // TODO elegir un momento  para earthVelocity
        double spaceshipMass    = Math.pow(2*10, 5);                        // kg

        TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> calculateDerivativeX = new TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double>() {
            @Override
            public Double apply(Integer order, AcceleratedParticle current, List<AcceleratedParticle> all) {
                double pos = current.getX();
                double vel = current.getVx();
                double m = current.getMass();

                final List<AcceleratedParticle> others = removedFromList(all, current);

                double forceX = 0;

                for (AcceleratedParticle other : others) {
                    double distance = current.distance(other);
                    double gravityForceNorm = gravityConstant * ((current.getMass()*other.getMass())/(Math.pow(distance, 2)));

                    forceX += gravityForceNorm * (current.getX() - other.getX()) / distance; // TODO: Chequear con el profe que |rj - ri| es la norma 2 y que j es la otra partícula y i es la actual
                }

                switch (order) {
                    case 0:
                        return pos;
                    case 1:
                        return vel;
                    case 2:
                        return forceX/m;
                    case 3:
                        return 0.0;
                    case 4:
                        return 0.0;
                    case 5:
                        return 0.0;
                }
                return (double) 0;
            }
        };

        TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> calculateDerivativeY = new TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double>() {
            @Override
            public Double apply(Integer order, AcceleratedParticle current, List<AcceleratedParticle> all) {
                double pos = current.getY();
                double vel = current.getVy();
                double m = current.getMass();

                final List<AcceleratedParticle> others = removedFromList(all, current);

                double forceY = 0;

                for (AcceleratedParticle other : others) {
                    double distance = current.distance(other);
                    double gravityForceNorm = gravityConstant * ((current.getMass()*other.getMass())/(Math.pow(distance, 2)));

                    forceY += gravityForceNorm * (current.getY() - other.getY()) / distance; // TODO: Chequear con el profe que |rj - ri| es la norma 2 y que j es la otra partícula y i es la actual

                }

                switch (order) {
                    case 0:
                        return pos;
                    case 1:
                        return vel;
                    case 2:
                        return forceY/m;
                    case 3:
                        return 0.0;
                    case 4:
                        return 0.0;
                    case 5:
                        return 0.0;
                }
                return (double) 0;
            }
        };

        AcceleratedParticle earth, sun, spaceship, mars;

        earth = new AcceleratedParticle()
                .withType(ParticleType.EARTH)
                .withMass(earthMass)
                .withRadius(earthRadius)
                .withVx(earthVx)
                .withVy(earthVy)
                .withX(earthX)
                .withY(earthY)
                .withDerivativeFunctionX(calculateDerivativeX)
                .withDerivativeFunctionY(calculateDerivativeY)
        ;

        sun = new AcceleratedParticle() // inicia en (0,0)
                .withType(ParticleType.SUN)
                .withMass(sunMass)
                .withRadius(sunRadius)
                .withDerivativeFunctionX(calculateDerivativeX)
                .withDerivativeFunctionY(calculateDerivativeY)
        ;

        mars = new AcceleratedParticle()
                .withType(ParticleType.MARS)
                .withMass(marsMass)
                .withRadius(marsRadius)
                .withVx(marsVx)
                .withVy(marsVy)
                .withX(marsX)
                .withY(marsY)
                .withDerivativeFunctionX(calculateDerivativeX)
                .withDerivativeFunctionY(calculateDerivativeY)
        ;

        spaceship = new AcceleratedParticle()
                .withType(ParticleType.SPACESHIP)
                .withMass(spaceshipMass)
                .withDerivativeFunctionX(calculateDerivativeX)
                .withDerivativeFunctionY(calculateDerivativeY)
        ;

        return Arrays.asList(earth, sun, mars, spaceship);
    }

    @Override
    public boolean stop(List<ParticleHistory> histories) {
        return false;
    }

    private static List<AcceleratedParticle> removedFromList(List<AcceleratedParticle> all, AcceleratedParticle particle) {
        return all.stream()
            .filter(p -> !(p.getType().equals(particle.getType()) || p.getId() == particle.getId()))
            .collect(Collectors.toList());
    }



    // Autogenerated //

    public MarsSimulation withIntegration(Integration integration) {
        setIntegration(integration);
        return this;
    }
    public MarsSimulation withDt(double dt) {
        setDt(dt);
        return this;
    }
    public MarsSimulation withMaxTime(double maxTime) {
        setMaxTime(maxTime);
        return this;
    }
    public MarsSimulation withSaveFactor(long saveFactor) {
        setSaveFactor(saveFactor);
        return this;
    }
    public MarsSimulation withStatusBarActivated(boolean statusBarActivated) {
        setStatusBarActivated(statusBarActivated);
        return this;
    }

    public LocalDateTime getLaunchDate() {
        return launchDate;
    }
    public void setLaunchDate(LocalDateTime launchDate) {
        this.launchDate = launchDate;
    }
    public MarsSimulation withLaunchDate(LocalDateTime launchDate) {
        setLaunchDate(launchDate);
        return this;
    }
}
