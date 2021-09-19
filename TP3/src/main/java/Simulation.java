import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Simulation {

    private static final Integer EVENTS_SAVING_EACH = 25;
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
            double minSpeed, maxSpeed;

            lGridSide                                   = config.getL_grid_side();
            numberOfParticles                           = config.getN_number_of_particles();
            maxEvents                                   = config.getMax_events();
            minSpeed                                    = config.getMin_speed();
            maxSpeed                                    = config.getMax_speed();
            MultipleN multipleN                         = config.getMultiple_n();
            MultipleTemperatures multipleTemperatures   = config.getMultiple_temperatures();
            MultipleSimulations multipleSimulations     = config.getMultiple_simulations();

            boolean simpleSimulation                    = true;

            if (multipleN.isActivated()) {
                simulateWithMultipleN(minSpeed, maxSpeed, multipleN.getValues(), lGridSide, maxEvents, seed);
                simpleSimulation = false;
            }

            if (multipleTemperatures.isActivated()) {
                simulateWithMultipleTemperatures(multipleTemperatures.getSpeeds_ranges(), numberOfParticles, lGridSide, maxEvents, seed);
                simpleSimulation = false;
            }

            if (multipleSimulations.isActivated()) {
                simulatewithMultipleSimulations(multipleSimulations.getSeeds(), minSpeed, maxSpeed, numberOfParticles, lGridSide, maxEvents);
                simpleSimulation = false;
            }

            if (!simpleSimulation) return;

            List<VelocityParticle> particles = new ArrayList<>();

            // Generate big particle
            VelocityParticle bigParticle = new VelocityParticle(ParticleType.BIG, 0, lGridSide/2.0, lGridSide/2.0, 0.7, 0.0, 0.0, 2.0);
            particles.add(bigParticle);

            // Generate small particles
            particles = VelocityParticlesGenerator.generateRandomWaterParticles(particles, numberOfParticles, lGridSide, 0.2, minSpeed, maxSpeed, r, 0.9); // TODO speed entre -2 y 2

            long startTime = System.nanoTime();

            List<ExtendedEvent> events = Brownian.simulate(particles, bigParticle, lGridSide, maxEvents, true);

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;

            System.out.println("Time in ms: " + timeElapsed / 1000000.0);

            // Save results

            // Ovito
            new XYZ_Writer(FILENAME).addAllFrames(events).writeAndClose();

            System.out.println("Finished saving " + FILENAME + ".exyz");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void simulateWithMultipleN(double minSpeed, double maxSpeed, List<Long> values, long gridSide, long maxEvents, long seed) throws IOException {
        List<MultipleNResult<Double>> timesResults = new ArrayList<>();
        List<MultipleNResult<ExtendedEvent>> multipleNResults = new ArrayList<>();

        Random r;
        List<VelocityParticle> particles;
        List<ExtendedEvent> res, filteredRes;

        long startTime = System.nanoTime();

        for (Long numberOfParticles: values) {
            System.out.printf("%d particles\n", numberOfParticles);

            r = new Random(seed);

            particles = new ArrayList<>();

            // Generate big particle
            VelocityParticle bigParticle = new VelocityParticle(ParticleType.BIG, 0, gridSide/2.0, gridSide/2.0, 0.7, 0.0, 0.0, 2.0);
            particles.add(bigParticle);

            // Generate small particles
            particles = VelocityParticlesGenerator.generateRandomWaterParticles(particles, numberOfParticles, gridSide, 0.2, minSpeed, maxSpeed, r, 0.9); // TODO speed entre -2 y 2

            res = Brownian.simulate(particles, bigParticle, gridSide, maxEvents, true);

            MultipleNResult<Double> times = new MultipleNResult<>(
                numberOfParticles,
                res.stream()
                    .map(extendedEvent -> extendedEvent.getEvent().getTime())
                    .collect(Collectors.toList())
            );
            timesResults.add(times);

            filteredRes = new ArrayList<>();

            for (int i = 0; i < res.size() - 1; i += EVENTS_SAVING_EACH) {
                ExtendedEvent extendedEvent = res.get(i);
                filteredRes.add(extendedEvent);
            }

            filteredRes.add(res.get(res.size()-1));

            multipleNResults.add(new MultipleNResult<>(numberOfParticles, filteredRes));
        }

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        new JsonWriter(POSTPROCESSING_FILENAME + "_multiple_n_event_times")
            .withObj(timesResults)
            .write();

        System.out.println("Finished saving " + POSTPROCESSING_FILENAME + "_multiple_n_event_times.json");

        new JsonWriter(POSTPROCESSING_FILENAME + "_multiple_n")
            .withObj(multipleNResults)
            .write();

        System.out.println("Finished saving " + POSTPROCESSING_FILENAME + "_multiple_n.json");
    }

    static void simulateWithMultipleTemperatures(List<List<Double>> speedRanges, long numberOfParticles, long gridSide, long maxEvents, long seed) throws IOException {
        List<MultipleTemperaturesResult> multipleTemperaturesResults = new ArrayList<>();

        Random r;
        List<VelocityParticle> particles;
        List<ExtendedEvent> res, filteredRes;

        double minSpeed, maxSpeed;

        long startTime = System.nanoTime();

        for (List<Double> speedRange: speedRanges) {
            minSpeed = speedRange.get(0);
            maxSpeed = speedRange.get(1);

            System.out.printf("Simulation with speed between %.2g and %.2g\n", minSpeed, maxSpeed);

            r = new Random(seed);

            particles = new ArrayList<>();

            // Generate big particle
            VelocityParticle bigParticle = new VelocityParticle(ParticleType.BIG, 0, gridSide/2.0, gridSide/2.0, 0.7, 0.0, 0.0, 2.0);
            particles.add(bigParticle);

            // Generate small particles
            particles = VelocityParticlesGenerator.generateRandomWaterParticles(particles, numberOfParticles, gridSide, 0.2, minSpeed, maxSpeed, r, 0.9); // TODO speed entre -2 y 2

            res = Brownian.simulate(particles, bigParticle, gridSide, maxEvents, true);

            filteredRes = new ArrayList<>();

            for (int i = 0; i < res.size() - 1; i += EVENTS_SAVING_EACH) {
                ExtendedEvent extendedEvent = res.get(i);
                filteredRes.add(extendedEvent);
            }

            filteredRes.add(res.get(res.size()-1));

            multipleTemperaturesResults.add(new MultipleTemperaturesResult(
                numberOfParticles,
                minSpeed,
                maxSpeed,
                filteredRes
            ));
        }

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        new JsonWriter(POSTPROCESSING_FILENAME + "_multiple_temperatures")
            .withObj(multipleTemperaturesResults)
            .write();

        System.out.println("Finished saving " + POSTPROCESSING_FILENAME + "_multiple_temperatures.json");
    }

    private static final Integer LOADING_BAR_SIZE = 20;

    public static void printLoadingBar(double percentage) {
        StringBuilder loadingBar = new StringBuilder("[");
        for (int h = 0; h < LOADING_BAR_SIZE; h++) {
            if (h < Math.ceil(percentage * LOADING_BAR_SIZE) - 1)
                loadingBar.append("=");
            else if (h < Math.ceil(percentage * LOADING_BAR_SIZE))
                loadingBar.append(">");
            else
                loadingBar.append(" ");
        }
        loadingBar.append("]");
        System.out.printf("%s %d%% Completed\r", loadingBar, (int) (percentage * 100));
    }

    static void simulatewithMultipleSimulations(List<Long> seeds, double minSpeed, double maxSpeed, long numberOfParticles, long gridSide, long maxEvents) throws IOException {
        List<List<ExtendedEvent>> simulationsResults = new ArrayList<>();

        Random r;
        List<VelocityParticle> particles;
        List<ExtendedEvent> res, filteredRes;

        long startTime = System.nanoTime();

        for (int iter = 0; iter < seeds.size(); iter++) {
            Long seed = seeds.get(iter);

            r = new Random(seed + iter);

            printLoadingBar(1.0 * iter / seeds.size());

            particles = new ArrayList<>();

            // Generate big particle
            VelocityParticle bigParticle = new VelocityParticle(ParticleType.BIG, 0, gridSide/2.0, gridSide/2.0, 0.7, 0.0, 0.0, 2.0);
            particles.add(bigParticle);

            // Generate small particles
            particles = VelocityParticlesGenerator.generateRandomWaterParticles(particles, numberOfParticles, gridSide, 0.2, minSpeed, maxSpeed, r, 0.9); // TODO speed entre -2 y 2

            res = Brownian.simulate(particles, bigParticle, gridSide, maxEvents, false);

            filteredRes = new ArrayList<>();

            for (int i = 0; i < res.size() - 1; i += EVENTS_SAVING_EACH) {
                ExtendedEvent extendedEvent = res.get(i);
                filteredRes.add(extendedEvent);
            }

            filteredRes.add(res.get(res.size()-1));

            simulationsResults.add(filteredRes);
        }

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        new JsonWriter(POSTPROCESSING_FILENAME + "_multiple_simulations")
                .withObj(simulationsResults)
                .write();

        System.out.println("Finished saving " + POSTPROCESSING_FILENAME + "_multiple_simulations.json");
    }

}