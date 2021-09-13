import java.util.List;

public class MultipleSimulationsResult {

    static class TimedPosition {
        final private double time;
        final private double x;
        final private double y;

        TimedPosition(double time, double x, double y) {
            this.time = time;
            this.x = x;
            this.y = y;
        }

        public double getTime() {
            return time;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    final private List<TimedPosition> positions;

    public MultipleSimulationsResult(List<TimedPosition> positions) {
        this.positions = positions;
    }

    public List<TimedPosition> getPositions() {
        return positions;
    }
}