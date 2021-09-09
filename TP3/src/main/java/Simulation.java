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

//            // pretty print
//            String prettyConfig = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
//            System.out.println(prettyConfig);

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

            List<VelocityParticle> particles = new ArrayList<>();
            // Generate big particle
            particles.add(new VelocityParticle(0, l_grid_side/2.0, l_grid_side/2.0, 0.7, 0.0, 0.0, 2.0));

            // Generate small particles
            particles = VelocityParticlesGenerator.generateRandom(particles, numberOfParticles, l_grid_side, 0.2, 2.0, r, 0.9);

            particles.forEach(System.out::println);

            long startTime = System.nanoTime();

            Queue<Event> events = Brownian.simulate(particles, l_grid_side, r);

//            frames = OffLattice.simulate(particles, config.getR_interaction_radius(), config.getM_grid_dimension(), l_grid_side ,config.getNoise_amplitude(),  config.getFrames(), r);

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;

            System.out.println("Time in ms: " + timeElapsed / 1000000.0 );

            // save results
//            new XYZ_Writer(FILENAME).addAllFrames(frames).writeAndClose();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}