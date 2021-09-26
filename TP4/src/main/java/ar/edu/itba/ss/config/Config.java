package ar.edu.itba.ss.config;// https://www.baeldung.com/jackson-yaml
// https://mkyong.com/java/jackson-how-to-parse-json/

public class Config {

    private Long    seed;
    private String  system;
    private String  integration;
    private Double  dt;
    private Double  max_time;
    private Long    save_factor;
    private Boolean loading_bar;

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getIntegration() {
        return integration;
    }

    public void setIntegration(String integration) {
        this.integration = integration;
    }

    public Double getDt() {
        return dt;
    }

    public void setDt(Double dt) {
        this.dt = dt;
    }

    public Double getMax_time() {
        return max_time;
    }

    public void setMax_time(Double max_time) {
        this.max_time = max_time;
    }

    public Long getSave_factor() {
        return save_factor;
    }

    public void setSave_factor(Long dt2_factor) {
        this.save_factor = dt2_factor;
    }

    public Boolean getLoading_bar() {
        return loading_bar;
    }

    public void setLoading_bar(Boolean loading_bar) {
        this.loading_bar = loading_bar;
    }

    @Override
    public String toString() {
        return "Config{" +
            "seed=" + seed +
            ", system='" + system + '\'' +
            ", integration='" + integration + '\'' +
            ", dt=" + dt +
            ", max_time=" + max_time +
            ", save_factor=" + save_factor +
            ", loading_bar=" + loading_bar +
            '}';
    }
}