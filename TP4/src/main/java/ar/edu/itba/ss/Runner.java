package ar.edu.itba.ss;

import ar.edu.itba.ss.config.Config;
import ar.edu.itba.ss.dto.IntegrationResults;
import ar.edu.itba.ss.integrations.Beeman;
import ar.edu.itba.ss.integrations.Gear;
import ar.edu.itba.ss.integrations.Integration;
import ar.edu.itba.ss.integrations.VerletOriginal;
import ar.edu.itba.ss.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Runner {

    private static final String         CONFIG_PATH                                 = "TP4/src/main/resources/config/config.json";
    private static final String         JSON_WRITER_PATH                            = "TP4/src/main/resources/postprocessing";
    private static final String         XYZ_WRITER_PATH                             = "TP4/src/main/resources/ovito";
    private static final String         OVITO_OSCILLATOR_VERLET_FILENAME            = "SdS_TP4_2021Q2G01_oscillator_verlet_output";
    private static final String         OVITO_OSCILLATOR_BEEMAN_FILENAME            = "SdS_TP4_2021Q2G01_oscillator_beeman_output";
    private static final String         OVITO_MARS_FILENAME                         = "SdS_TP4_2021Q2G01_mars_output";
    private static final String         OSCILLATOR_POSTPROCESSING_FILENAME          = "SdS_TP4_2021Q2G01_oscillator_results";
    private static final String         MARS_POSTPROCESSING_FILENAME                = "SdS_TP4_2021Q2G01_mars_results";
    private static final String         MARS_POSTPROCESSING_FILENAME_MULTIPLE_DATES = "SdS_TP4_2021Q2G01_mars_results_with_multiple_dates";

    private static final LocalDateTime  DATA_START_DATE                             = LocalDateTime.of(2021, Month.SEPTEMBER, 24, 0, 0, 0);

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
            }
            else
                random = new Random(seed);

            // Integrations

            HashMap<String, Integration> integrationHashMap = new HashMap<String, Integration>() {{
                put(IntegrationType.VERLET_ORIGINAL.name().toLowerCase(),   new VerletOriginal());
                put(IntegrationType.BEEMAN.name().toLowerCase(),            new Beeman());
                put(IntegrationType.GEAR.name().toLowerCase(),              new Gear());
            }};

            // Simulation

            String system = config.getSystem();

            // Start simulation

            /////////////////////////// OSCILLATOR  ///////////////////////////

            if (system.equals(SystemType.OSCILLATOR.name().toLowerCase())) {
                runOscillatorSimulation(config, integrationHashMap);
            }

            ///////////////////////////    MARS     ///////////////////////////

            else if (system.equals(SystemType.MARS.name().toLowerCase())) {

                if(config.getMultiple_dates()){
                    runMarsSimulationWithMultipleDates(config, integrationHashMap);
                } else {
                    runMarsSimulation(config, integrationHashMap);
                }

            }

            ///////////////////////////    ERROR     ///////////////////////////

            else {
                throw new IllegalArgumentException(String.format("System type %s undefined\n", system));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final int LOADING_BAR_SIZE = 20;

    private static void runOscillatorSimulation(Config config, HashMap<String, Integration> integrationHashMap) throws IOException {
        Simulation<List<Frame>> simulation;

        List<IntegrationResults> results = new ArrayList<>();

        long startTime = System.nanoTime();

        long totalIntegrations = integrationHashMap.keySet().size();
        long integrationNumber = 0;

        for (Map.Entry<String, Integration> entry : integrationHashMap.entrySet()) {

            if (config.getLoading_bar()) Utils.printLoadingBar(1.0*integrationNumber/totalIntegrations, LOADING_BAR_SIZE);

            simulation = new OscillatorSimulation()
                .withIntegration(entry.getValue())
                .withDt(config.getDt())
                .withSaveFactor(config.getSave_factor())
                .withMaxTime(config.getMax_time())            // seconds
                .withStatusBarActivated(false)
            ;

            results.add(new IntegrationResults()
                .withIntegration(entry.getKey())
                .withResults(simulation.simulate())
            );

            integrationNumber++;
        }

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        new JsonWriter(OSCILLATOR_POSTPROCESSING_FILENAME)
            .withObj(results)
            .write();

        System.out.println("Finished saving " + OSCILLATOR_POSTPROCESSING_FILENAME + ".json");

        new XYZ_Writer(OVITO_OSCILLATOR_VERLET_FILENAME)
            .addAllFrames(results.stream()
                .filter(r -> r.getIntegration().equals(IntegrationType.VERLET_ORIGINAL.name().toLowerCase()))
                .map(IntegrationResults::getResults)
                .findAny()
                .orElse(new ArrayList<>())
            )
            .writeAndClose();

        System.out.println("Finished saving " + OVITO_OSCILLATOR_VERLET_FILENAME + ".exyz");

        new XYZ_Writer(OVITO_OSCILLATOR_BEEMAN_FILENAME)
            .addAllFrames(results.stream()
                .filter(r -> r.getIntegration().equals(IntegrationType.BEEMAN.name().toLowerCase()))
                .map(IntegrationResults::getResults)
                .findAny()
                .orElse(new ArrayList<>())
            )
            .writeAndClose();

        System.out.println("Finished saving " + OVITO_OSCILLATOR_BEEMAN_FILENAME + ".exyz");
    }

    private static void runMarsSimulation(Config config, HashMap<String, Integration> integrationHashMap) throws IOException {

        LocalDateTime launchDate = config.getLaunch_date();

        MarsSimulation simulation = new MarsSimulation()
            .withIntegration(integrationHashMap.get(config.getIntegration()))
            .withDt(config.getDt())
            .withSaveFactor(config.getSave_factor())
            .withStatusBarActivated(false)
            ;

        // Simulate until date
        long secondsUntilLaunch = DATA_START_DATE.until(launchDate, ChronoUnit.SECONDS);
        simulation.setMaxTime(secondsUntilLaunch);
        simulation.setSpaceshipPresent(false);
        List<Frame> simulated = simulation.simulate();
        Frame lastFrame = simulated.get(simulated.size()-1);
        AcceleratedParticle earth = lastFrame.getParticles().stream().filter(p -> p.getType() == ParticleType.EARTH).findAny().orElse(null);
        AcceleratedParticle sun = lastFrame.getParticles().stream().filter(p -> p.getType() == ParticleType.SUN).findAny().orElse(null);
        AcceleratedParticle mars = lastFrame.getParticles().stream().filter(p -> p.getType() == ParticleType.MARS).findAny().orElse(null);

        simulation = new MarsSimulation()
            .withIntegration(integrationHashMap.get(config.getIntegration()))
            .withDt(config.getDt())
            .withSaveFactor(config.getSave_factor())
            .withStatusBarActivated(false)
        ;

        simulation.setEarth(earth);
        simulation.setMars(mars);
        simulation.setSun(sun);
        simulation.setSpaceshipPresent(true);
        simulation.setMaxTime(config.getMax_time());

        long startTime = System.nanoTime();

        List<Frame> results = simulation.simulate();

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        new JsonWriter(MARS_POSTPROCESSING_FILENAME)
            .withObj(results)
            .write();

        System.out.println("Finished saving " + MARS_POSTPROCESSING_FILENAME + ".json");

        new XYZ_Writer(OVITO_MARS_FILENAME)
            .addAllFrames(results)
            .writeAndClose();

//        new XYZ_Writer(OVITO_MARS_FILENAME)
//            .addAllFrames(results.stream()
//                .map(f -> f.withParticles(f.getParticles().stream()
//                    .map(p -> p.withRadius(p.getRadius()/Math.pow(10, 3)))
//                    .collect(Collectors.toList())
//                ))
//                .collect(Collectors.toList()))
//            .writeAndClose();

        System.out.println("Finished saving " + OVITO_MARS_FILENAME + ".exyz");

    }

    private static void runMarsSimulationWithMultipleDates(Config config, HashMap<String, Integration> integrationHashMap) throws IOException {
        double period = config.getMax_time();

        LocalDateTime lastDate = DATA_START_DATE.plusSeconds((long) period);
        System.out.println(lastDate);

        JsonWriter jsonWriter = new JsonWriter(MARS_POSTPROCESSING_FILENAME_MULTIPLE_DATES);

        Map<LocalDateTime, List<Frame>> results = new TreeMap<>();

        MarsSimulation simulation;

        long count, secondsUntilLaunch;
        LocalDateTime date;


        // A partir de launchaDate voy simulando cada un día hasta 2 años (period)
        for(date = DATA_START_DATE, count = 0; date.isBefore(lastDate); date = date.plusDays(1), count++) {
            if (config.getLoading_bar()) Utils.printLoadingBar((1.0 * count)/(period/(3600*24)), LOADING_BAR_SIZE);

             simulation = new MarsSimulation()
                .withIntegration(integrationHashMap.get(config.getIntegration()))
                .withDt(config.getDt())
                .withSaveFactor(config.getSave_factor())
                .withStatusBarActivated(false)
                ;

            // Simulate until date
            secondsUntilLaunch = DATA_START_DATE.until(date, ChronoUnit.SECONDS);
            simulation.setMaxTime(secondsUntilLaunch);
            simulation.setSpaceshipPresent(false);
            List<Frame> simulated = simulation.simulate();
            Frame lastFrame = simulated.get(simulated.size()-1);
            AcceleratedParticle earth = lastFrame.getParticles().stream().filter(p -> p.getType() == ParticleType.EARTH).findAny().orElse(null);
            AcceleratedParticle sun = lastFrame.getParticles().stream().filter(p -> p.getType() == ParticleType.SUN).findAny().orElse(null);
            AcceleratedParticle mars = lastFrame.getParticles().stream().filter(p -> p.getType() == ParticleType.MARS).findAny().orElse(null);

            simulation = new MarsSimulation()
                .withIntegration(integrationHashMap.get(config.getIntegration()))
                .withDt(config.getDt())
                .withSaveFactor(config.getSave_factor())
                .withStatusBarActivated(false)
            ;

            simulation.setEarth(earth);
            simulation.setMars(mars);
            simulation.setSun(sun);
            simulation.setSpaceshipPresent(true);
            simulation.setMaxTime(period);

            results.put(date, simulation.simulate());
        }
        if (config.getLoading_bar()) Utils.printLoadingBar((1.0 * count)/(period/(3600*24)), LOADING_BAR_SIZE);

        jsonWriter.setObject(results);
        jsonWriter.write();


    }

    private Date addSeconds(Date date, Integer seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

}