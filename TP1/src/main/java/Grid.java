import java.util.ArrayList;
import java.util.List;

public class Grid {

    public static <T extends Particle> List<T>[][] build(List<T> particles, int M, int L){
        double cellSize = 1.0 * L/M;

        List<T>[][] matrix = new ArrayList[M][M];
//            int [][] =  [5][5]

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                matrix[i][j] = new ArrayList<>();
            }
        }

//        Arrays.stream(matrix).forEach(() -> ArrayList::new);

        particles.forEach(p -> matrix[(int) (p.getX() / cellSize)][(int) (p.getY() / cellSize)].add(p));

//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < M; i++) {
//            for (int j = 0; j < M; j++) {
//                sb.append(matrix[i][j].toString()).append(", ");
//            }
//            sb.reverse().delete(0, 2).reverse();
//            sb.append("\n");
//        }
//
//        System.out.println(sb);

        return matrix;
    }
}
