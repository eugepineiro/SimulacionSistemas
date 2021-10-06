package ar.edu.itba.ss.config;

public class MultipleDt {
    private boolean activated;
    private double min_exp;
    private double max_exp;
    private double increment;

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public double getMin_exp() {
        return min_exp;
    }

    public void setMin_exp(double min_exp) {
        this.min_exp = min_exp;
    }

    public double getMax_exp() {
        return max_exp;
    }

    public void setMax_exp(double max_exp) {
        this.max_exp = max_exp;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }
}
