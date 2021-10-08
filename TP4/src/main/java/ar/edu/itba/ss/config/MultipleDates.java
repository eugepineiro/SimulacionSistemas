package ar.edu.itba.ss.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MultipleDates {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private boolean activated;
    private LocalDateTime min;
    private LocalDateTime max;
    private long increment;

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public LocalDateTime getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = LocalDateTime.parse(min, formatter);
    }

    public LocalDateTime getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = LocalDateTime.parse(max, formatter);
    }

    public long getIncrement() {
        return increment;
    }

    public void setIncrement(long increment) {
        this.increment = increment;
    }
}
