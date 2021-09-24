package ar.edu.itba.ss;// https://www.baeldung.com/jackson-yaml
// https://mkyong.com/java/jackson-how-to-parse-json/

public class Config {

    private Long    seed;
    private Long    n_number_of_particles;
    private Long    l_grid_side;
    private Double  dt;
    private Long    dt2_factor;

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

    public Double getDt() {
        return dt;
    }

    public void setDt(Double dt) {
        this.dt = dt;
    }

    public Long getDt2_factor() {
        return dt2_factor;
    }

    public void setDt2_factor(Long dt2_factor) {
        this.dt2_factor = dt2_factor;
    }

    @Override
    public String toString() {
        return "ar.edu.itba.ss.ar.edu.itba.ss.ar.edu.itba.ss.Config{" +
                "seed=" + seed +
                ", n_number_of_particles=" + n_number_of_particles +
                ", l_grid_side=" + l_grid_side +
                ", dt=" + dt +
                ", dt2_factor=" + dt2_factor +
                '}';
    }
}