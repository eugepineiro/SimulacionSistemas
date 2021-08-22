import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class OffLatticeSimulation {

    private static final String FILENAME = "SdS_TP2_2021Q2G01_output";

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        long curr;
        try {
            // JSON file to Java object
            Config config = mapper.readValue(new File("TP2/src/main/resources/config/config.json"), Config.class);

            if (1.0 * config.getL_grid_side() / config.getM_grid_dimension() <= (config.getR_interaction_radius() + 2.0 * config.getL_grid_side() / 50) ) {
                throw new IllegalArgumentException("L/M > rc");
            }

            // Configure random
            Random r;
            Long seed = config.getSeed();
            if (seed == 0) {
                curr = System.currentTimeMillis();
                System.out.printf("Current seed: %d\n", curr);
                r = new Random(curr);
            }
            else
                r = new Random(seed);

            int numberOfParticles = (int) (config.getDensity() * Math.pow(config.getL_grid_side(), 2));

            List<VelocityParticle> particles = VelocityParticlesGenerator.generateRandom(numberOfParticles, config.getL_grid_side(), 0., config.getSpeed(), r);

            List<List<VelocityParticle>> frames;

            long startTime = System.nanoTime();

            frames = OffLattice.simulate(particles, config.getR_interaction_radius(), config.getM_grid_dimension(), config.getL_grid_side(),config.getNoise_amplitude(),  config.getFrames(), r);

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;

            System.out.println("Time in ms: " + timeElapsed / 1000000.0 );

            // save results
            new XYZ_Writer(FILENAME).addAllFrames(frames).writeAndClose();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}