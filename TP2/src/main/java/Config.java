// https://www.baeldung.com/jackson-yaml
// https://mkyong.com/java/jackson-how-to-parse-json/

public class Config {

    private Long seed;
    private Integer m_grid_dimension;
    private Integer n_number_of_particles;
    private Double r_interaction_radius;
    private Integer l_grid_side;
    private Integer frames;
    private Double speed;
    private Double noise_amplitude;

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public Integer getM_grid_dimension() {
        return m_grid_dimension;
    }

    public Integer getN_number_of_particles() {
        return n_number_of_particles;
    }

    public Double getR_interaction_radius() {
        return r_interaction_radius;
    }

    public Integer getL_grid_side() {
        return l_grid_side;
    }


    public void setM_grid_dimension(Integer m_grid_dimension) {
        this.m_grid_dimension = m_grid_dimension;
    }

    public void setN_number_of_particles(Integer n_number_of_particles) {
        this.n_number_of_particles = n_number_of_particles;
    }

    public void setR_interaction_radius(Double r_interaction_radius) {
        this.r_interaction_radius = r_interaction_radius;
    }

    public void setL_grid_side(Integer l_grid_side) {
        this.l_grid_side = l_grid_side;
    }

    public Integer getFrames() {
        return frames;
    }

    public void setFrames(Integer frames) {
        this.frames = frames;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getNoise_amplitude() {
        return noise_amplitude;
    }

    public void setNoise_amplitude(Double noise_amplitude) {
        this.noise_amplitude = noise_amplitude;
    }

    @Override
    public String toString() {
        return "Config{" +
                "seed=" + seed +
                ", m_grid_dimension=" + m_grid_dimension +
                ", n_number_of_particles=" + n_number_of_particles +
                ", r_interaction_radius=" + r_interaction_radius +
                ", l_grid_side=" + l_grid_side +
                ", frames=" + frames +
                ", speed=" + speed +
                ", noise_amplitude=" + noise_amplitude +
                '}';
    }
}