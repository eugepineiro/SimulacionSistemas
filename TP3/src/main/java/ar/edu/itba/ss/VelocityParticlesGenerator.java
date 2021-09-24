package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;

/*
* (random * (upper - lower)) + lower
* */

public class VelocityParticlesGenerator {

    public static List<VelocityParticle> generateRandomWaterParticles(int N, int L, Double fixedRadius, double minSpeed, double maxSpeed, Random r, double mass) {
        return generateRandomWaterParticles(Collections.emptyList(), N, L, fixedRadius, minSpeed, maxSpeed, r, mass);
    }

    private static class ParticleCollision implements BiPredicate<Particle, Particle> {
        @Override
        public boolean test(Particle p1, Particle p2) {
            return ( Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2) ) <= Math.pow(p1.getRadius() + p2.getRadius(), 2);
        }

        @Override
        public BiPredicate<Particle, Particle> and(BiPredicate<? super Particle, ? super Particle> other) {
            return BiPredicate.super.and(other);
        }

        @Override
        public BiPredicate<Particle, Particle> negate() {
            return BiPredicate.super.negate();
        }

        @Override
        public BiPredicate<Particle, Particle> or(BiPredicate<? super Particle, ? super Particle> other) {
            return BiPredicate.super.or(other);
        }
    }

    public static List<VelocityParticle> generateRandomWaterParticles(List<VelocityParticle> particles, long N, long L, Double fixedRadius, double minSpeed, double maxSpeed, Random r, double mass){
        List<VelocityParticle> generated = new ArrayList<>(particles);

        double x, y, angle, newSpeed;
        double radius, maxRadius = L/50.0; // TODO: Is it okay?

        for (long i = 0; i < N; i++) {
            Particle newParticle;
            do {
                if (fixedRadius == null)
                    radius = r.nextDouble() * maxRadius; // upper bound excluded
                else
                    radius = fixedRadius;
                x = r.nextDouble() * (L - 2*radius) + radius; // upper bound excluded
                y = r.nextDouble() * (L - 2*radius) + radius; // upper bound excluded
                newParticle = new Particle(i+1, x, y, radius);
            } while (!allTestTrue(generated, newParticle, new ParticleCollision().negate()));
            angle = r.nextDouble() * 2*Math.PI;
            newSpeed = r.nextDouble() * (maxSpeed - minSpeed) + minSpeed;

            generated.add(new VelocityParticle(ParticleType.SMALL, newParticle, newSpeed, angle, mass));
        }

        return generated;
    }


    private static boolean allTestTrue(List<VelocityParticle> particles, Particle particle, BiPredicate<Particle, Particle> condition) {
        for (VelocityParticle p: particles)
            if (!condition.test(particle, p)) return false;
        return true;
    }
}
