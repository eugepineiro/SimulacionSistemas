import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class XYZ_Writer {
    private static class PCoords {
        private final long id;
        private final double x, y, z, angle;
        private Integer[] rgb;

        public PCoords(long id, double x, double y, double z, double angle) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.z = z;
            this.angle = angle;
        }
        
        @Override
        public String toString() {
            return String.format("%d\t%g\t%g\t%g\t%g\n", id, x, y, z, angle);
        }
    }

    private static final String PATH = "TP2/src/main/resources/ovito";
    private final List<List<PCoords>> frames;
    private FileWriter file;

    public XYZ_Writer(String filename) {

        try {
            this.file = new FileWriter(String.format("%s/%s.xyz", PATH, filename));
        } catch (IOException e) {
            System.out.printf("%s.xyz open failed.\n", filename);
            e.printStackTrace();
            System.exit(1);
        }

        this.frames = new ArrayList<>();
    }

    public XYZ_Writer addParticle(int frame, long id, double x, double y, double angle) {
        frames.get(frame).add(new PCoords(id, x, y, 0, angle));

        return this;
    }

    public XYZ_Writer addFrame(List<? extends Particle> particleList) {
        List<PCoords> frame = particleList.stream().map(p -> new PCoords(p.getId(), p.getX(), p.getY(), 0, ((VelocityParticle) p).getAngle())).collect(Collectors.toList());
        frames.add(frame);

        return this;
    }

    public XYZ_Writer addAllFrames(List<List<VelocityParticle>> frames) {
        frames.forEach(this::addFrame);

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

    private Integer[] angleToColor(Double angle){
        Integer[] rgb = new Integer[3];

        rgb[0] = (int) ((angle * 255/(2*Math.PI))) % (1<<8);
        rgb[1] = (int) ((angle * 255/(2*Math.PI)) - 128) % (1<<8);
        rgb[2] = (int) ((angle * 255/(2*Math.PI)) - 32) % (1<<8);

        if (rgb[0] < 0) rgb[0] += (1<<8);
        if (rgb[1] < 0) rgb[1] += (1<<8);
        if (rgb[2] < 0) rgb[2] += (1<<8);

        return rgb;
    }

}

