package ar.edu.itba.ss;

import ar.edu.itba.ss.config.Config;
import ar.edu.itba.ss.dto.ContractileParticleDto;
import ar.edu.itba.ss.models.ContractileParticle;
import ar.edu.itba.ss.models.Frame;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Runner {

    private static final String    CONFIG_PATH         = "TP5/src/main/resources/config/config.json";
    private static final String    JSON_WRITER_PATH    = "TP5/src/main/resources/postprocessing";
    private static final String    XYZ_WRITER_PATH     = "TP5/src/main/resources/ovito";

    private static final String    OVITO_FILENAME      = "SdS_TP5_2021Q2G01_output";

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
            config.getMax_radius(),
            config.getEscape_velocity(),
            config.getMax_desired_velocity(),
            config.getBeta(),
            config.getTau()
        );

        // Simulate

        final double dt = config.getMin_radius() / (2 * Math.max(config.getMax_desired_velocity(), config.getEscape_velocity()));

        final EscapeRoomSimulation simulation = new EscapeRoomSimulation()
            .withRandom             (r)
            .withDt                 (dt)
            .withSaveFactor         (config.getSave_factor())
            .withMaxTime            (config.getMax_time())
            .withStatusBarActivated (config.getLoading_bar())
            .withRoomHeight         (config.getRoom_height())
            .withRoomWidth          (config.getRoom_width())
            .withTargetWidth        (config.getTarget_width())
            .withOuterTargetDistance(config.getOuter_target_dist())
            .withOuterTargetWidth   (config.getOuter_target_width())
            .withParticles          (particles)
            ;

        final EscapeRoomSimulation.Results results = simulation.simulate();

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

//        final List<Frame<ContractileParticleDto>> minimalResults = results.getFrames().stream()
//            .map(f -> new Frame<ContractileParticleDto>()
//                .withParticles(f.getParticles().stream().map(ContractileParticleDto::new).collect(Collectors.toList()))
//                .withTime     (f.getTime())
//            )
//            .collect(Collectors.toList());
//            ;
    }

    private static final int LOADING_BAR_SIZE = 20;


}