import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BruteForceMethod extends SearchMethod {
    public static Map<Particle, List<Particle>> search(List<Particle> particles, double interactionRadius, int L, boolean periodic) {
        Map<Particle, List<Particle>> result = new HashMap<>();

        Particle p;

        for(int i = 0 ; i < particles.size() ; i++) {
            p = particles.get(i);
            if (!result.containsKey(p)) {
                result.put(p, new ArrayList<>());
            }

            for(int j = i+1 ; j < particles.size() ; j++) {
                // si o si hay que hacerlo con la diagonal porque sino add neighbour agrega repetidos a la lista
                addNeighbours(result, particles.get(i), particles.get(j), interactionRadius, L, periodic);
            }
        }

        return result;
    }
}
