import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellIndexMethod extends SearchMethod {

     public static <T extends Particle> Map<T, List<T>> search(List<T>[][] matrix, double interactionRadius, int M, int L, boolean periodicReturnCond) {

          Map<T, List<T>> particleMap = new HashMap<>();

          T aux;

          // Recorrido de grilla
          for(int i = 0 ; i < M ; i++) {
               for(int j= 0; j < M ; j++) {

                    // partículas de una celda
                    for(int t = 0; t < matrix[i][j].size(); t++){

                         aux = matrix[i][j].get(t);
                         if (!particleMap.containsKey(aux)) {particleMap.put(aux, new ArrayList<>());}

                         // chequeo contra la misma celda
                         for(int k = t + 1; k < matrix[i][j].size(); k++) {
                              addNeighbours(particleMap, matrix[i][j].get(t), matrix[i][j].get(k), interactionRadius, L, periodicReturnCond);
                         }

                         // chequeo celda superior

                         int k = periodicReturnCond? (j+1) % M : (j+1);
                         if(k < M) {
                              for(T p: matrix[i][k]) {
                                   addNeighbours(particleMap, matrix[i][j].get(t), p, interactionRadius, L, periodicReturnCond);
                              }
                         }

                         // chequeo columna derecha (i+1; j-1) (i+1; j) (i+1; j+1)
                         int h = periodicReturnCond? (i+1) % M :(i + 1);

                         if(h < M) {
                              for (int r=0; r < 3; r++) {
                                   if(k >= 0 && k < M) {
                                        for(T p: matrix[h][k]) {
                                             addNeighbours(particleMap, matrix[i][j].get(t), p, interactionRadius, L, periodicReturnCond);
                                        }
                                   }

                                   if (periodicReturnCond) {
                                        k = (k-1) % M;
                                        if (k<0) k += M;
                                   }
                                   else {
                                        k = k-1;
                                   }
                              }
                         }
                    }

               }
          }
          return particleMap;
     }
}
