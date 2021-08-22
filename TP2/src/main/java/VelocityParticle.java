public class VelocityParticle extends Particle {

    private Double speed;
    private Double angle;

    public VelocityParticle(Particle particle, Double speed, Double angle) {
        super(particle.getId(), particle.getX(), particle.getY(), particle.getRadius());
        this.speed = speed;
        this.angle = angle;
    }

    public VelocityParticle(long id, double x, double y, double radius, Double speed, Double angle) {
        super(id, x, y, radius);
        this.speed = speed;
        this.angle = angle;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }
}
