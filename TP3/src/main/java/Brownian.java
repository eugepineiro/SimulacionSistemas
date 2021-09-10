import java.util.*;
import java.util.stream.Collectors;

public class Brownian {

    private static final double MAX_TIME = 20;
    private static final double MAX_EVENTS = 200;

    public static List<ExtendedEvent> simulate(List<VelocityParticle> particles, long gridSide) {

        List<ExtendedEvent> extendedEvents = new LinkedList<>();
        SortedSet<Event> programmedEvents = new TreeSet<>(Comparator.comparing(Event::getTime));

        Event currentEvent;
        List<VelocityParticle> lastModified;

        double currentTime = 0;
        double deltaTime = 0;

        // Calculate first events
        addFirstEvents(programmedEvents, particles, gridSide, currentTime);

        for (long events = 0; !programmedEvents.isEmpty() /* && currentTime < MAX_TIME */ && events < MAX_EVENTS; events++) { // TODO check cut condition
//            System.out.println("-------------- QUEUE:");
//            programmedEvents.forEach(System.out::println);
//            System.out.println("QUEUE END --------------\n");

            currentEvent = programmedEvents.first();
            deltaTime = currentEvent.getTime() - currentTime;
            currentTime += deltaTime;

//            System.out.printf("Delta time added: %.4g\n", deltaTime);

            // Update particles times
            updateParticles(particles, deltaTime);

            // Solve collisions and update collided particles
            lastModified = solveCollision(currentEvent);

            // Remove deprecated collisions
            // TODO: Mandar a una funcion esto
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

            // New collisions
            addNewEvents(programmedEvents, particles, gridSide, currentEvent, currentTime);

            // Add to history
            currentEvent.freeze();
            extendedEvents.add(ExtendedEvent.from(currentEvent).withFrame(particles));
            programmedEvents.remove(currentEvent);

            System.out.println(currentEvent);

//            System.out.println("\n-------------- PARTICLES:");
//            particles.forEach(System.out::println);
//            System.out.println("PARTICLES --------------");
//
//            System.out.println("\n------------------------------------------------------------\n");
        }

        return extendedEvents;
    }

    private static void addFirstEvents(SortedSet<Event> queue ,List<VelocityParticle> velocityParticles, long gridSide, double currentTime){
        double time;

        for (int i = 0; i < velocityParticles.size(); i++) {
            VelocityParticle particle1 = velocityParticles.get(i);

            // Vertical Wall
            time = particle1.collidesX(gridSide);
            if (time >= 0) {
                queue.add(Event.collision(particle1, Wall.VERTICAL, currentTime + time));
            }

            // Horizontal Wall
            time = particle1.collidesY(gridSide);
            if (time >= 0) {
                queue.add(Event.collision(particle1, Wall.HORIZONTAL, currentTime + time));
            }

            for (int j = i+1; j < velocityParticles.size(); j++) {
                VelocityParticle particle2 = velocityParticles.get(j);

                //Particles Collision
                time = particle1.collides(particle2);
                if (time >= 0) {
                    queue.add(Event.collision(particle1, particle2, currentTime + time));
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

    private static void addNewEvents(SortedSet<Event> queue, List<VelocityParticle> velocityParticles, long gridSide, Event event, double currentTime){
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
                queue.add(Event.collision(particle, Wall.VERTICAL, currentTime + time));
            }

            // Horizontal Wall
            time = particle.collidesY(gridSide);
            if (time >= 0) {
                queue.add(Event.collision(particle, Wall.HORIZONTAL, currentTime + time));
            }

            for (VelocityParticle other: velocityParticles) {
                if (!collidedParticles.contains(other)) { // TODO: Not looking for next collision with exact same pair of particles at this point. Expecting collision with something else first.
                    //Particles Collision
                    time = particle.collides(other);
                    if(time >= 0 ) {
                        queue.add(Event.collision(particle, other, currentTime + time));
                    }
                }
            }
        }

    }

}
