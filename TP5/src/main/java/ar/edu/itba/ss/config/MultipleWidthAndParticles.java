package ar.edu.itba.ss.config;

import java.util.LinkedList;
import java.util.List;

public class MultipleWidthAndParticles {

    public static class WidthAndParticles {
        private double width;
        private long particles;

        public double getWidth() {
            return width;
        }
        public void setWidth(double width) {
            this.width = width;
        }
        public WidthAndParticles withWidth(double width) {
            setWidth(width);
            return this;
        }

        public long getParticles() {
            return particles;
        }
        public void setParticles(long particles) {
            this.particles = particles;
        }
        public WidthAndParticles withParticles(long particles) {
            setParticles(particles);
            return this;
        }
    }

    private boolean activated;
    private List<Double> target_width;
    private List<Long> number_of_particles;
    private List<Long> seeds;

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public List<Double> getTarget_width() {
        return target_width;
    }

    public void setTarget_width(List<Double> target_width) {
        this.target_width = target_width;
    }

    public List<Long> getNumber_of_particles() {
        return number_of_particles;
    }

    public void setNumber_of_particles(List<Long> number_of_particles) {
        this.number_of_particles = number_of_particles;
    }

    public List<Long> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<Long> seeds) {
        this.seeds = seeds;
    }

    public List<WidthAndParticles> toWidthAndParticles() {
        if (target_width.size() != number_of_particles.size()) {
            throw new IllegalStateException("Width and Particles different sizes");
        }

        final List<WidthAndParticles> ret = new LinkedList<>();
        for (int i = 0; i < number_of_particles.size(); i++) {
            ret.add(new WidthAndParticles()
                .withParticles(number_of_particles.get(i))
                .withWidth(target_width.get(i))
            );
        }

        return ret;
    }
}
