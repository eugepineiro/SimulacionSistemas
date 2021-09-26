package ar.edu.itba.ss;

import ar.edu.itba.ss.integrations.Beeman;
import ar.edu.itba.ss.integrations.Gear;
import ar.edu.itba.ss.integrations.Integration;
import ar.edu.itba.ss.integrations.VerletOriginal;
import ar.edu.itba.ss.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Runner {

    private static final Integer    EVENTS_SAVING_EACH                  = 25;
    private static final String     CONFIG_PATH                         = "TP4/src/main/resources/config/config.json";
    private static final String     JSON_WRITER_PATH                    = "TP4/src/main/resources/postprocessing";
    private static final String     XYZ_WRITER_PATH                     = "TP4/src/main/resources/ovito";
    private static final String     OVITO_FILENAME                      = "SdS_TP4_2021Q2G01_output";
    private static final String     OSCILLATOR_POSTPROCESSING_FILENAME  = "SdS_TP4_2021Q2G01_oscillator_results";
    private static final String     MARS_POSTPROCESSING_FILENAME        = "SdS_TP4_2021Q2G01_mars_results";

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
            Random random;
            Long seed = config.getSeed();
            if (seed == 0) {
                curr = System.currentTimeMillis();
                System.out.printf("Current seed: %d\n", curr);
                random = new Random(curr);
            }
            else
                random = new Random(seed);

            boolean simpleSimulation            = true;

            // Multiple simulations if specified

            if (!simpleSimulation) return;

            // Simulation

            String system = config.getSystem();

            // Start simulation

            Simulation<List<Frame>> simulation;

            HashMap<String, Integration> integrationHashMap = new HashMap<String, Integration>() {{
                put(IntegrationType.VERLET_ORIGINAL.name().toLowerCase(),   new VerletOriginal());
                put(IntegrationType.BEEMAN.name().toLowerCase(),            new Beeman());
                put(IntegrationType.GEAR.name().toLowerCase(),              new Gear());
            }};

            if (system.equals(SystemType.OSCILLATOR.name().toLowerCase())) {
                simulation = new OscillatorSimulation()
                    .withIntegration(integrationHashMap.get(config.getIntegration()))
                    .withDt(config.getDt())
                    .withMaxTime(config.getMax_time())            // seconds
                    .withStatusBarActivated(config.getLoading_bar())
                    ;
            }
            else if (system.equals(SystemType.MARS.name().toLowerCase())) {
                simulation = new MarsSimulation()

                ;
            }
            else {
                throw new IllegalArgumentException(String.format("System type %s undefined\n", system));
            }

            long startTime = System.nanoTime();

            List<Frame> results = simulation.simulate();

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;

            System.out.println("Time in ms: " + timeElapsed / 1000000.0);

            results.forEach(System.out::println);

            // Save results
            
            new JsonWriter(OSCILLATOR_POSTPROCESSING_FILENAME)
                .withObj(results)
                .write();

            System.out.println("Finished saving " + OSCILLATOR_POSTPROCESSING_FILENAME + ".json");
            
            // Ovito
            new XYZ_Writer(OVITO_FILENAME).addAllFrames(results).writeAndClose();

            System.out.println("Finished saving " + OVITO_FILENAME + ".exyz");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Postprocessing

    private static final int LOADING_BAR_SIZE = 20;

}