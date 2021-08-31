import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OffLatticeSimulation {

    private static final String FILENAME = "SdS_TP2_2021Q2G01_output";
    private static final String POSTPROCESSING_FILENAME = "SdS_TP2_2021Q2G01_results";
    private static final Integer LOADING_BAR_SIZE = 20;

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        long curr;
        try {
            // JSON file to Java object
            Config config = mapper.readValue(new File("TP2/src/main/resources/config/config.json"), Config.class);

//            // pretty print
//            String prettyConfig = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
//            System.out.println(prettyConfig);

            if(config.getPolarization().getActivated()){
                comparePolarizations(config);
                return;
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


            int numberOfParticles;
            int l_grid_side;
            if(config.getN_number_of_particles() == null){
                l_grid_side = config.getL_grid_side();
                numberOfParticles = (int) (config.getDensity() * Math.pow(config.getL_grid_side(), 2));
            }else if( config.getL_grid_side() == null ){
                numberOfParticles = config.getN_number_of_particles();
                l_grid_side = (int) Math.sqrt(config.getN_number_of_particles()/config.getDensity());
            } else {
                numberOfParticles = config.getN_number_of_particles();
                l_grid_side = config.getL_grid_side();
            }

            if (1.0 * l_grid_side / config.getM_grid_dimension() <= (config.getR_interaction_radius() /*+ 2.0 * l_grid_side / 50)*/) ) {
                throw new IllegalArgumentException("L/M > rc");
            }

            System.out.println(" l " + l_grid_side);

            List<VelocityParticle> particles = VelocityParticlesGenerator.generateRandom(numberOfParticles, l_grid_side, 0., config.getSpeed(), r);

            List<List<VelocityParticle>> frames;

            long startTime = System.nanoTime();

            frames = OffLattice.simulate(particles, config.getR_interaction_radius(), config.getM_grid_dimension(), l_grid_side ,config.getNoise_amplitude(),  config.getFrames(), r);

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;

            System.out.println("Time in ms: " + timeElapsed / 1000000.0 );

            // save results
            new XYZ_Writer(FILENAME).addAllFrames(frames).writeAndClose();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void comparePolarizations(Config config) throws IOException {

        // Configure random
        Long seed = config.getSeed();
        if (seed == 0) {
            seed = System.currentTimeMillis();
        }
        Random r; // = new Random(seed);

        int min_n, max_n, n_increase;
        double min_density, max_density, density_increase, min_noise, max_noise, noise_increase;

        min_n = config.getPolarization().getNumber_of_particles_range()[0];
        max_n = config.getPolarization().getNumber_of_particles_range()[1];
        n_increase = config.getPolarization().getNumber_of_particles_increase();

        min_density = config.getPolarization().getDensity_range()[0];
        max_density = config.getPolarization().getDensity_range()[1];
        density_increase = config.getPolarization().getDensity_increase();

        min_noise = config.getPolarization().getNoise_range()[0];
        max_noise = config.getPolarization().getNoise_range()[1];
        noise_increase = config.getPolarization().getNoise_increase();

        int numberOfSimulations = config.getPolarization().getNumber_of_simulations();
        List<List<Double>> simulationsPolarizationsList;

        List<VelocityParticle> particles;
        List<List<VelocityParticle>> frames;
        List<Double> framedPolarizations;
        List<PostProcessing> postProcessingList = new ArrayList<>();
        int L;
        int M = config.getM_grid_dimension();

        int it = 0, totalIterations = ((int) Math.ceil(1.0 * (max_n - min_n)/n_increase)) * ((int) Math.ceil((max_density - min_density)/density_increase)) * ((int) Math.ceil((max_noise - min_noise)/noise_increase)) * numberOfSimulations;

        System.out.println("Running simulations");

        long startTime = System.nanoTime();

        for(int n = min_n; n < max_n; n += n_increase) {
            for(double density = min_density; density < max_density; density += density_increase) {
                for(double noise = min_noise; noise < max_noise; noise += noise_increase) {

                    simulationsPolarizationsList = new ArrayList<>();

                    L = (int) Math.sqrt(n/density);
                    r = new Random(seed);
                    for(int simulations = 0; simulations < numberOfSimulations; simulations++) {

                        // Count iterations
                        it++;
                        printLoadingBar(1.0 * it / totalIterations);

                        if (1.0 * L / M <= config.getR_interaction_radius() ) {
                            M = (int) ((1.0 * L / config.getR_interaction_radius()) - 0.1);
                        }

                        // Sets seed
//

                        particles = VelocityParticlesGenerator.generateRandom(n, L, 0., config.getSpeed(), r);
                        frames = OffLattice.simulate(particles, config.getR_interaction_radius(), M, L, noise, config.getFrames(), r);

                        // Calculate polarization
                        framedPolarizations = calculatePolarization(frames, config.getSpeed());

                        simulationsPolarizationsList.add(framedPolarizations);

                    }

                    if (simulationsPolarizationsList.size() > 0)
                        postProcessingList.add(new PostProcessing(simulationsPolarizationsList, density, n, noise));
                }
            }
        }

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Time in ms: " + timeElapsed / 1000000.0 );

        // Save results
        new JsonWriter(POSTPROCESSING_FILENAME)
                .withObj(postProcessingList)
                .write();
    }

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

    public static List<Double> calculatePolarization(List<List<VelocityParticle>> frames, double speed) {

        List<Double> framedPolarizations = new ArrayList<>();
        double vx, vy, polarization;

        if (frames.size() == 0) {
            return new ArrayList<>();
        }

        for(List<VelocityParticle> frame: frames) {
            // | sum(vx), sum(vy) | / n*getSpeed
            vx = 0;
            vy = 0;

            for(VelocityParticle p: frame) {
                vx += p.getSpeed() * Math.cos(p.getAngle());
                vy += p.getSpeed() * Math.sin(p.getAngle());
            }

            polarization = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2)) / (frame.size() * speed);
            framedPolarizations.add(polarization);
        }

        return framedPolarizations;
    }

}