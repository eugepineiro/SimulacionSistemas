package ar.edu.itba.ss;

import java.util.List;
import java.util.stream.Collectors;

public class ExtendedEvent {

    private Event event;
    private List<VelocityParticle> frame;

    public ExtendedEvent(Event event) {
        this.event = event;
    }

    public static ExtendedEvent from(Event event) {
        return new ExtendedEvent(event);
    }

    public ExtendedEvent withFrame(List<VelocityParticle> frame) {
        this.frame = frame.stream().map(VelocityParticle::clone).collect(Collectors.toList());
        return this;
    }

    public Event getEvent() {
        return event;
    }

    public List<VelocityParticle> getFrame() {
        return frame;
    }

    @Override
    public String toString() {
        return "ar.edu.itba.ss.ExtendedEvent{" +
                "event=" + event +
                '}';
    }
}
