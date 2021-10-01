package ar.edu.itba.ss;

import ar.edu.itba.ss.integrations.Integration;
import ar.edu.itba.ss.models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MarsSimulation implements Simulation<List<Frame>> {

    // Status bar
    private final   int             STATUS_BAR_SIZE         = 31;

    private         Integration     integration;
    private         double          dt;
    private         double          maxTime;
    private         long            saveFactor;
    private         boolean         statusBarActivated;


    private         AcceleratedParticle     earth, sun, spaceship, mars;



    public List simulate() {

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

        List<Frame> frames = new ArrayList<>();

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

        List<ParticleHistory> histories = Stream.of(earth, sun, mars, spaceship)
                .map(p -> {
                        final ParticleHistory history = new ParticleHistory()
                            .withPast(p)
                            .withPresent(p.clone())
                            ;

                        integration.setup(history, dt);

                        return history;
                })
                .collect(Collectors.toList());

        long count = 0;
        for (double time = 0; time <= maxTime; time += dt) {   // currentTime
            if (statusBarActivated) Utils.printLoadingBar(time/maxTime, STATUS_BAR_SIZE);

            if (count % saveFactor == 0) {
                frames.add(new Frame()
                        .withParticles(histories.stream().map(ParticleHistory::getPresent).collect(Collectors.toList()))
                        .withTime(time)
                );
            }
            for (ParticleHistory h : histories) {
                h.setFuture(integration.update(Collections.singletonList(h.getPresent()), h.getPresent(), h.getPast(), dt));       //updated particle with ri(t+dt) y vi(t)

                h.setPast(h.getPresent());
                h.setPresent(h.getFuture());
            }

            count++;
        }

        return frames;
    }

    public List<AcceleratedParticle> removedFromList(List<AcceleratedParticle> all, AcceleratedParticle particle) {
        return all.stream()
            .filter(p -> !(p.getType().equals(particle.getType()) || p.getId() == particle.getId()))
            .collect(Collectors.toList());
    }

    //////////////// Autogenerated /////////////////

    public Integration getIntegration() {
        return integration;
    }
    public void setIntegration(Integration integration) {
        this.integration = integration;
    }
    public MarsSimulation withIntegration(Integration integration) {
        setIntegration(integration);
        return this;
    }

    public double getDt() {
        return dt;
    }
    public void setDt(double dt) {
        this.dt = dt;
    }
    public MarsSimulation withDt(double dt) {
        setDt(dt);
        return this;
    }

    public double getMaxTime() {
        return maxTime;
    }
    public void setMaxTime(double maxTime) {
        this.maxTime = maxTime;
    }
    public MarsSimulation withMaxTime(double maxTime) {
        setMaxTime(maxTime);
        return this;
    }

    public long getSaveFactor() {
        return saveFactor;
    }
    public void setSaveFactor(long saveFactor) {
        this.saveFactor = saveFactor;
    }
    public MarsSimulation withSaveFactor(long saveFactor) {
        setSaveFactor(saveFactor);
        return this;
    }

    public boolean isStatusBarActivated() {
        return statusBarActivated;
    }
    public void setStatusBarActivated(boolean statusBarActivated) {
        this.statusBarActivated = statusBarActivated;
    }
    public MarsSimulation withStatusBarActivated(boolean statusBarActivated) {
        setStatusBarActivated(statusBarActivated);
        return this;
    }
}
