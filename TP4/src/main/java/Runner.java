import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Runner {

    private static final Integer    EVENTS_SAVING_EACH     = 25;
    private static final String     CONFIG_PATH             = "TP4/src/main/resources/config/config.json";
    private static final String     JSON_WRITER_PATH        = "TP4/src/main/resources/postprocessing";
    private static final String     XYZ_WRITER_PATH         = "TP4/src/main/resources/ovito";
    private static final String     FILENAME                = "SdS_TP4_2021Q2G01_output";
    private static final String     POSTPROCESSING_FILENAME = "SdS_TP4_2021Q2G01_results";

    // Main

    public static void main(String[] args) {
        JsonWriter.setPath(JSON_WRITER_PATH);
        XYZ_Writer.setPath(XYZ_WRITER_PATH);

        ObjectMapper mapper = new ObjectMapper();
        long curr;
        try {
            // JSON file to Java object
            Config config = mapper.readValue(new File(CONFIG_PATH), Config.class);

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
            double minSpeed, maxSpeed;

            lGridSide                                   = config.getL_grid_side();
            numberOfParticles                           = config.getN_number_of_particles();


            boolean simpleSimulation                    = true;

            // Multiple simulations if specified

            if (!simpleSimulation) return;

            List<VelocityParticle> particles = new ArrayList<>();

            // Generate particles

            long startTime = System.nanoTime();

//            List<ExtendedEvent> events = Brownian.simulate(particles, bigParticle, lGridSide, maxEvents, true);

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;

            System.out.println("Time in ms: " + timeElapsed / 1000000.0);

            // Save results

            // Ovito
//            new XYZ_Writer(FILENAME).addAllFrames(events).writeAndClose();

            System.out.println("Finished saving " + FILENAME + ".exyz");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Postprocessing

    private static final int LOADING_BAR_SIZE = 20;

}