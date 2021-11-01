package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellIndexMethod extends SearchMethod {

     public static <T extends Particle> Map<T, List<T>> search(List<T>[][] matrix, double interactionRadius, int M, int L, boolean periodicReturnCond) {
          return search(matrix, interactionRadius, M, L, L, periodicReturnCond);
     }

     public static <T extends Particle> Map<T, List<T>> search(List<T>[][] matrix, double interactionRadius, int M, int width, int height, boolean periodicReturnCond) {

          Map<T, List<T>> particleMap = new HashMap<>();

          T aux;

          // Recorrido de grilla
          for(int i = 0 ; i < matrix.length ; i++) {
               for(int j= 0; j < matrix[i].length ; j++) {

                    // partículas de una celda
                    for(int t = 0; t < matrix[i][j].size(); t++){

                         aux = matrix[i][j].get(t);
                         if (!particleMap.containsKey(aux)) {particleMap.put(aux, new ArrayList<>());}

                         // chequeo contra la misma celda
                         for(int k = 0; k < matrix[i][j].size(); k++) {
                              if (k != t) {
                                   addNeighbours(particleMap, matrix[i][j].get(t), matrix[i][j].get(k), interactionRadius, width, height, periodicReturnCond);
                              }
                         }

                         // chequeo celda superior

                         int k = periodicReturnCond? (j+1) % matrix[i].length : (j+1);
                         if(k < matrix[i].length) {
                              for(T p: matrix[i][k]) {
                                   addNeighbours(particleMap, matrix[i][j].get(t), p, interactionRadius, width, height, periodicReturnCond);
                              }
                         }

                         // chequeo columna derecha (i+1; j-1) (i+1; j) (i+1; j+1)
                         int h = periodicReturnCond? (i+1) % matrix.length :(i + 1);

                         if(h < matrix.length) {
                              for (int r=0; r < 3; r++) {
                                   if(k >= 0 && k < matrix[i].length) {
                                        for(T p: matrix[h][k]) {
                                             addNeighbours(particleMap, matrix[i][j].get(t), p, interactionRadius, width, height, periodicReturnCond);
                                        }
                                   }

                                   if (periodicReturnCond) {
                                        k = (k-1) % matrix[i].length;
                                        if (k<0) k += matrix[i].length;
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
