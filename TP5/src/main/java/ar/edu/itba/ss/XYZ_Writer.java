package ar.edu.itba.ss;

import ar.edu.itba.ss.models.ContractileParticle;
import ar.edu.itba.ss.models.Frame;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class XYZ_Writer {
    private static enum PType {
        WALL,
        TARGET,
        HALL_WALL,
        OUTER_TARGET,
        PARTICLE
        ;
    }

    private static class PCoords {
        private final PType type;
        private final long id;
        private final double x, y, vx, vy, radius;

        public PCoords(PType type, long id, double x, double y, double vx, double vy, double radius) {
            this.type = type;
            this.id = id;
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.radius = radius;
        }
        
        @Override
        public String toString() {
            return String.format("%s\t%d\t%g\t%g\t%g\t%g\t%g\n", type, id, x, y, vx, vy, radius);
        }
    }

    private static String PATH = "TP5/src/main/resources/ovito";
    private double roomHeight, roomWidth, targetWidth, outerTargetDistance, outerTargetWidth;
    private final List<List<PCoords>> frames;
    private FileWriter file;

    public static void setPath(String path) {
        XYZ_Writer.PATH = path;
    }

    public XYZ_Writer(String filename) {

        try {
            this.file = new FileWriter(String.format("%s/%s.exyz", PATH, filename));
        } catch (IOException e) {
            System.out.printf("%s.xyze open failed.\n", filename);
            e.printStackTrace();
            System.exit(1);
        }

        this.frames = new ArrayList<>();
    }

    private static final double wallPointsSeparation = 0.2;
    private static final double wallDepth            = 0.20;
    private List<PCoords> walls                      = null;

    private void initWalls(long maxParticleId) {
        long startingId = maxParticleId;
        walls = new LinkedList<>();

        double inc;

        // Left and right wallç
        inc = wallPointsSeparation;
//        inc = roomHeight/wallPointsSeparation;
        for (double y = 0; y <= roomHeight; y+=inc) {
            walls.add(new PCoords(PType.WALL, startingId++, 0     , y, 0, 0, wallDepth/2));
            walls.add(new PCoords(PType.WALL, startingId++, roomWidth, y, 0, 0, wallDepth/2));
        }

//        inc = roomWidth/wallPointsSeparation;
        double leftUpperCornerX  = (roomWidth/2) - (targetWidth/2);
        double rightUpperCornerX = (roomWidth/2) + (targetWidth/2);
        for (double x = 0; x <= roomWidth; x+=inc) {
            // Top wall
            walls.add(new PCoords(PType.WALL, startingId++, x, roomHeight, 0, 0, wallDepth/2));

            // Bottom wall
            if (x < leftUpperCornerX || x > rightUpperCornerX) {
                walls.add(new PCoords(PType.WALL, startingId++  , x, 0, 0, 0, wallDepth/2));
            }

            // Target
//            else {
//                walls.add(new PCoords(PType.TARGET, startingId++, x, 0, 0, 0, wallDepth/2));
//            }
        }

        // Hall left and right walls
//        inc = outerTargetDistance/wallPointsSeparation;
        double leftBottomCornerX  = (roomWidth/2) - (outerTargetWidth/2);
        double rightBottomCornerX = (roomWidth/2) + (outerTargetWidth/2);
        for (double y = -outerTargetDistance; y <= 0; y+=inc) {
            walls.add(new PCoords(PType.HALL_WALL, startingId++, leftBottomCornerX , y, 0, 0, wallDepth/2));
            walls.add(new PCoords(PType.HALL_WALL, startingId++, rightBottomCornerX, y, 0, 0, wallDepth/2));
        }

        // Bottom Target
//        inc = outerTargetWidth/wallPointsSeparation;
        for (double x = leftBottomCornerX; x <= leftBottomCornerX + outerTargetWidth; x+=inc) {
            walls.add(new PCoords(PType.OUTER_TARGET, startingId++, x, -outerTargetDistance, 0, 0, wallDepth/2));
        }

    }

    public List<PCoords> getWalls(long maxParticleId) {
        if (walls == null) {
            initWalls(maxParticleId);
        }
        return walls;
    }

    public XYZ_Writer addFrame(Frame<ContractileParticle> frame) {
        List<PCoords> f = frame.getParticles().stream().map(p -> new PCoords(PType.PARTICLE, p.getId(), p.getX(), p.getY(), p.getVx(), p.getVy(), p.getRadius())).collect(Collectors.toList());
        long maxId = frame.getParticles().stream().map(Particle::getId).max(Comparator.naturalOrder()).orElse(0L);
        f.addAll(getWalls(maxId));

        frames.add(f);

        return this;
    }

    public XYZ_Writer addAllFrames(List<Frame<ContractileParticle>> framesList) {
        framesList.forEach(this::addFrame);
        return this;
    }
    
    public void writeAndClose() {
        try{
            for(List<PCoords> frame: frames) {
                file.write(String.format("%d\n\n", frame.size()));
                frame.forEach(p -> {
                    try {
                        file.write(p.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getRoomWidth() {
        return roomWidth;
    }
    public void setRoomWidth(double roomWidth) {
        this.roomWidth = roomWidth;
    }
    public XYZ_Writer withRoomWidth(double roomWidth) {
        setRoomWidth(roomWidth);
        return this;
    }

    public double getRoomHeight() {
        return roomHeight;
    }
    public void setRoomHeight(double roomHeight) {
        this.roomHeight = roomHeight;
    }
    public XYZ_Writer withRoomHeight(double roomHeight) {
        setRoomHeight(roomHeight);
        return this;
    }

    public double getTargetWidth() {
        return targetWidth;
    }
    public void setTargetWidth(double targetWidth) {
        this.targetWidth = targetWidth;
    }
    public XYZ_Writer withTargetWidth(double targetWidth) {
        setTargetWidth(targetWidth);
        return this;
    }

    public double getOuterTargetDistance() {
        return outerTargetDistance;
    }
    public void setOuterTargetDistance(double outerTargetDistance) {
        this.outerTargetDistance = outerTargetDistance;
    }
    public XYZ_Writer withOuterTargetDistance(double outerTargetDistance) {
        setOuterTargetDistance(outerTargetDistance);
        return this;
    }

    public double getOuterTargetWidth() {
        return outerTargetWidth;
    }
    public void setOuterTargetWidth(double outerTargetWidth) {
        this.outerTargetWidth = outerTargetWidth;
    }
    public XYZ_Writer withOuterTargetWidth(double outerTargetWidth) {
        setOuterTargetWidth(outerTargetWidth);
        return this;
    }

}

