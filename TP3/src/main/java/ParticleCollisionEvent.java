public class ParticleCollisionEvent extends Event{
    private VelocityParticle particle1;
    private VelocityParticle particle2;

    public ParticleCollisionEvent(VelocityParticle particle1, VelocityParticle particle2, double time) {
        super(time);
        this.particle1 = particle1;
        this.particle2 = particle2;
    }

    public VelocityParticle getParticle1() {
        return particle1;
    }

    public VelocityParticle getParticle2() {
        return particle2;
    }

    @Override
    public void freeze() {
        this.particle1 = particle1.clone();
        this.particle2 = particle2.clone();
    }

    @Override
    public String toString() {
        return "ParticlesCollisionEvent{" +
            "time=" + time +
            ", particle1=" + particle1 +
            ", particle2=" + particle2 +
            '}';
    }
}
