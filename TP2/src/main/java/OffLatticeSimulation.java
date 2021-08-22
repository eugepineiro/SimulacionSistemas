import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class OffLatticeSimulation {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // JSON file to Java object
            Config config = mapper.readValue(new File("src/main/resources/config/config.json"), Config.class);

            if (1.0 * config.getL_grid_side() / config.getM_grid_dimension() <= (config.getR_interaction_radius() + 2.0 * config.getL_grid_side() / 50) ) {
                throw new IllegalArgumentException("L/M > rc");
            }

            // Configure random
            Random r;
            Long seed = config.getSeed();
            if (seed == null)
                r = new Random();
            else
                r = new Random(seed);

            List<VelocityParticle> particles = VelocityParticlesGenerator.generateRandom(config.getN_number_of_particles(), config.getL_grid_side(), 0., config.getSpeed(), r);

            List<List<VelocityParticle>> frames;

            long startTime = System.nanoTime();

            frames = OffLattice.simulate(particles, config.getR_interaction_radius(), config.getM_grid_dimension(), config.getL_grid_side(),config.getNoise_amplitude(),  config.getFrames(), r);

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;

            // save results

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}