package ar.edu.itba.ss.algorithms;

import ar.edu.itba.ss.models.ContractileParticle;
import ar.edu.itba.ss.models.Point2D;
import ar.edu.itba.ss.models.WallContact;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CPMEscapeRoomSimulation extends EscapeRoomSimulation {

    @Override
    public ContractileParticle getNextParticle(ContractileParticle particle, List<ContractileParticle> otherParticles) {
        double x, y, vx, vy, radius;

        x       = particle.getX();
        y       = particle.getY();
        vx      = particle.getVx();
        vy      = particle.getVy();
        radius  = particle.getRadius();

        /** Determine contacts */

        List<ContractileParticle> particleContacts = otherParticles.stream().filter(particle::overlapsWith).collect(Collectors.toList());
        List<WallContact> wallContacts = getWallContacts(particle);
        List<Object> contacts = new LinkedList<>();
        contacts.addAll(particleContacts);
        contacts.addAll(wallContacts);

        /** Calculate escape velocity */

        double escapeVx = 0;
        double escapeVy = 0;

        if (!contacts.isEmpty()) {
            double eX = 0;
            double eY = 0;

            for (Object c: contacts) {
                double cX, cY;
                if (c instanceof WallContact) {
                    cX = ((WallContact)c).getX();
                    cY = ((WallContact)c).getY();
                }
                else if (c instanceof ContractileParticle) {
                    cX = ((ContractileParticle)c).getX();
                    cY = ((ContractileParticle)c).getY();
                }
                else {
                    throw new IllegalStateException("Contact unsupported: " + c);
                }

                double d = particle.distance(cX, cY);
                eX += (particle.getX() - cX) / d;
                eY += (particle.getY() - cY) / d;
            }

            double norm = Math.sqrt(Math.pow(eX, 2) + Math.pow(eY, 2));
            escapeVx = escapeSpeed * (eX / norm);     // TODO: Chequear los módulos, a veces no parecen consistentes (a menor distancia en eje X, devuelve mayor modulo en y)
            escapeVy = escapeSpeed * (eY / norm);
            radius = minRadius;
        }
        else { // TODO: preguntar si hay que aumentar el tamaño incluso si se superpone
            /** Radius update */

            double newRadius = radius + maxRadius / (tau / dt);
            radius = Math.min(newRadius, maxRadius);
        }

        /** Calculate desired velocity */

        double desiredVx = 0;
        double desiredVy = 0;

        Point2D target = getTarget(particle);

        double desiredSpeed = maxDesiredSpeed * Math.pow((radius - minRadius)/(maxRadius - minRadius), beta);

        double eTargetDist = particle.distance(target);
        double eTargetX = (target.getX() - particle.getX()) / eTargetDist;
        double eTargetY = (target.getY() - particle.getY()) / eTargetDist;

        desiredVx = desiredSpeed * eTargetX;
        desiredVy = desiredSpeed * eTargetY;

        /** Next Particle */

        vx = desiredVx + escapeVx;
        vy = desiredVy + escapeVy;

        x += vx * dt;
        y += vy * dt;

        return particle.clone()
            .withX(x)
            .withY(y)
            .withVx(vx)
            .withVy(vy)
            .withRadius(radius)
            ;
    }

    private List<WallContact> getWallContacts(ContractileParticle particle) {

        final List<WallContact> possibleWallContacts = Arrays.asList(
            new WallContact(0       , particle.getY()), // left wall
            new WallContact(roomWidth  , particle.getY()), // right wall
            new WallContact(roomHeight , particle.getX())  // top wall
        );

        final List<WallContact> wallContacts = possibleWallContacts.stream()
            .filter(particle::overlapsWith)
            .collect(Collectors.toList());

        double targetLeftCornerX  = (roomWidth/2) - (targetWidth/2);
        double targetRightCornerX = (roomWidth/2) + (targetWidth/2);

        if (particle.getX() < targetLeftCornerX || particle.getX() > targetRightCornerX) { // outside target
            final WallContact bottomWallContact = new WallContact(particle.getX(), 0);
            if (particle.overlapsWith(bottomWallContact)) {
                wallContacts.add(bottomWallContact);
            }
        }
        else if (particle.getX() < (roomWidth/2)) { // inside target, left
            final WallContact leftCorner = new WallContact(targetLeftCornerX, 0);
            if(particle.overlapsWith(leftCorner))
                wallContacts.add(leftCorner);
        }
        else { // particle.getX() >= (roomWidth/2)  // inside target, right
            final WallContact rightCorner = new WallContact(targetRightCornerX, 0);
            if(particle.overlapsWith(rightCorner))
                wallContacts.add(rightCorner);
        }

        return  wallContacts;
    }

    private Point2D getTarget(ContractileParticle particle) {
        double particleX, particleY;
        particleX = particle.getX();
        particleY = particle.getY();

        double targetX;
        double targetY;

        double left;
        double right;

        if (particleY > 0) {
            double targetLeftUpperCornerX  = (roomWidth/2) - (targetWidth/2);

            left = targetLeftUpperCornerX + 0.2 * targetWidth;
            right = targetLeftUpperCornerX + 0.8 * targetWidth;

            targetY = 0;
        }
        else {
            double targetLeftLowerCornerX  = (roomWidth/2) - (outerTargetWidth/2);
            double targetRightLowerCornerX = (roomWidth/2) + (outerTargetWidth/2);

            left = targetLeftLowerCornerX;
            right = targetRightLowerCornerX;

            targetY = -outerTargetDistance;
        }

        if (particleX < left || particleX > right) {
            targetX = random.nextDouble() * (right - left) + left;
        }
        else {
            targetX = particleX;
        }

        return new Point2D(targetX, targetY);
    }

    //////////////// Autogenerated /////////////////

    public CPMEscapeRoomSimulation withRandom(Random random) {
        setRandom(random);
        return this;
    }

    public CPMEscapeRoomSimulation withDt(double dt) {
        setDt(dt);
        return this;
    }

    public CPMEscapeRoomSimulation withMaxTime(double maxTime) {
        setMaxTime(maxTime);
        return this;
    }

    public CPMEscapeRoomSimulation withSaveFactor(long saveFactor) {
        setSaveFactor(saveFactor);
        return this;
    }

    public CPMEscapeRoomSimulation withStatusBarActivated(boolean statusBarActivated) {
        setStatusBarActivated(statusBarActivated);
        return this;
    }

    public CPMEscapeRoomSimulation withRoomWidth(double roomWidth) {
        setRoomWidth(roomWidth);
        return this;
    }

    public CPMEscapeRoomSimulation withRoomHeight(double roomHeight) {
        setRoomHeight(roomHeight);
        return this;
    }

    public CPMEscapeRoomSimulation withTargetWidth(double targetWidth) {
        setTargetWidth(targetWidth);
        return this;
    }

    public CPMEscapeRoomSimulation withOuterTargetDistance(double outerTargetDistance) {
        setOuterTargetDistance(outerTargetDistance);
        return this;
    }

    public CPMEscapeRoomSimulation withOuterTargetWidth(double outerTargetWidth) {
        setOuterTargetWidth(outerTargetWidth);
        return this;
    }

    public CPMEscapeRoomSimulation withMinRadius(Double minRadius) {
        setMinRadius(minRadius);
        return this;
    }

    public CPMEscapeRoomSimulation withMaxRadius(Double maxRadius) {
        setMaxRadius(maxRadius);
        return this;
    }

    public CPMEscapeRoomSimulation withBeta(Double beta) {
        setBeta(beta);
        return this;
    }

    public CPMEscapeRoomSimulation withTau(Double tau) {
        setTau(tau);
        return this;
    }

    public CPMEscapeRoomSimulation withEscapeSpeed(Double escapeSpeed) {
        setEscapeSpeed(escapeSpeed);
        return this;
    }

    public CPMEscapeRoomSimulation withMaxDesiredSpeed(Double maxDesiredSpeed) {
        setMaxDesiredSpeed(maxDesiredSpeed);
        return this;
    }

    public CPMEscapeRoomSimulation withParticles(List<ContractileParticle> particles) {
        setParticles(particles);
        return this;
    }

}
