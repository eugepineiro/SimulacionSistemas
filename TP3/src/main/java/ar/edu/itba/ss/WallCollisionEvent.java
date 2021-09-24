package ar.edu.itba.ss;

public class WallCollisionEvent extends Event {

    private VelocityParticle particle;
    final private Wall wall;

    public WallCollisionEvent(VelocityParticle particle, Wall wall, double time) {
        super(Type.WALL_COLLISION, time);
        this.particle = particle;
        this.wall = wall;
    }

    public VelocityParticle getParticle() {
        return particle;
    }

    public Wall getWall() {
        return wall;
    }

    @Override
    public void freeze() {
        this.particle = particle.clone();
    }

    @Override
    public String toString() {
        return "ar.edu.itba.ss.WallCollisionEvent{" +
            "time=" + time +
            ", particle=" + particle +
            ", wall=" + wall +
            '}';
    }
}
