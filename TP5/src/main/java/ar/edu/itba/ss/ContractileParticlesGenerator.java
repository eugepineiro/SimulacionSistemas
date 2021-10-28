package ar.edu.itba.ss;

import ar.edu.itba.ss.models.ContractileParticle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;

public class ContractileParticlesGenerator {

    public static List<ContractileParticle> generateRandomParticles(Random r, long N, double roomHeight, double roomWidth, Double minRadius, double maxRadius) {
        return generateRandomParticles(Collections.emptyList(), r, N, roomHeight, roomWidth, minRadius, maxRadius);
    }

    public static List<ContractileParticle> generateRandomParticles(List<ContractileParticle> particles, Random r, long N, double roomHeight, double roomWidth, Double minRadius, double maxRadius){
        List<ContractileParticle> generated = new ArrayList<>(particles);

        double x, y, angle, newSpeed;
        double radius = minRadius;

        for (long i = 0; i < N; i++) {
            Particle newParticle;
            do {
                x = r.nextDouble() * (roomWidth  - 2*radius) + radius; // upper bound excluded
                y = r.nextDouble() * (roomHeight - 2*radius) + radius; // upper bound excluded
                newParticle = new Particle(i+1, x, y, radius);
            } while (!allTestTrue(generated, newParticle, new ParticleCollision().negate()));

            angle    = 0;   // TODO: Preguntar valor inicial
            newSpeed = 0;   // TODO: Preguntar valor inicial

            generated.add(new ContractileParticle(newParticle, newSpeed, angle));
        }

        return generated;
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

    private static boolean allTestTrue(List<ContractileParticle> particles, Particle particle, BiPredicate<Particle, Particle> condition) {
        for (ContractileParticle p: particles)
            if (!condition.test(particle, p)) return false;
        return true;
    }
}
