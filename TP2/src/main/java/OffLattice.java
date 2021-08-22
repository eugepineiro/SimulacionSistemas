import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OffLattice {

    public static List<List<VelocityParticle>> simulate(List<VelocityParticle> particles, double interactionRadius, int M, int L, double noiseAmplitude, int numberOfFrames, Random r) {
        Map<VelocityParticle, List<VelocityParticle>> neighbours;



        // Frame 1
        // Particulas con sus posiciones, angulos, velocidades

        // Frame 2
        // Particulas con sus posiciones y vecinos

        List<List<VelocityParticle>> frames = new ArrayList<>();

        List<VelocityParticle> previousFrame = particles;

        for(int i = 0; i < numberOfFrames; i++) {
           // previousFrame = simulateFrame(previousFrame, interactionRadius, M, L, noiseAmplitude, r);
            frames.add(previousFrame);
        }

        return frames;
    }
    /*
    private static List<VelocityParticle> simulateFrame(List<VelocityParticle> previousFrame, double interactionRadius, int M, int L, double noiseAmplitude , Random r) {
        Map<VelocityParticle, List<VelocityParticle>> neighbours = CellIndexMethod.search(Grid.build(previousFrame, M, L), interactionRadius, M, L, true);
        // update particles speed with angle average

        
        return neighbours.entrySet().stream((Map.Entry<VelocityParticle, List<VelocityParticle>> entry) -> {
            VelocityParticle p = entry.getKey();
            List<VelocityParticle> n = entry.getValue();

            n.add(p); // Avg with its own angle

            double sinAvg = 0, cosAvg = 0, newAngle;

            for (VelocityParticle vp: n) {
                sinAvg += Math.sin(vp.getAngle());
                cosAvg += Math.cos(vp.getAngle());
            }
            sinAvg = sinAvg/n.size();
            cosAvg = cosAvg/n.size();

            double noise = r.nextDouble() * 2*(noiseAmplitude/2.0) + (- noiseAmplitude/2.0);
            newAngle =  Math.atan2(sinAvg, cosAvg) + noise;

            return new VelocityParticle(
                p.getId(), 
                p.getX() + p.getSpeed() * Math.cos(newAngle),
                p.getY() + p.getSpeed() * Math.sin(newAngle),
                p.getRadius(),
                p.getSpeed(),
                p.getAngle()
            );
        }).collect(Collectors.toList());
    }
    */
}
