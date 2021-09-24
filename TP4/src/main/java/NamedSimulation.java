import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NamedSimulation implements Simulation<List> {

    // Status bar
    private final   int                     STATUS_BAR_SIZE         = 31;

    private         List<VelocityParticle>  particles;
    private         long                    gridSide;
    private         boolean                 statusBarActivated;

    // Stop conditions

    public List simulate() {
        return null;
    }

    //////////////// Autogenerated /////////////////

    public List<VelocityParticle> getParticles() {
        return particles;
    }
    public void setParticles(List<VelocityParticle> particles) {
        this.particles = particles;
    }
    public NamedSimulation withParticles(List<VelocityParticle> particles) {
        setParticles(particles);
        return this;
    }

    public long getGridSide() {
        return gridSide;
    }
    public void setGridSide(long gridSide) {
        this.gridSide = gridSide;
    }
    public NamedSimulation withGridSide(long gridSide) {
        setGridSide(gridSide);
        return this;
    }

    public boolean isStatusBarActivated() {
        return statusBarActivated;
    }
    public void setStatusBarActivated(boolean statusBarActivated) {
        this.statusBarActivated = statusBarActivated;
    }
    public NamedSimulation withStatusBarActivated(boolean statusBarActivated) {
        setStatusBarActivated(statusBarActivated);
        return this;
    }
}
