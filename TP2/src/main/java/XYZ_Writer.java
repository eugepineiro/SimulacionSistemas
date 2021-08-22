import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XYZ_Writer {
    private static class PCoords {
        private final long id;
        private final double x, y, z;

        public PCoords(long id, double x, double y, double z) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        @Override
        public String toString() {
            return String.format("%d\t%g\t%g\t%g\n", id, x, y, z);
        }
    }

    List<PCoords> particles;
    FileWriter file;

    public XYZ_Writer(String file_path) {

        try {
            this.file = new FileWriter(file_path);
        } catch (IOException e) {
            System.out.printf("%s open failed.\n", file_path);
            e.printStackTrace();
            System.exit(1);
        }

        this.particles = new ArrayList<>();
    }

    public void addParticle(long id, double x, double y) {
        particles.add(new PCoords(id, x, y, 0));
    }

    public void addAllParticles(List<Particle> particleList) {
        particleList.forEach(p -> addParticle(p.getId(), p.getX(), p.getY()));
    }
    
    public void writeAndClose() {
        try{
            file.write(String.format("%d\n\n", particles.size()));
            particles.forEach(p -> {
                try {
                    file.write(p.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

