// https://www.baeldung.com/jackson-yaml
// https://mkyong.com/java/jackson-how-to-parse-json/

public class Config {

    private Long seed;
    private Long n_number_of_particles;
    private Long l_grid_side;
    private Long max_events;
    private MultipleN multiple_n;

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public Long getN_number_of_particles() {
        return n_number_of_particles;
    }

    public void setN_number_of_particles(Long n_number_of_particles) {
        this.n_number_of_particles = n_number_of_particles;
    }

    public Long getL_grid_side() {
        return l_grid_side;
    }

    public void setL_grid_side(Long l_grid_side) {
        this.l_grid_side = l_grid_side;
    }

    public Long getMax_events() {
        return max_events;
    }

    public void setMax_events(Long max_events) {
        this.max_events = max_events;
    }

    public MultipleN getMultiple_n() {
        return multiple_n;
    }

    public void setMultiple_n(MultipleN multiple_n) {
        this.multiple_n = multiple_n;
    }

    @Override
    public String toString() {
        return "Config{" +
                "seed=" + seed +
                ", n_number_of_particles=" + n_number_of_particles +
                ", l_grid_side=" + l_grid_side +
                ", max_events=" + max_events +
                ", multiple_n=" + multiple_n +
                '}';
    }
}