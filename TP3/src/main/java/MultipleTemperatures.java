import java.util.List;

public class MultipleTemperatures {

    private boolean activated;
    private List<List<Double>> speeds_ranges;

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public List<List<Double>> getSpeeds_ranges() {
        return speeds_ranges;
    }

    public void setSpeeds_ranges(List<List<Double>> speeds_ranges) {
        this.speeds_ranges = speeds_ranges;
    }
}
