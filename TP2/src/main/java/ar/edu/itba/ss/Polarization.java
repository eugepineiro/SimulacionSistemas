package ar.edu.itba.ss;

public class Polarization {

    private Boolean activated;
    private Integer[] number_of_particles_range;
    private Integer number_of_particles_increase;
    private Double[] density_range;
    private Double density_increase;
    private Integer number_of_simulations;
    private Double[] noise_range;
    private Double noise_increase;

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Integer[] getNumber_of_particles_range() {
        return number_of_particles_range;
    }

    public void setNumber_of_particles_range(Integer[] number_of_particles_range) {
        this.number_of_particles_range = number_of_particles_range;
    }

    public Integer getNumber_of_particles_increase() {
        return number_of_particles_increase;
    }

    public void setNumber_of_particles_increase(Integer number_of_particles_increase) {
        this.number_of_particles_increase = number_of_particles_increase;
    }

    public Double[] getDensity_range() {
        return density_range;
    }

    public void setDensity_range(Double[] density_range) {
        this.density_range = density_range;
    }

    public Double getDensity_increase() {
        return density_increase;
    }

    public void setDensity_increase(Double density_increase) {
        this.density_increase = density_increase;
    }

    public Integer getNumber_of_simulations() {
        return number_of_simulations;
    }

    public void setNumber_of_simulations(Integer number_of_simulations) {
        this.number_of_simulations = number_of_simulations;
    }

    public Double[] getNoise_range() {
        return noise_range;
    }

    public void setNoise_range(Double[] noise_range) {
        this.noise_range = noise_range;
    }

    public Double getNoise_increase() {
        return noise_increase;
    }

    public void setNoise_increase(Double noise_increase) {
        this.noise_increase = noise_increase;
    }
}
