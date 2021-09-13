import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

            lGridSide                                   = config.getL_grid_side();
            numberOfParticles                           = config.getN_number_of_particles();
            maxEvents                                   = config.getMax_events();
            MultipleN multipleN                         = config.getMultiple_n();
            MultipleTemperatures multipleTemperatures   = config.getMultiple_temperatures();

            if (multipleN.isActivated()) simulateWithMultipleN(multipleN.getValues(), lGridSide, maxEvents, seed);

            if (multipleTemperatures.isActivated()) simulateWithMultipleTemperatures(multipleTemperatures.getSpeeds_ranges(), numberOfParticles, lGridSide, maxEvents, seed);

            if (multipleN.isActivated() || multipleTemperatures.isActivated()) return;

            List<VelocityParticle> particles = new ArrayList<>();

            // Generate big particle
            VelocityParticle bigParticle = new VelocityParticle(ParticleType.BIG, 0, lGridSide/2.0, lGridSide/2.0, 0.7, 0.0, 0.0, 2.0);
            particles.add(bigParticle);

            // Generate small particles
            particles = VelocityParticlesGenerator.generateRandomWaterParticles(particles, numberOfParticles, lGridSide, 0.2, 0.0, 2.0, r, 0.9); // TODO speed entre -2 y 2

            long startTime = System.nanoTime();

            List<ExtendedEvent> events = Brownian.simulate(particles, bigParticle, lGridSide, maxEvents);

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;

            System.out.println("Time in ms: " + timeElapsed / 1000000.0);

            // Save results

            // 1. Ovito
            new XYZ_Writer(FILENAME).addAllFrames(events).writeAndClose();

            // 2. Postprocessing
//            new JsonWriter(POSTPROCESSING_FILENAME)
//                .withObj(events)
//                .write();

            System.out.println("Finished saving");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void simulateWithMultipleN(List<Long> values, long gridSide, long maxEvents, long seed) throws IOException {
        List<MultipleNResult<Double>> timesResults = new ArrayList<>();
        List<MultipleNResult<List<Double>>> smallParticlesSpeedsResults = new ArrayList<>();

        Random r;
        List<VelocityParticle> particles;
        List<ExtendedEvent> res;

        long startTime = System.nanoTime();

        for (Long numberOfParticles: values) {
            System.out.printf("%d particles\n", numberOfParticles);

            r = new Random(seed);

            particles = new ArrayList<>();

            // Generate big particle
            VelocityParticle bigParticle = new VelocityParticle(ParticleType.BIG, 0, gridSide/2.0, gridSide/2.0, 0.7, 0.0, 0.0, 2.0);
            particles.add(bigParticle);

            // Generate small particles
            particles = VelocityParticlesGenerator.generateRandomWaterParticles(particles, numberOfParticles, gridSide, 0.2, 0.0, 2.0, r, 0.9); // TODO speed entre -2 y 2

            res = Brownian.simulate(particles, bigParticle, gridSide, maxEvents);

            MultipleNResult<Double> times = new MultipleNResult<>(
                numberOfParticles,
                res.stream()
                    .map(extendedEvent -> extendedEvent.getEvent().getTime())
                    .collect(Collectors.toList())
            );
            timesResults.add(times);

            MultipleNResult<List<Double>> smallParticlesSpeeds = new MultipleNResult<>(
                numberOfParticles,
                res.stream()
                    .map(extendedEvent -> extendedEvent.getFrame().stream()
                        .filter(velocityParticle -> velocityParticle.getType().equals(ParticleType.SMALL))
                        .map(VelocityParticle::getSpeed)
                        .collect(Collectors.toList())
                    )
                    .collect(Collectors.toList())
            );
            smallParticlesSpeedsResults.add(smallParticlesSpeeds);
        }

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        new JsonWriter(POSTPROCESSING_FILENAME + "_multiple_n_event_times")
            .withObj(timesResults)
            .write();

        new JsonWriter(POSTPROCESSING_FILENAME + "_multiple_n_small_particles_speeds")
            .withObj(smallParticlesSpeedsResults)
            .write();

        System.out.println("Finished saving");
    }

    static void simulateWithMultipleTemperatures(List<List<Double>> speedRanges, long numberOfParticles, long gridSide, long maxEvents, long seed) throws IOException {
        List<MultipleTemperaturesResult> positionsResults = new ArrayList<>();

        Random r;
        List<VelocityParticle> particles;
        List<ExtendedEvent> res;

        double minSpeed, maxSpeed;

        long startTime = System.nanoTime();

        for (List<Double> speedRange: speedRanges) {
            minSpeed = speedRange.get(0);
            maxSpeed = speedRange.get(1);

            System.out.printf("Speeds between %.2g and %.2g\n", minSpeed, maxSpeed);

            r = new Random(seed);

            particles = new ArrayList<>();

            // Generate big particle
            VelocityParticle bigParticle = new VelocityParticle(ParticleType.BIG, 0, gridSide/2.0, gridSide/2.0, 0.7, 0.0, 0.0, 2.0);
            particles.add(bigParticle);

            // Generate small particles
            particles = VelocityParticlesGenerator.generateRandomWaterParticles(particles, numberOfParticles, gridSide, 0.2, minSpeed, maxSpeed, r, 0.9); // TODO speed entre -2 y 2

            res = Brownian.simulate(particles, bigParticle, gridSide, maxEvents);

            MultipleTemperaturesResult positionsResult = new MultipleTemperaturesResult(
                numberOfParticles,
                minSpeed,
                maxSpeed,
                res.stream()
                    .map(extendedEvent -> extendedEvent.getFrame().stream()
                        .filter(particle -> particle.getType() == ParticleType.BIG)
                        .findFirst().orElse(null))
                        .filter(Objects::nonNull)
                        .map(velocityParticle -> new MultipleTemperaturesResult.Position(velocityParticle.getX(), velocityParticle.getY()))
                        .collect(Collectors.toList())
            );

            positionsResults.add(positionsResult);
        }

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        new JsonWriter(POSTPROCESSING_FILENAME + "_multiple_temperatures_big_particle_positions")
                .withObj(positionsResults)
                .write();

        System.out.println("Finished saving");
    }

}