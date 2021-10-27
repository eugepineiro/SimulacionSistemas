package ar.edu.itba.ss.algorithms;

import ar.edu.itba.ss.models.ContractileParticle;
import ar.edu.itba.ss.models.Point2D;
import ar.edu.itba.ss.models.WallContact;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CPM {

    public static ContractileParticle getNextParticle(ContractileParticle particle, List<ContractileParticle> otherParticles, double roomWidth, double roomHeight, double targetWidth, double outerTargetDistance, double outerTargetWidth, double dt, Random r) {
        double x, y, vx, vy, radius;

        x       = particle.getX();
        y       = particle.getY();
        vx      = particle.getVx();
        vy      = particle.getVy();
        radius  = particle.getRadius();

        /** Determine contacts */

        List<ContractileParticle> particleContacts = otherParticles.stream().filter(particle::overlapsWith).collect(Collectors.toList());
        List<WallContact> wallContacts = getWallContacts(particle, roomHeight, roomWidth, targetWidth);
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
            escapeVx = particle.getEscapeSpeed() * (eX / norm);     // TODO: Chequear los módulos, a veces no parecen consistentes (a menor distancia en eje X, devuelve mayor modulo en y)
            escapeVy = particle.getEscapeSpeed() * (eY / norm);
            radius = particle.getMinRadius();
        }
        else { // TODO: preguntar si hay que aumentar el tamaño incluso si se superpone
            /** Radius update */

            double newRadius = radius + particle.getMaxRadius() / (particle.getTau() / dt);
            radius = Math.min(newRadius, particle.getMaxRadius());
        }

        /** Calculate desired velocity */

        double desiredVx = 0;
        double desiredVy = 0;

        Point2D target = getTarget(particle, roomWidth, targetWidth, outerTargetDistance, outerTargetWidth, r);

        double desiredSpeed = particle.getMaxDesiredSpeed() * Math.pow((radius - particle.getMinRadius())/(particle.getMaxRadius() - particle.getMinRadius()), particle.getBeta());

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

    public static List<WallContact> getWallContacts(ContractileParticle particle, double roomWidth, double roomHeight, double targetWidth) {

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

    public static Point2D getTarget(ContractileParticle particle, double roomWidth, double targetWidth, double outerTargetDistance, double outerTargetWidth, Random r) {
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
            targetX = r.nextDouble() * (right - left) + left;
        }
        else {
            targetX = particleX;
        }

        return new Point2D(targetX, targetY);
    }
}
