import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/*
* (random * (upper - lower)) + lower
* */

public class VelocityParticlesGenerator {
    public static List<VelocityParticle> generateRandom(int N, int L, Double fixedRadius, double speed, Random r) {
        return ParticlesGenerator.generateRandom(N, L, fixedRadius, r, new ParticleCollision().negate())
                .stream()
                .map(p -> {
                    double angle;
                    angle = r.nextDouble() * 2*Math.PI;
                    return new VelocityParticle(p, speed, angle);
                })
                .collect(Collectors.toList());
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
}
