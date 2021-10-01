package ar.edu.itba.ss;

import ar.edu.itba.ss.integrations.Integration;
import ar.edu.itba.ss.models.AcceleratedParticle;
import ar.edu.itba.ss.models.Frame;
import ar.edu.itba.ss.models.ParticleType;
import ar.edu.itba.ss.models.TriFunction;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class OscillatorSimulation implements Simulation<List<Frame>> {

    // Status bar
    private final   int                     STATUS_BAR_SIZE         = 31;

    private         Integration             integration;
    private         double                  dt;
    private         double                  maxTime;
    private         long                    saveFactor;
    private         boolean                 statusBarActivated;

    private         AcceleratedParticle     particle;

    // Stop conditions

    public List<Frame> simulate() {

        // Initial Conditions
        double k        = Math.pow(10,4);       // N/m
        double r        = 1;                    // m
        double gamma    = 100;                  // kg/s
        double mass     = 70;                   // kg
        double vy       = -gamma/(2*mass);      // m/s

        TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double> calculateDerivativeX = new TriFunction<Integer, AcceleratedParticle, List<AcceleratedParticle>, Double>() {
            @Override
            public Double apply(Integer order, AcceleratedParticle current, List<AcceleratedParticle> all) {
                double pos = current.getX();
                double vel = current.getVx();
                double m = current.getMass();

                switch (order) {
                    case 0:
                        return pos;
                    case 1:
                        return vel;
                    case 2:
                        return (- k * pos - gamma * vel)/m;
                    case 3:
                        return (- k * vel - gamma * this.apply(2, current, all))/m;
                    case 4:
                        return (- k * this.apply(2, current, all) - gamma * this.apply(3, current, all))/m;
                    case 5:
                        return (- k * this.apply(3, current, all) - gamma * this.apply(4, current, all))/m;
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

                switch (order) {
                    case 0:
                        return pos;
                    case 1:
                        return vel;
                    case 2:
                        return (- k * pos - gamma * vel)/m;
                    case 3:
                        return (- k * vel - gamma * this.apply(2, current, all))/m;
                    case 4:
                        return (- k * this.apply(2, current, all) - gamma * this.apply(3, current, all))/m;
                    case 5:
                        return (- k * this.apply(3, current, all) - gamma * this.apply(4, current, all))/m;
                }
                return (double) 0;
            }
        };

        particle = new AcceleratedParticle()
            .withType(ParticleType.OSCILLABLE)
            .withY(r)
            .withVy(vy)
            .withMass(mass)
            .withDerivativeFunctionX(calculateDerivativeX)
            .withDerivativeFunctionY(calculateDerivativeY)
            ;

        particle.setForceY(mass*calculateDerivativeY.apply(2, particle, Collections.singletonList(particle)));

        List<Frame> frames = new ArrayList<>();

        AcceleratedParticle previous    = particle;                    // (t-dt)
        AcceleratedParticle current     = previous.clone();            // (t)
        AcceleratedParticle next        = null;                        // (t+dt)

        integration.setup(previous, current, next, dt);

        long count = 0;
        for (double time = 0; time <= maxTime; time += dt) {   // currentTime
            if (statusBarActivated) Utils.printLoadingBar(time/maxTime, STATUS_BAR_SIZE);

            if (count % saveFactor == 0) {
                frames.add(new Frame()
                    .withParticles(Collections.singletonList(current.clone()))
                    .withTime(time)
                );
            }

            next = integration.update(Collections.singletonList(current), current, previous, dt);       //updated particle with ri(t+dt) y vi(t)

            previous = current;
            current  = next;

            count++;
        }

        return frames;
    }

    //////////////// Autogenerated /////////////////


    public Integration getIntegration() {
        return integration;
    }
    public void setIntegration(Integration integration) {
        this.integration = integration;
    }
    public OscillatorSimulation withIntegration(Integration integration) {
        setIntegration(integration);
        return this;
    }

    public double getDt() {
        return dt;
    }
    public void setDt(double dt) {
        this.dt = dt;
    }
    public OscillatorSimulation withDt(double dt) {
        setDt(dt);
        return this;
    }

    public double getMaxTime() {
        return maxTime;
    }
    public void setMaxTime(double maxTime) {
        this.maxTime = maxTime;
    }
    public OscillatorSimulation withMaxTime(double maxTime) {
        setMaxTime(maxTime);
        return this;
    }

    public long getSaveFactor() {
        return saveFactor;
    }
    public void setSaveFactor(long saveFactor) {
        this.saveFactor = saveFactor;
    }
    public OscillatorSimulation withSaveFactor(long saveFactor) {
        setSaveFactor(saveFactor);
        return this;
    }

    public boolean isStatusBarActivated() {
        return statusBarActivated;
    }
    public void setStatusBarActivated(boolean statusBarActivated) {
        this.statusBarActivated = statusBarActivated;
    }
    public OscillatorSimulation withStatusBarActivated(boolean statusBarActivated) {
        setStatusBarActivated(statusBarActivated);
        return this;
    }
}
