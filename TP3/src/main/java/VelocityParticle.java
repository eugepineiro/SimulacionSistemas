public class VelocityParticle extends Particle {

    private Double vx;
    private Double vy;
    private Double mass;

    public VelocityParticle(Particle particle, Double speed, Double angle, Double mass) {
        this(particle.getId(), particle.getX(), particle.getY(), particle.getRadius(), speed, angle, mass);
    }

    public VelocityParticle(long id, double x, double y, double radius, Double speed, Double angle, Double mass) {
        super(id, x, y, radius);
        this.vx = speed * Math.cos(angle);
        this.vy = speed * Math.sin(angle);
        this.mass = mass;
    }

    public Double getSpeed() {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
    }

    public Double getAngle() {
        return (vx == 0)? ((vy >= 0)? Math.PI/2.0 : Math.PI*(3/2)): Math.tan(vy/vx);
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

    public Double getMass() {
        return mass;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    @Override
    public String toString() {
        return "VelocityParticle{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                ", vx=" + vx +
                ", vy=" + vy +
                ", mass=" + mass +
                '}';
    }
}
