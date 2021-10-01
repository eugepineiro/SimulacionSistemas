package ar.edu.itba.ss;

import ar.edu.itba.ss.integrations.Integration;
import ar.edu.itba.ss.models.AcceleratedParticle;
import ar.edu.itba.ss.models.ParticleType;

import java.time.LocalDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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

        BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionX = (current, all) -> {
            double m = current.getMass();

            final List<AcceleratedParticle> others = removedFromList(all, current);

            double forceX = 0;

            for (AcceleratedParticle other : others) {
                double distance = current.distance(other);
                double gravityForceNorm = gravityConstant * ((current.getMass()*other.getMass())/(Math.pow(distance, 2)));

                forceX += gravityForceNorm * (other.getX() - current.getX()) / distance; // TODO: Chequear con el profe que |rj - ri| es la norma 2 y que j es la otra partícula y i es la actual
            }

            return forceX/m;
        };

        BiFunction<AcceleratedParticle, List<AcceleratedParticle>, Double> accelerationFunctionY = (current, all) -> {
            double m = current.getMass();

            final List<AcceleratedParticle> others = removedFromList(all, current);

            double forceY = 0;

            for (AcceleratedParticle other : others) {
                double distance = current.distance(other);
                double gravityForceNorm = gravityConstant * ((current.getMass()*other.getMass())/(Math.pow(distance, 2)));

                forceY += gravityForceNorm * (other.getY() - current.getY()) / distance; // TODO: Chequear con el profe que |rj - ri| es la norma 2 y que j es la otra partícula y i es la actual
            }

            return forceY/m;
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
        ;

        sun = new AcceleratedParticle() // inicia en (0,0)
                .withType(ParticleType.SUN)
                .withMass(sunMass)
                .withRadius(sunRadius)
        ;

        mars = new AcceleratedParticle()
                .withType(ParticleType.MARS)
                .withMass(marsMass)
                .withRadius(marsRadius)
                .withVx(marsVx)
                .withVy(marsVy)
                .withX(marsX)
                .withY(marsY)
        ;

        spaceship = new AcceleratedParticle()
                .withType(ParticleType.SPACESHIP)
                .withMass(spaceshipMass)
        ;

        final List<AcceleratedParticle> particles = Arrays.asList(earth, sun, mars, spaceship);

        double accX, accY, m;

        for (AcceleratedParticle particle : particles) {
            particle.withAccelerationFunctionX(accelerationFunctionX);
            particle.withAccelerationFunctionY(accelerationFunctionY);

            m                               = particle.getMass();

            // x
            accX                                    = particle.getPositionDerivativeX(2, Collections.singletonList(particle));
            ArrayList<Double> furtherDerivativesX   = particle.getFurtherDerivativesX();
            furtherDerivativesX.set(0, 0.0);
            furtherDerivativesX.set(1, 0.0);
            furtherDerivativesX.set(2, 0.0);
            particle.setFurtherDerivativesX(furtherDerivativesX);

            // y
            accY                                    = particle.getPositionDerivativeY(2, Collections.singletonList(particle));
            ArrayList<Double> furtherDerivativesY   = particle.getFurtherDerivativesY();
            furtherDerivativesY.set(0, 0.0);
            furtherDerivativesY.set(1, 0.0);
            furtherDerivativesY.set(2, 0.0);
            particle.setFurtherDerivativesY(furtherDerivativesY);

            particle.setForceX(m * accX);
            particle.setForceY(m * accY);
        }

        return particles;
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
