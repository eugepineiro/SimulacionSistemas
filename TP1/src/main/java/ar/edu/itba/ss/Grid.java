package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    public static <T extends Particle> List<T>[][] build(List<T> particles, int M, int L){
        return build(particles, M, L, L);
    }

    public static <T extends Particle> List<T>[][] build(List<T> particles, int M, int width, int height){
        int min = Math.min(width, height);
        double cellSize = 1.0 * min/M;

        int widthGridSize = (int) Math.ceil(width/cellSize);
        int heightGridSize = (int) Math.ceil(height/cellSize);

        List<T>[][] matrix = new ArrayList[widthGridSize][heightGridSize];

        for (int i = 0; i < widthGridSize; i++) {
            for (int j = 0; j < heightGridSize; j++) {
                matrix[i][j] = new ArrayList<>();
            }
        }

        particles.forEach(p -> matrix[(int) (p.getX() / cellSize)][(int) (p.getY() / cellSize)].add(p));

        return matrix;
    }
}
