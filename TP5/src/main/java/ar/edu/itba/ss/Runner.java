package ar.edu.itba.ss;

import static ar.edu.itba.ss.Utils.printLoadingBar;

import ar.edu.itba.ss.algorithms.CPMEscapeRoomSimulation;
import ar.edu.itba.ss.config.Config;
import ar.edu.itba.ss.dto.ExtendedEscapeSimulationsResult;
import ar.edu.itba.ss.models.ContractileParticle;
import ar.edu.itba.ss.models.EscapeRoomSimulationResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Runner {

    private static final String    CONFIG_PATH                                  = "TP5/src/main/resources/config/config.json";
    private static final String    JSON_WRITER_PATH                             = "TP5/src/main/resources/postprocessing";
    private static final String    XYZ_WRITER_PATH                              = "TP5/src/main/resources/ovito";

    private static final String    OVITO_FILENAME                               = "SdS_TP5_2021Q2G01_output";
    private static final String    POSTPROCESSING_FILENAME                      = "SdS_TP5_2021Q2G01_results";
    private static final String    POSTPROCESSING_MULTIPLE_SIMULATIONS_FILENAME = "SdS_TP5_2021Q2G01_multiple_simulations_results";

    // Main

    // Integrations

    public static void main(String[] args) {
        JsonWriter.setPath(JSON_WRITER_PATH);
        XYZ_Writer.setPath(XYZ_WRITER_PATH);

        ObjectMapper mapper = new ObjectMapper();
        try {
            // JSON file to Java object
            Config config = mapper.readValue(new File(CONFIG_PATH), Config.class);

            // Simulation

            boolean multipleRuns = false;

            if (config.getMultiple_simulations().isActivated()) {
                simulatewithMultipleSimulations(config, config.getMultiple_simulations().getSeeds());
                multipleRuns = true;
            }

            if (!multipleRuns) {
                runSimulation(config);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runSimulation(Config config) {

        // Configure random

        long curr;
        Random r;

        Long seed = config.getSeed();
        if (seed == 0) {
            curr = System.currentTimeMillis();
            System.out.printf("Current seed: %d\n", curr);
            r = new Random(curr);
        }
        else
            r = new Random(seed);

        // Generate particles

        final List<ContractileParticle> particles = ContractileParticlesGenerator.generateRandomParticles(
            r,
            config.getNumber_of_particles(),
            config.getRoom_height(),
            config.getRoom_width(),
            config.getMin_radius(),
            config.getMax_radius()
        );

        // Simulate

        final double dt = config.getMin_radius() / (2 * Math.max(config.getMax_desired_velocity(), config.getEscape_velocity()));

        final CPMEscapeRoomSimulation simulation = new CPMEscapeRoomSimulation()
            .withRandom             (r)
            .withDt                 (dt)
            .withSaveFactor         (config.getSave_factor())
            .withMaxTime            (config.getMax_time())
            .withStatusBarActivated (config.getLoading_bar())
            .withMinRadius          (config.getMin_radius())
            .withMaxRadius          (config.getMax_radius())
            .withEscapeSpeed        (config.getEscape_velocity())
            .withMaxDesiredSpeed    (config.getMax_desired_velocity())
            .withBeta               (config.getBeta())
            .withTau                (config.getTau())
            .withRoomHeight         (config.getRoom_height())
            .withRoomWidth          (config.getRoom_width())
            .withTargetWidth        (config.getTarget_width())
            .withOuterTargetDistance(config.getOuter_target_dist())
            .withOuterTargetWidth   (config.getOuter_target_width())
            .withParticles          (particles)
            ;

        long startTime = System.nanoTime();

        final EscapeRoomSimulationResult results = simulation.simulate();

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println();
        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        // Save results

        // Ovito
        new XYZ_Writer(OVITO_FILENAME)
            .withRoomHeight         (config.getRoom_height())
            .withRoomWidth          (config.getRoom_width())
            .withTargetWidth        (config.getTarget_width())
            .withOuterTargetDistance(config.getOuter_target_dist())
            .withOuterTargetWidth   (config.getOuter_target_width())
            .addAllFrames           (results.getFrames())
            .writeAndClose();

        System.out.println("Finished saving " + OVITO_FILENAME + ".exyz");
    }

    private static final int LOADING_BAR_SIZE = 20;

    static void simulatewithMultipleSimulations(Config config, List<Long> seeds) throws IOException {

        List<ExtendedEscapeSimulationsResult> simulationsResults = new LinkedList<>();

        Random r;
        List<ContractileParticle> particles;
        EscapeRoomSimulationResult results;

        long startTime = System.nanoTime();

        int iter;
        for (iter = 0; iter < seeds.size(); iter++) {
            Long seed = seeds.get(iter);

            r = new Random(seed + iter);

            printLoadingBar(1.0 * iter / seeds.size(), LOADING_BAR_SIZE);

            // Generate particles

             particles = ContractileParticlesGenerator.generateRandomParticles(
                r,
                config.getNumber_of_particles(),
                config.getRoom_height(),
                config.getRoom_width(),
                config.getMin_radius(),
                config.getMax_radius()
            );

            final double dt = config.getMin_radius() / (2 * Math.max(config.getMax_desired_velocity(), config.getEscape_velocity()));

            final CPMEscapeRoomSimulation simulation = new CPMEscapeRoomSimulation()
                .withRandom             (r)
                .withDt                 (dt)
                .withSaveFactor         (config.getSave_factor())
                .withMaxTime            (config.getMax_time())
                .withMinRadius          (config.getMin_radius())
                .withMaxRadius          (config.getMax_radius())
                .withEscapeSpeed        (config.getEscape_velocity())
                .withMaxDesiredSpeed    (config.getMax_desired_velocity())
                .withBeta               (config.getBeta())
                .withTau                (config.getTau())
                .withRoomHeight         (config.getRoom_height())
                .withRoomWidth          (config.getRoom_width())
                .withTargetWidth        (config.getTarget_width())
                .withOuterTargetDistance(config.getOuter_target_dist())
                .withOuterTargetWidth   (config.getOuter_target_width())
                .withParticles          (particles)
                .withStatusBarActivated (false)
                ;

            results = simulation.simulate();

            simulationsResults.add(ExtendedEscapeSimulationsResult.extendResult(results)
                .withSeed(seed)
                .withNumberOfParticles(config.getNumber_of_particles())
            );
        }

        printLoadingBar(1.0, LOADING_BAR_SIZE);

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println();
        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        new JsonWriter(POSTPROCESSING_MULTIPLE_SIMULATIONS_FILENAME)
            .withObj(simulationsResults)
            .write();

        System.out.println("Finished saving " + POSTPROCESSING_MULTIPLE_SIMULATIONS_FILENAME);
    }

}