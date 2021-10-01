package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Frame;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XYZ_Writer {
    private static class PCoords {
        private final String type;
        private final long id;
        private final double x, y, vx, vy, radius;

        public PCoords(String type, long id, double x, double y, double vx, double vy, double radius) {
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

    private static String PATH = "TP3/src/main/resources/ovito";
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

    public XYZ_Writer addFrame(Frame frame) {
        List<PCoords> f = frame.getParticles().stream().map(p -> new PCoords(p.getType().name(), p.getId(), p.getX(), p.getY(), p.getVx(), p.getVy(), p.getRadius())).collect(Collectors.toList());

        frames.add(f);

        return this;
    }

    public XYZ_Writer addAllFrames(List<Frame> framesList) {
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

}

