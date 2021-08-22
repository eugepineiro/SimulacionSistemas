import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Simulation {
    // carga config, llama al CIM, devuelve list_vecinos
    
    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();
        // carga config
        try {
            // JSON file to Java object
            Config config = mapper.readValue(new File("src/main/resources/config/config.json"), Config.class);

//            // pretty print
//            String prettyConfig = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
//            System.out.println(prettyConfig);

            if (1.0 * config.getL_grid_side() / config.getM_grid_dimension() <= (config.getR_interaction_radius() + 2.0 * config.getL_grid_side() / 50) ) {
                throw new IllegalArgumentException("L/M > rc + 2 * max_particle_radius (L/50.0)");
            }

            // Configure random
            Random r;
            Long seed = config.getSeed();
            if (seed == null)
                r = new Random();
            else
                r = new Random(seed);

            if(config.getCompare_with_brute_force().getCompare()){
                compareCimWithBruteForce(config, r);
            }

            if (config.getRandom_generator()) {
                config.setParticles(ParticlesGenerator.generateRandom(config.getN_number_of_particles(), config.getL_grid_side(), null, r));
            }
            else {
                config.setN_number_of_particles(config.getParticles().size());
                List<Particle> particles = config.getParticles();
                for (int i = 0; i < config.getN_number_of_particles(); i++){
                    particles.get(i).setId(i);
                }
            }

            List<Particle>[][] matrix = Grid.build(config.getParticles(), config.getM_grid_dimension(), config.getL_grid_side());
            Map<Particle, List<Particle>> neighbours;



            long startTime = System.nanoTime();

            if(config.getBrute_force()) {
                neighbours = BruteForceMethod.search(config.getParticles(), config.getR_interaction_radius(), config.getL_grid_side(), config.getPeriodic_return());
            } else {
                neighbours = CellIndexMethod.search(matrix, config.getR_interaction_radius(), config.getM_grid_dimension(), config.getL_grid_side(), config.getPeriodic_return());
            }

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;

            //System.out.println("Time in ms: " + timeElapsed / 1000000.0 );

            List<Map<String, Object>> to_ret = neighbours.entrySet().stream().map(entry -> {
                Map<String, Object> obj = new HashMap<>();
                obj.put("id", entry.getKey().getId());
                obj.put("x", entry.getKey().getX());
                obj.put("y", entry.getKey().getY());
                obj.put("radius", entry.getKey().getRadius());
                obj.put("neighbours", entry.getValue());
                return obj;
            }).sorted(Comparator.comparingLong(a -> (Long) a.get("id"))).collect(Collectors.toList());

            Gson gson = new Gson();
            String json = gson.toJson(to_ret);
//            System.out.println(json);
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/result.json"));
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compareCimWithBruteForce(Config config, Random r) throws FileNotFoundException {

        List<Particle>[][] matrix;

        System.setOut(new PrintStream(new FileOutputStream("src/main/resources/compare.json", true)));
        System.out.println("[");

        boolean started = false;
        final int iterationsToAverage = config.getCompare_with_brute_force().getIterations_to_average();
        long elapsedAverageCim, elapsedAverageBf;

        for( int n = 1; n < config.getCompare_with_brute_force().getMax_N(); n+=1) {

            config.setParticles(ParticlesGenerator.generateRandom(n, config.getL_grid_side(), null, r));

            for (int m =1; m< config.getCompare_with_brute_force().getMax_M(); m+=1) {
                /*if(!(1.0 * config.getL_grid_side() / m <= (config.getR_interaction_radius() + 2.0 * config.getL_grid_side() / 50)))
                    continue; */
                if(1.0 * config.getL_grid_side() / m <= (config.getR_interaction_radius() + 0.5))
                    continue;

                elapsedAverageCim = 0;
                elapsedAverageBf = 0;

                for (int l=0; l< iterationsToAverage; l++) {
                    matrix = Grid.build(config.getParticles(), m, config.getL_grid_side());

                    // RUN BRUTE FORCE
                    long startTime = System.nanoTime();
                    BruteForceMethod.search(config.getParticles(), config.getR_interaction_radius(), config.getL_grid_side(), config.getPeriodic_return());
                    long endTime = System.nanoTime();
                    elapsedAverageBf += endTime - startTime;

                    // RUN CIM
                    startTime = System.nanoTime();
                    CellIndexMethod.search(matrix, config.getR_interaction_radius(), m, config.getL_grid_side(), config.getPeriodic_return());
                    endTime = System.nanoTime();
                    elapsedAverageCim += endTime - startTime;
                }

                elapsedAverageCim /= iterationsToAverage;
                elapsedAverageBf /= iterationsToAverage;

                if (started) {
                    System.out.print(",\n");
                }
                if (!started) started = true;
                System.out.printf("{\"N\":%d, \"M\":%d, \"time_bf\":%f, \"time_cim\":%f}", n, m, elapsedAverageBf / 1000000.0, elapsedAverageCim / 1000000.0);
            }
        }
        System.out.println("]");
    }
}