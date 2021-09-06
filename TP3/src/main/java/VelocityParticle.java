public class VelocityParticle extends Particle {

    private Double vx;
    private Double vy;

    public VelocityParticle(Particle particle, Double speed, Double angle) {
        this(particle.getId(), particle.getX(), particle.getY(), particle.getRadius(), speed, angle);
    }

    public VelocityParticle(long id, double x, double y, double radius, Double speed, Double angle) {
        super(id, x, y, radius);
        this.vx = speed * Math.cos(angle);
        this.vy = speed * Math.sin(angle);
    }

    public Double getSpeed() {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
    }

    public Double getAngle() {
        return (vx == 0)? ((vy >= 0)? 0: Math.PI): Math.tan(vy/vx);
    }

    public Double getVx() {
        return vx;
    }

    public void setVx(Double vx) {
        this.vx = vx;
    }

    public Double getVy() {
        return vy;
    }

    public void setVy(Double vy) {
        this.vy = vy;
    }
}
