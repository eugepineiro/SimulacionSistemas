import java.util.HashMap;
import java.util.Map;

public class VelocityParticle extends Particle {

    private double vx;
    private double vy;
    private double mass;
    private long collisions;

    public VelocityParticle(Particle particle, double speed, double angle, double mass) {
        this(particle.getId(), particle.getX(), particle.getY(), particle.getRadius(), speed, angle, mass);
    }

    public VelocityParticle(long id, double x, double y, double radius, double speed, double angle, double mass) {
        super(id, x, y, radius);
        this.vx = speed * Math.cos(angle);
        this.vy = speed * Math.sin(angle);
        this.mass = mass;
        this.collisions = 0L;
    }

    public double getSpeed() {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
    }

    public double getAngle() {
        return (vx == 0)? ((vy >= 0)? Math.PI/2.0 : Math.PI*(3/2)): Math.tan(vy/vx);
    }

    /* Duration of time until the invoking particle collides with a vertical wall */
    public double collidesX(long gridSide) {
        return (vx < 0) ?
            (0-x)/vx :              // left wall
            ( (vx > 0) ?
                (gridSide-x)/vx :   // right wall
                -1                  // going up or down
            );
    }

    /* Duration of time until the invoking particle collides with a horizontal wall */
    public double collidesY(long gridSide) {
        return (vy < 0) ?
            (0-y)/vy :              // lower wall
            ( (vy > 0) ?
                (gridSide-y)/vy :   // upper wall
                -1                  // going left or right
            );
    }

    /* Duration of time until the invoking particle collides with particle p */
    public double collides(VelocityParticle p) {

        Map<String, Double> deltas = getDeltasAndInners(p);

        double inner_v_r = deltas.get("delta_vx") * deltas.get("delta_x") + deltas.get("delta_vy") * deltas.get("delta_y");
        double inner_v_v = deltas.get("delta_vx") * deltas.get("delta_vx") + deltas.get("delta_vy") * deltas.get("delta_vy");
        double inner_r_r = deltas.get("delta_x") * deltas.get("delta_x") + deltas.get("delta_y") * deltas.get("delta_y");

        double R1, R2;
        R1 = radius;
        R2 = p.getRadius();

        double distance = Math.pow(inner_v_r, 2) - inner_v_v * ( inner_r_r - Math.pow(R1 + R2, 2));

        if ( inner_v_r >= 0 || distance < 0)
            return -1; // inf

        return - ( inner_v_r + Math.sqrt(distance) ) / inner_v_v;
    }

    /* Update the invoking particle to simulate it bouncing off a vertical wall. */
    public void bounceX(){
        vx = -vx;
        collisions++;
    }

    /* Update the invoking particle to simulate it bouncing off a horizontal wall */
    public void bounceY(){
        vy = -vy;
        collisions++;
    }

    /* Update both particles to simulate them bouncing off each other. */
    public void bounce(VelocityParticle p){
        // collision
        double m_i = mass;
        double m_j = p.getMass();

        double sigma = radius + p.getRadius();

        double delta_vx = vx - p.getVx();
        double delta_vy = vy - p.getVy();

        double delta_x = x - p.getX();
        double delta_y = y - p.getY();

        double inner_v_r = delta_vx * delta_x + delta_vy * delta_y;

        double J = (2 * m_i * m_j * inner_v_r)/(sigma * (m_i + m_j));

        double J_x = J * (x - p.getX()) / sigma;
        double J_y = J * (y - p.getY()) / sigma;

        // Particle i update (me)

        vx = vx + J_x/m_i;
        vy = vy + J_y/m_i;

        // Particle j update

        vx = vx + J_x/m_j;
        vy = vy + J_y/m_j;

        collisions++;
    }

    /* Total number of collisions involving this particle */
    public long getCollisionCount(){
        return collisions;
    }

    private Map<String, Double> getDeltasAndInners(VelocityParticle p) {
        Map<String, Double> deltas = new HashMap<>();

        deltas.put("delta_x", x - p.getX());
        deltas.put("delta_y", y - p.getY());
        deltas.put("delta_vx", vx - p.getVx());
        deltas.put("delta_vy", vy - p.getVy());
        deltas.put("inner_v_r", deltas.get("delta_vx") * deltas.get("delta_x") + deltas.get("delta_vy") * deltas.get("delta_y"));
        deltas.put("inner_v_v", deltas.get("delta_vx") * deltas.get("delta_vx") + deltas.get("delta_vy") * deltas.get("delta_vy"));
        deltas.put("inner_r_r", deltas.get("delta_x") * deltas.get("delta_x") + deltas.get("delta_y") * deltas.get("delta_y"));

        return deltas;
    }


    ////////////////////////////// Bureaucracy //////////////////////////////

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
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
