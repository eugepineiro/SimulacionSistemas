import java.util.List;

public class MultipleTemperaturesResult {

    static class Position {
        final private double x;
        final private double y;

        public Position(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    final private long N;
    final private double minSpeed;
    final private double maxSpeed;
    final private List<Position> positions;

    public MultipleTemperaturesResult(long n, double minSpeed, double maxSpeed, List<Position> positions) {
        N = n;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.positions = positions;
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

    public List<Position> getPositions() {
        return positions;
    }
}