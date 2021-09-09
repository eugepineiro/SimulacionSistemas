public class Event {

    private double time;
    private VelocityParticle p1;
    private VelocityParticle p2;



    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Event(double time, VelocityParticle p1, VelocityParticle p2) {
        this.time = time;
        this.p1 = p1;
        this.p2 = p2;
    }







}
