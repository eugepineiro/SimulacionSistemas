package ar.edu.itba.ss;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XYZ_Writer {
    private static class PCoords {
        private final String type;
        private final long id;
        private final double x, y, vx, vy, mass, radius, time;

        public PCoords(String type, long id, double x, double y, double vx, double vy, double mass, double radius, double time) {
            this.type = type;
            this.id = id;
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.mass  = mass;
            this.radius = radius;
            this.time = time;
        }
        
        @Override
        public String toString() {
            return String.format("%s\t%d\t%g\t%g\t%g\t%g\t%g\t%g\t%g\n", type, id, x, y, vx, vy, mass, radius, time);
        }
    }

    private static String PATH = "TP3/src/main/resources/ovito";
    private final List<List<PCoords>> frames;
    private FileWriter file;

    public static void setPath(String path) {
        PATH = path;
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

    public ar.edu.itba.ss.XYZ_Writer addFrame(ExtendedEvent extendedEvent) {
        List<PCoords> frame = extendedEvent.getFrame().stream()
                .map(p -> new PCoords(p.getType().toString(), p.getId(), p.getX(), p.getY(), p.getVx(), p.getVy(), p.getMass(), p.getRadius(), extendedEvent.getEvent().getTime()))
                .collect(Collectors.toList());
        frames.add(frame);

        return this;
    }

    public ar.edu.itba.ss.XYZ_Writer addAllFrames(List<ExtendedEvent> events) {
        events.forEach(this::addFrame);
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

}

