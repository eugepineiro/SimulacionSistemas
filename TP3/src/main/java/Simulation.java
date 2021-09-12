import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Simulation {

    private static final String JSON_WRITER_PATH = "TP3/src/main/resources/postprocessing";
    private static final String XYZ_WRITER_PATH = "TP3/src/main/resources/ovito";
    private static final String FILENAME = "SdS_TP3_2021Q2G01_output";
    private static final String POSTPROCESSING_FILENAME = "SdS_TP3_2021Q2G01_results";

    public static void main(String[] args) {
        JsonWriter.setPath(JSON_WRITER_PATH);
        XYZ_Writer.setPath(XYZ_WRITER_PATH);

        ObjectMapper mapper = new ObjectMapper();
        long curr;
        try {
            // JSON file to Java object
            Config config = mapper.readValue(new File("TP3/src/main/resources/config/config.json"), Config.class);

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


            long numberOfParticles, lGridSide, maxEvents;

            lGridSide = config.getL_grid_side();
            numberOfParticles = config.getN_number_of_particles();
            maxEvents = config.getMax_events();

            List<VelocityParticle> particles = new ArrayList<>();

            // Generate big particle
            VelocityParticle bigParticle = new VelocityParticle(ParticleType.BIG, 0, lGridSide/2.0, lGridSide/2.0, 0.7, 0.0, 0.0, 2.0);
            particles.add(bigParticle);

            // Generate small particles
            particles = VelocityParticlesGenerator.generateRandomWaterParticles(particles, numberOfParticles, lGridSide, 0.2, 2.0, r, 0.9); // TODO speed entre -2 y 2

            long startTime = System.nanoTime();

            List<ExtendedEvent> events = Brownian.simulate(particles, bigParticle, lGridSide, maxEvents);

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;

            System.out.println("Time in ms: " + timeElapsed / 1000000.0);

            // Save results

            // 1. Ovito
            new XYZ_Writer(FILENAME).addAllFrames(events).writeAndClose();

            // 2. Postprocessing
            new JsonWriter(POSTPROCESSING_FILENAME)
                .withObj(events)
                .write();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}