package ar.edu.itba.ss;

import ar.edu.itba.ss.config.Config;
import ar.edu.itba.ss.config.MultipleDates;
import ar.edu.itba.ss.config.MultipleDt;
import ar.edu.itba.ss.config.MultipleVelocities;
import ar.edu.itba.ss.dto.DatedMap;
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

    private static final String         CONFIG_PATH                                     = "TP4/src/main/resources/config/config.json";
    private static final String         JSON_WRITER_PATH                                = "TP4/src/main/resources/postprocessing";
    private static final String         XYZ_WRITER_PATH                                 = "TP4/src/main/resources/ovito";
    private static final String         OVITO_OSCILLATOR_VERLET_FILENAME                = "SdS_TP4_2021Q2G01_oscillator_verlet_output";
    private static final String         OVITO_OSCILLATOR_BEEMAN_FILENAME                = "SdS_TP4_2021Q2G01_oscillator_beeman_output";
    private static final String         OVITO_MARS_FILENAME                             = "SdS_TP4_2021Q2G01_mars_output";
    private static final String         OVITO_JUPITER_FILENAME                          = "SdS_TP4_2021Q2G01_jupiter_output";
    private static final String         OSCILLATOR_POSTPROCESSING_FILENAME              = "SdS_TP4_2021Q2G01_oscillator_results";
    private static final String         OSCILLATOR_POSTPROCESSING_FILENAME_MULTIPLE_DT  = "SdS_TP4_2021Q2G01_oscillator_results_with_multiple_dt";
    private static final String         MARS_POSTPROCESSING_FILENAME                    = "SdS_TP4_2021Q2G01_mars_results";
    private static final String         MARS_POSTPROCESSING_FILENAME_MULTIPLE_DATES     = "SdS_TP4_2021Q2G01_mars_results_with_multiple_dates";
    private static final String         MARS_POSTPROCESSING_FILENAME_MULTIPLE_VEL       = "SdS_TP4_2021Q2G01_mars_results_with_multiple_velocities";
    private static final String         JUPITER_POSTPROCESSING_FILENAME                 = "SdS_TP4_2021Q2G01_jupiter_results";
    private static final String         JUPITER_POSTPROCESSING_FILENAME_MULTIPLE_DATES  = "SdS_TP4_2021Q2G01_jupiter_results_with_multiple_dates";
    private static final String         JUPITER_POSTPROCESSING_FILENAME_MULTIPLE_VEL    = "SdS_TP4_2021Q2G01_jupiter_results_with_multiple_velocities";

    private static final LocalDateTime  DATA_START_DATE                                 = LocalDateTime.of(2021, Month.SEPTEMBER, 24, 0, 0, 0);

    // Main

    // Integrations

    private static final HashMap<String, Integration> integrationHashMap = new HashMap<String, Integration>() {{
        put(IntegrationType.VERLET_ORIGINAL.name().toLowerCase(),   new VerletOriginal());
        put(IntegrationType.BEEMAN.name().toLowerCase(),            new Beeman());
        put(IntegrationType.GEAR.name().toLowerCase(),              new Gear());
    }};

    public static void main(String[] args) {
        JsonWriter.setPath(JSON_WRITER_PATH);
        XYZ_Writer.setPath(XYZ_WRITER_PATH);

        ObjectMapper mapper = new ObjectMapper();
        try {
            // JSON file to Java object
            Config config = mapper.readValue(new File(CONFIG_PATH), Config.class);

            // Simulation

            String system = config.getSystem();

            // Start simulation

            /////////////////////////// OSCILLATOR  ///////////////////////////

            if (system.equals(SystemType.OSCILLATOR.name().toLowerCase())) {

                boolean multipleRuns = false;

                if (config.getMultiple_dt().isActivated()) {
                    multipleRuns = true;
                    final MultipleDt mul = config.getMultiple_dt();
                    runOscillatorSimulationWithMultipleDt(mul.getMin_exp(), mul.getMax_exp(), mul.getIncrement(), config);
                }
                runOscillatorSimulation(config);
            }

            ///////////////////////////    MARS     ///////////////////////////

            else if (system.equals(SystemType.MARS.name().toLowerCase())) {

                boolean multipleRuns = false;

                if (config.getMultiple_velocities().isActivated()) {
                    multipleRuns = true;
                    final MultipleVelocities mul = config.getMultiple_velocities();
                    runMarsSimulationWithMultipleVelocities(mul.getMin(), mul.getMax(), mul.getIncrement(), config);
                }
                if (config.getMultiple_dates().isActivated()) {
                    multipleRuns = true;
                    final MultipleDates mul = config.getMultiple_dates();
                    runMarsSimulationWithMultipleDates(mul.getMin(), mul.getMax(), mul.getIncrement(), config);
                }
                if (!multipleRuns) {
                    runMarsSimulation(config);
                }

            }

            ///////////////////////////    JUPITER     ///////////////////////////

            else if (system.equals(SystemType.JUPITER.name().toLowerCase())) {

                boolean multipleRuns = false;

                if (config.getMultiple_velocities().isActivated()) {
                    multipleRuns = true;
                    final MultipleVelocities mul = config.getMultiple_velocities();
                    runJupiterSimulationWithMultipleVelocities(mul.getMin(), mul.getMax(), mul.getIncrement(), config);
                }
                if (config.getMultiple_dates().isActivated()) {
                    multipleRuns = true;
                    final MultipleDates mul = config.getMultiple_dates();
                    runJupiterSimulationWithMultipleDates(mul.getMin(), mul.getMax(), mul.getIncrement(), config);
                }
                if (!multipleRuns) {
                    runJupiterSimulation(config);
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

    private static void runOscillatorSimulation(Config config) throws IOException {
        Simulation<List<Frame>> simulation;

        System.out.print("Running oscillator simulation\n");

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

    private static void runOscillatorSimulationWithMultipleDt(double minExp, double maxExp, double increment, Config config) throws IOException {

        System.out.printf("Running oscillator simulation with multiple dt between exponent %.2g and %.2g with an increment of %.2g\n", minExp, maxExp, increment);

        Simulation<List<Frame>> simulation;

        long startTime = System.nanoTime();

        Map<Double, List<IntegrationResults>> results = new HashMap<>();

        long totalIntegrations = integrationHashMap.keySet().size() * (long) (((maxExp - minExp)/increment) - increment);
        long interationNumber = 0;

        double exp, dt;
        for (exp = minExp; exp < maxExp; exp += increment) {
            dt = Math.pow(10, exp);

            List<IntegrationResults> dtResults = new ArrayList<>();

            for (Map.Entry<String, Integration> entry : integrationHashMap.entrySet()) {
                if (config.getLoading_bar()) Utils.printLoadingBar(1.0*interationNumber/totalIntegrations, LOADING_BAR_SIZE);

                simulation = new OscillatorSimulation()
                    .withIntegration(entry.getValue())
                    .withDt(dt)
                    .withSaveFactor(config.getSave_factor())
                    .withMaxTime(config.getMax_time())            // seconds
                    .withStatusBarActivated(false)
                ;

                dtResults.add(new IntegrationResults()
                    .withIntegration(entry.getKey())
                    .withResults(simulation.simulate())
                );

                interationNumber++;
            }

            results.put(dt, dtResults);
        }
        if (config.getLoading_bar()) Utils.printLoadingBar(1.0*interationNumber/totalIntegrations, LOADING_BAR_SIZE);

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        new JsonWriter(OSCILLATOR_POSTPROCESSING_FILENAME_MULTIPLE_DT)
            .withObj(results)
            .write();

        System.out.println("Finished saving " + OSCILLATOR_POSTPROCESSING_FILENAME_MULTIPLE_DT + ".json");
    }

    private static void runMarsSimulation(Config config) throws IOException {

        LocalDateTime launchDate = config.getLaunch_date();

        System.out.printf("Running mars simulation on launch date %s\n", launchDate);

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

        AcceleratedParticle spaceship = results.get(0).getParticles().stream().filter(p -> p.getType().equals(ParticleType.SPACESHIP)).findAny().orElse(null);

        simulated.forEach(frame -> {
            final AcceleratedParticle earthF = frame.getParticles().stream().filter(p -> p.getType().equals(ParticleType.EARTH)).findAny().orElse(null);

            if (earthF != null && spaceship != null) {
                final AcceleratedParticle spaceshipF = spaceship.clone()
                    .withX(earthF.getX())
                    .withY(earthF.getY())
                    ;

                frame.getParticles().add(spaceshipF);
            }
        });

        new XYZ_Writer(OVITO_MARS_FILENAME)
            .addAllFrames(simulated)
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

    private static void runMarsSimulationWithMultipleDates(LocalDateTime minLaunchDate, LocalDateTime maxLaunchDate, long increment, Config config) throws IOException {
        double period = config.getMax_time();

        System.out.printf("Running mars simulation with multiple launch dates between %s and %s with an increment of %d seconds\n", minLaunchDate, maxLaunchDate, increment);

        Map<LocalDateTime, List<Frame>> results = new TreeMap<>();
        MarsSimulation simulation;

        long startTime = System.nanoTime();

        long secondsUntilLaunch;
        LocalDateTime date;

        // A partir de launchDate voy simulando cada un día hasta 2 años (period)
        for(date = minLaunchDate; date.isBefore(maxLaunchDate); date = date.plusSeconds(increment)) {
            if (config.getLoading_bar()) Utils.printLoadingBar(1.0*minLaunchDate.until(date, ChronoUnit.SECONDS)/minLaunchDate.until(maxLaunchDate, ChronoUnit.SECONDS), LOADING_BAR_SIZE);

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
        if (config.getLoading_bar()) Utils.printLoadingBar(1.0*minLaunchDate.until(date, ChronoUnit.SECONDS)/minLaunchDate.until(maxLaunchDate, ChronoUnit.SECONDS), LOADING_BAR_SIZE);

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        new JsonWriter(MARS_POSTPROCESSING_FILENAME_MULTIPLE_DATES)
            .withObj(results)
            .write();

        System.out.println("Finished saving " + MARS_POSTPROCESSING_FILENAME_MULTIPLE_DATES + ".json");
    }

    private static void runMarsSimulationWithMultipleVelocities(double minSpeed, double maxSpeed, double increment, Config config) throws IOException {

        LocalDateTime launchDate = config.getLaunch_date();

        System.out.printf("Running mars simulation with multiple velocities between %.2g and %.2g with increment %.2g on launch date %s\n", minSpeed, maxSpeed, increment, launchDate);

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

        Map<Double, List<Frame>> results = new HashMap<>();

        long startTime = System.nanoTime();

        double initialSpeed;
        for (initialSpeed = minSpeed; initialSpeed < maxSpeed; initialSpeed += increment) {
            if (config.getLoading_bar()) Utils.printLoadingBar((initialSpeed - minSpeed)/(maxSpeed - minSpeed), LOADING_BAR_SIZE);

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
            simulation.setSpaceshipInitialSpeed(initialSpeed);
            simulation.setMaxTime(config.getMax_time());

            results.put(initialSpeed, simulation.simulate());
        }
        if (config.getLoading_bar()) Utils.printLoadingBar((initialSpeed - minSpeed)/(maxSpeed - minSpeed), LOADING_BAR_SIZE);

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        new JsonWriter(MARS_POSTPROCESSING_FILENAME_MULTIPLE_VEL)
            .withObj(new DatedMap<Double, List<Frame>>()
                .withMap(results)
                .withDate(launchDate))
            .write();

        System.out.println("Finished saving " + MARS_POSTPROCESSING_FILENAME_MULTIPLE_VEL + ".json");
    }

    private static void runJupiterSimulation(Config config) throws IOException {

        LocalDateTime launchDate = config.getLaunch_date();

        System.out.printf("Running jupiter simulation on launch date %s\n", launchDate);

        JupiterSimulation simulation = new JupiterSimulation()
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

        simulation = new JupiterSimulation()
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

        new JsonWriter(JUPITER_POSTPROCESSING_FILENAME)
            .withObj(results)
            .write();

        System.out.println("Finished saving " + JUPITER_POSTPROCESSING_FILENAME + ".json");

        AcceleratedParticle spaceship = results.get(0).getParticles().stream().filter(p -> p.getType().equals(ParticleType.SPACESHIP)).findAny().orElse(null);

        simulated.forEach(frame -> {
            final AcceleratedParticle earthF = frame.getParticles().stream().filter(p -> p.getType().equals(ParticleType.EARTH)).findAny().orElse(null);

            if (earthF != null && spaceship != null) {
                final AcceleratedParticle spaceshipF = spaceship.clone()
                    .withX(earthF.getX())
                    .withY(earthF.getY())
                    ;

                frame.getParticles().add(spaceshipF);
            }
        });

        new XYZ_Writer(OVITO_JUPITER_FILENAME)
            .addAllFrames(simulated)
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

        System.out.println("Finished saving " + OVITO_JUPITER_FILENAME + ".exyz");

    }

    private static void runJupiterSimulationWithMultipleDates(LocalDateTime minLaunchDate, LocalDateTime maxLaunchDate, long increment, Config config) throws IOException {
        double period = config.getMax_time();
        LocalDateTime lastDate = DATA_START_DATE.plusSeconds((long) period);

        System.out.printf("Running jupiter simulation with multiple launch dates between %s and %s with an increment of %d seconds\n", minLaunchDate, maxLaunchDate, increment);

        Map<LocalDateTime, List<Frame>> results = new TreeMap<>();
        JupiterSimulation simulation;

        long startTime = System.nanoTime();

        long secondsUntilLaunch;
        LocalDateTime date;

        // A partir de launchDate voy simulando cada un día hasta 2 años (period)
        for(date = minLaunchDate; date.isBefore(maxLaunchDate); date = date.plusSeconds(increment)) {
            if (config.getLoading_bar()) Utils.printLoadingBar(1.0*minLaunchDate.until(date, ChronoUnit.SECONDS)/minLaunchDate.until(maxLaunchDate, ChronoUnit.SECONDS), LOADING_BAR_SIZE);

            simulation = new JupiterSimulation()
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
            AcceleratedParticle jupiter = lastFrame.getParticles().stream().filter(p -> p.getType() == ParticleType.JUPITER).findAny().orElse(null);

            simulation = new JupiterSimulation()
                    .withIntegration(integrationHashMap.get(config.getIntegration()))
                    .withDt(config.getDt())
                    .withSaveFactor(config.getSave_factor())
                    .withStatusBarActivated(false)
            ;

            simulation.setEarth(earth);
            simulation.setMars(mars);
            simulation.setSun(sun);
            simulation.setJupiter(jupiter);
            simulation.setSpaceshipPresent(true);
            simulation.setMaxTime(period);

            results.put(date, simulation.simulate());
        }
        if (config.getLoading_bar()) Utils.printLoadingBar(1.0*minLaunchDate.until(date, ChronoUnit.SECONDS)/minLaunchDate.until(maxLaunchDate, ChronoUnit.SECONDS), LOADING_BAR_SIZE);

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        new JsonWriter(JUPITER_POSTPROCESSING_FILENAME_MULTIPLE_DATES)
                .withObj(results)
                .write();

        System.out.println("Finished saving " + JUPITER_POSTPROCESSING_FILENAME_MULTIPLE_DATES + ".json");
    }

    private static void runJupiterSimulationWithMultipleVelocities(double minSpeed, double maxSpeed, double increment, Config config) throws IOException {

        LocalDateTime launchDate = config.getLaunch_date();

        System.out.printf("Running mars simulation with multiple velocities between %.2g and %.2g with increment %.2g on launch date %s\n", minSpeed, maxSpeed, increment, launchDate);

        JupiterSimulation simulation = new JupiterSimulation()
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
        AcceleratedParticle jupiter = lastFrame.getParticles().stream().filter(p -> p.getType() == ParticleType.JUPITER).findAny().orElse(null);

        Map<Double, List<Frame>> results = new HashMap<>();

        long startTime = System.nanoTime();

        double initialSpeed;
        for (initialSpeed = minSpeed; initialSpeed < maxSpeed; initialSpeed += increment) {
            if (config.getLoading_bar()) Utils.printLoadingBar((initialSpeed - minSpeed)/(maxSpeed - minSpeed), LOADING_BAR_SIZE);

            simulation = new JupiterSimulation()
                    .withIntegration(integrationHashMap.get(config.getIntegration()))
                    .withDt(config.getDt())
                    .withSaveFactor(config.getSave_factor())
                    .withStatusBarActivated(false)
            ;

            simulation.setEarth(earth);
            simulation.setMars(mars);
            simulation.setSun(sun);
            simulation.setJupiter(jupiter);
            simulation.setSpaceshipPresent(true);
            simulation.setSpaceshipInitialSpeed(initialSpeed);
            simulation.setMaxTime(config.getMax_time());

            results.put(initialSpeed, simulation.simulate());
        }
        if (config.getLoading_bar()) Utils.printLoadingBar((initialSpeed - minSpeed)/(maxSpeed - minSpeed), LOADING_BAR_SIZE);

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Time in ms: " + timeElapsed / 1000000.0);

        new JsonWriter(JUPITER_POSTPROCESSING_FILENAME_MULTIPLE_VEL)
                .withObj(new DatedMap<Double, List<Frame>>()
                        .withMap(results)
                        .withDate(launchDate))
                .write();

        System.out.println("Finished saving " + JUPITER_POSTPROCESSING_FILENAME_MULTIPLE_VEL + ".json");
    }

}