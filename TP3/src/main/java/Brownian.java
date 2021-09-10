import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Brownian {

    // Status bar
    private static final int                STATUS_BAR_SIZE         = 31;
    private static final String             SUBJECT_NAME            = "BIG";

    // Stop conditions
    private static final double             MAX_EVENTS              = 100000;

    private static final long               STUDIED_SUBJECT_ID      = 0;
    private static final Predicate<Event>   isSubjectHittingWall    = (event) -> (event instanceof WallCollisionEvent && ((WallCollisionEvent) event).getParticle().getId() == STUDIED_SUBJECT_ID);

    private static String getStatusBar(long gridSide, double pos, double radius) {

        double radiusPctg, pctgPos;
        int subjectPosition, radiusOffset, lowerBound, upperBound, subjectNameMargin;

        pctgPos                     =       pos     /   gridSide;
        radiusPctg                  =       radius  /   gridSide;

        subjectPosition             = (int) Math.ceil(pctgPos * STATUS_BAR_SIZE) - 1;
        radiusOffset                = (int) Math.ceil(radiusPctg * STATUS_BAR_SIZE) - 1;
        lowerBound                  =       subjectPosition - radiusOffset;
        upperBound                  =       subjectPosition + radiusOffset;

        subjectNameMargin           =       ( ( (upperBound - lowerBound) - 1 ) - SUBJECT_NAME.length() ) / 2;

        StringBuilder statusBar = new StringBuilder("|");

        for (int h = 0; h < STATUS_BAR_SIZE; h++) {
            if      (h < lowerBound)
                statusBar.append("*");
            else if (h == lowerBound)
                statusBar.append("(");
            else if (h < upperBound) {
                int l = (h - lowerBound - 1) - subjectNameMargin;
                if (l >= 0 && l < SUBJECT_NAME.length())
                    statusBar.append(SUBJECT_NAME.charAt(l));
                else
                    statusBar.append(" ");
            }
            else if (h == upperBound)
                statusBar.append(")");
            else
                statusBar.append("*");
        }

        statusBar.append("|");

        return statusBar.toString();
    }

    private static void printStatusBar(long gridSide, VelocityParticle subject, long events) {

        String statusBarX = getStatusBar(gridSide, subject.getX(), subject.getRadius());
        String statusBarY = getStatusBar(gridSide, subject.getY(), subject.getRadius());

        long numberOfEvents = (events / 1000) * 1000;

        System.out.printf("\r%s (X) %s (Y) - %d events", statusBarX, statusBarY, numberOfEvents);
    }

    public static List<ExtendedEvent> simulate(List<VelocityParticle> particles, VelocityParticle bigParticle, long gridSide) {

        List<ExtendedEvent> extendedEvents = new LinkedList<>();
        List<Event> programmedEvents = new SortedList<>(Comparator.comparing(Event::getTime));

        Event currentEvent;
        List<VelocityParticle> lastModified;

        double currentTime = 0;
        double deltaTime = 0;

        boolean subjectHittingWall = false;

        // Calculate first events
        addFirstEvents(programmedEvents, particles, gridSide, currentTime);

        for (long events = 0; !programmedEvents.isEmpty() && !subjectHittingWall && events < MAX_EVENTS; events++) {

            currentEvent = programmedEvents.get(0);
            deltaTime = currentEvent.getTime() - currentTime;
            currentTime += deltaTime;

            subjectHittingWall = isSubjectHittingWall.test(currentEvent);

            // Update particles times
            updateParticles(particles, deltaTime);

            // Status bar
            printStatusBar(gridSide, bigParticle, events);

            // Solve collisions and update collided particles
            lastModified = solveCollision(currentEvent);

            // Remove deprecated collisions
            removeDeprecatedCollisions(programmedEvents, lastModified);

            // New collisions
            addNewEvents(programmedEvents, particles, gridSide, currentEvent, currentTime);

            // Add to history
            programmedEvents.remove(currentEvent);
            currentEvent.freeze();
            extendedEvents.add(ExtendedEvent.from(currentEvent).withFrame(particles));
        }

        // Status bar end
        System.out.println();

        return extendedEvents;
    }

    private static void addFirstEvents(List<Event> programmedEvents, List<VelocityParticle> velocityParticles, long gridSide, double currentTime){
        double time;

        for (int i = 0; i < velocityParticles.size(); i++) {
            VelocityParticle particle1 = velocityParticles.get(i);

            // Vertical Wall
            time = particle1.collidesX(gridSide);
            if (time >= 0) {
                programmedEvents.add(Event.collision(particle1, Wall.VERTICAL, currentTime + time));
            }

            // Horizontal Wall
            time = particle1.collidesY(gridSide);
            if (time >= 0) {
                programmedEvents.add(Event.collision(particle1, Wall.HORIZONTAL, currentTime + time));
            }

            for (int j = i+1; j < velocityParticles.size(); j++) {
                VelocityParticle particle2 = velocityParticles.get(j);

                //Particles Collision
                time = particle1.collides(particle2);
                if (time >= 0) {
                    programmedEvents.add(Event.collision(particle1, particle2, currentTime + time));
                }
            }
        }
    }

    private static void addNewEvents(List<Event> programmedEvents, List<VelocityParticle> velocityParticles, long gridSide, Event event, double currentTime){
        List<VelocityParticle> collidedParticles = new ArrayList<>();

        if (event instanceof WallCollisionEvent) {
            WallCollisionEvent e = (WallCollisionEvent) event;
            VelocityParticle particle = e.getParticle();

            collidedParticles.add(particle);
        }
        else if (event instanceof ParticleCollisionEvent) {
            ParticleCollisionEvent e = (ParticleCollisionEvent) event;
            VelocityParticle particle1 = e.getParticle1();
            VelocityParticle particle2 = e.getParticle2();

            collidedParticles.add(particle1);
            collidedParticles.add(particle2);
        }

        double time;

        for (VelocityParticle particle: collidedParticles) {

            // Vertical Wall
            time = particle.collidesX(gridSide);
            if (time >= 0) {
                programmedEvents.add(Event.collision(particle, Wall.VERTICAL, currentTime + time));
            }

            // Horizontal Wall
            time = particle.collidesY(gridSide);
            if (time >= 0) {
                programmedEvents.add(Event.collision(particle, Wall.HORIZONTAL, currentTime + time));
            }

            for (VelocityParticle other: velocityParticles) {
                if (!collidedParticles.contains(other)) { // TODO: Not looking for next collision with exact same pair of particles at this point. Expecting collision with something else first.
                    //Particles Collision
                    time = particle.collides(other);
                    if(time >= 0 ) {
                        programmedEvents.add(Event.collision(particle, other, currentTime + time));
                    }
                }
            }
        }
    }

    private static void updateParticles(List<VelocityParticle> particles, double deltaTime){
        for(VelocityParticle particle: particles){
            particle.play(deltaTime);
        }
    }

    private static List<VelocityParticle> solveCollision(Event event){
        List<VelocityParticle> ret = new ArrayList<>();

        if (event instanceof WallCollisionEvent) {
            WallCollisionEvent e = (WallCollisionEvent) event;
            VelocityParticle particle = e.getParticle();

            if (e.getWall().equals(Wall.VERTICAL)) {
                particle.bounceX();
            } else if (e.getWall().equals(Wall.HORIZONTAL)) {
                particle.bounceY();
            }

            ret.add(particle);
        }
        else if (event instanceof ParticleCollisionEvent) {
            ParticleCollisionEvent e = (ParticleCollisionEvent) event;
            VelocityParticle particle1 = e.getParticle1();
            VelocityParticle particle2 = e.getParticle2();

            particle1.bounce(particle2);

            ret.add(particle1);
            ret.add(particle2);
        }

        return ret;
    }

    private static void removeDeprecatedCollisions(List<Event> programmedEvents, List<VelocityParticle> lastModified) {
        Iterator<Event> it = programmedEvents.iterator();

        while (it.hasNext()) {
            Event e = it.next();
            if (e instanceof WallCollisionEvent) {
                VelocityParticle particle = ((WallCollisionEvent) e).getParticle();
                if (lastModified.contains(particle))
                    it.remove();
            }
            else { // (e instanceof ParticleCollisionEvent)
                VelocityParticle particle1 = ((ParticleCollisionEvent) e).getParticle1();
                VelocityParticle particle2 = ((ParticleCollisionEvent) e).getParticle2();
                if (lastModified.contains(particle1) || lastModified.contains(particle2))
                    it.remove();
            }
        }
    }

}
