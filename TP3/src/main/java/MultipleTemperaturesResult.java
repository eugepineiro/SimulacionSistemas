import java.util.List;

public class MultipleTemperaturesResult {

    final private long N;
    final private double minSpeed;
    final private double maxSpeed;
    final private List<ExtendedEvent> events;

    public MultipleTemperaturesResult(long n, double minSpeed, double maxSpeed, List<ExtendedEvent> events) {
        N = n;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.events = events;
    }

    public long getN() {
        return N;
    }

    public double getMinSpeed() {
        return minSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public List<ExtendedEvent> getEvents() {
        return events;
    }
}