public abstract class Event implements Comparable<Event>{

    public enum Type {
        WALL_COLLISION,
        PARTICLE_COLLISION
    }

    final protected Type type;
    final protected double time;

    public Event(Type type, double time) {
        this.type = type;
        this.time = time;
    }

    public static WallCollisionEvent collision(VelocityParticle particle, Wall wall, double time) {
        return new WallCollisionEvent(particle, wall, time);
    }

    public static ParticleCollisionEvent collision(VelocityParticle particle, VelocityParticle particle2, double time) {
        return new ParticleCollisionEvent(particle, particle2, time);
    }

    public double getTime() {
        return time;
    }

    public Type getType() {
        return type;
    }

    public void freeze() {
        //
    }

    @Override
    public int compareTo(Event o) {
        if (this.time < o.getTime()) {
            return -1;
        }
        else if (this.time > o.getTime()) {
            return 1;
        }
        return 0;
    }
}
