package ar.edu.itba.ss.config;// https://www.baeldung.com/jackson-yaml
// https://mkyong.com/java/jackson-how-to-parse-json/

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Config {

    private Long    seed;
    private Long    number_of_particles;
    private Double  room_height;
    private Double  room_width;
    private Double  target_width;
    private Double  outer_target_dist;
    private Double  outer_target_width;
    private Double  max_time;
    private Long    save_factor;
    private Double  min_radius;
    private Double  max_radius;
    private Double  beta;
    private Double  tau;
    private Double  escape_velocity;
    private Double  max_desired_velocity;
    private Boolean loading_bar;
    private MultipleSimulations multiple_simulations;
    private MultipleWidthAndParticles multiple_width_and_particles;

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public Long getNumber_of_particles() {
        return number_of_particles;
    }

    public void setNumber_of_particles(Long number_of_particles) {
        this.number_of_particles = number_of_particles;
    }

    public Double getRoom_height() {
        return room_height;
    }

    public void setRoom_height(Double room_height) {
        this.room_height = room_height;
    }

    public Double getRoom_width() {
        return room_width;
    }

    public void setRoom_width(Double room_width) {
        this.room_width = room_width;
    }

    public Double getTarget_width() {
        return target_width;
    }

    public void setTarget_width(Double target_width) {
        this.target_width = target_width;
    }

    public Double getOuter_target_dist() {
        return outer_target_dist;
    }

    public void setOuter_target_dist(Double outer_target_dist) {
        this.outer_target_dist = outer_target_dist;
    }

    public Double getOuter_target_width() {
        return outer_target_width;
    }

    public void setOuter_target_width(Double outer_target_width) {
        this.outer_target_width = outer_target_width;
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

    public void setSave_factor(Long save_factor) {
        this.save_factor = save_factor;
    }

    public Double getMin_radius() {
        return min_radius;
    }

    public void setMin_radius(Double min_radius) {
        this.min_radius = min_radius;
    }

    public Double getMax_radius() {
        return max_radius;
    }

    public void setMax_radius(Double max_radius) {
        this.max_radius = max_radius;
    }

    public Double getBeta() {
        return beta;
    }

    public void setBeta(Double beta) {
        this.beta = beta;
    }

    public Double getTau() {
        return tau;
    }

    public void setTau(Double tau) {
        this.tau = tau;
    }

    public Boolean getLoading_bar() {
        return loading_bar;
    }

    public void setLoading_bar(Boolean loading_bar) {
        this.loading_bar = loading_bar;
    }

    public Double getEscape_velocity() {
        return escape_velocity;
    }

    public void setEscape_velocity(Double escape_velocity) {
        this.escape_velocity = escape_velocity;
    }

    public Double getMax_desired_velocity() {
        return max_desired_velocity;
    }

    public void setMax_desired_velocity(Double max_desired_velocity) {
        this.max_desired_velocity = max_desired_velocity;
    }

    public MultipleSimulations getMultiple_simulations() {
        return multiple_simulations;
    }

    public void setMultiple_simulations(MultipleSimulations multiple_simulations) {
        this.multiple_simulations = multiple_simulations;
    }

    public MultipleWidthAndParticles getMultiple_width_and_particles() {
        return multiple_width_and_particles;
    }

    public void setMultiple_width_and_particles(MultipleWidthAndParticles multiple_width_and_particles) {
        this.multiple_width_and_particles = multiple_width_and_particles;
    }

    @Override
    public String toString() {
        return "Config{" +
            "seed=" + seed +
            ", number_of_particles=" + number_of_particles +
            ", room_height=" + room_height +
            ", room_width=" + room_width +
            ", target_width=" + target_width +
            ", outer_target_dist=" + outer_target_dist +
            ", outer_target_width=" + outer_target_width +
            ", max_time=" + max_time +
            ", save_factor=" + save_factor +
            ", min_radius=" + min_radius +
            ", max_radius=" + max_radius +
            ", beta=" + beta +
            ", tau=" + tau +
            ", escape_velocity=" + escape_velocity +
            ", max_desired_velocity=" + max_desired_velocity +
            ", loading_bar=" + loading_bar +
            ", multiple_simulations=" + multiple_simulations +
            ", multiple_width_and_particles=" + multiple_width_and_particles +
            '}';
    }
}