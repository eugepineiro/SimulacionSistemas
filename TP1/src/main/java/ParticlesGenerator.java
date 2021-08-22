import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticlesGenerator {
    public static List<Particle> generateRandom(int N, int L, Double fixedRadius, Random r) {
        List<Particle> generated = new ArrayList<>();
        double x, y;
        double radius, maxRadius = L/50.0; // TODO: Is it okay?
        for (int i = 0; i < N; i++) {
            x = r.nextDouble() * L; // upper bound excluded
            y = r.nextDouble() * L; // upper bound excluded
            if (fixedRadius == null)
                radius = r.nextDouble() * maxRadius; // upper bound excluded
            else
                radius = fixedRadius;
            generated.add(new Particle(i, x, y, radius));
        }

        return generated;
    }
}
