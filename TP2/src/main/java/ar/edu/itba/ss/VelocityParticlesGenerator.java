package ar.edu.itba.ss;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/*
* (random * (upper - lower)) + lower
* */

public class VelocityParticlesGenerator {
    public static List<VelocityParticle> generateRandom(int N, int L, double fixedRadius, double speed, Random r) {
        return ParticlesGenerator.generateRandom(N, L, fixedRadius, r)
                .stream()
                .map(p -> {
                    double angle;
                    angle = r.nextDouble() * 2*Math.PI;
                    return new VelocityParticle(p, speed, angle);
                })
                .collect(Collectors.toList());
    }
}
