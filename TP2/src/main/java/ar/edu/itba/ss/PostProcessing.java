package ar.edu.itba.ss;

import java.util.List;

public class PostProcessing {
    /** PARA UNA MISMA SEED, L, M, rc, speed
     * [
     *      {
     *          "polarization": [
     *                  [0.1, 0.2, 0.5, 0.8] // polarizacion de la simulacion 1 por cada frame
     *                  [0.1, 0.21, 0.5, 0.88] // polarizacion de la simulacion 2 por cada frame
     *          ],
     *          "density": 0.4,
     *          "N": 100,
     *          "noise": 1
     *      },
     *      { // cambio density, N o noise (todas las posibles combiinaciones)
     *          "polarization": [
     *                  [0.1, 0.2, 0.5, 0.8] // polarizacion de la simulacion 1 por cada frame
     *                  [0.1, 0.21, 0.5, 0.88] // polarizacion de la simulacion 2 por cada frame
     *          ],
     *          "density": 0.4,
     *          "N": 100,
     *          "noise": 1
     *      },
     * ]
     */

    private List<List<Double>> polarization;
    private Double density;
    private Integer N;
    private Double noise;

    public PostProcessing(List<List<Double>> polarization, Double density, Integer n, Double noise) {
        this.polarization = polarization;
        this.density = density;
        this.N = n;
        this.noise = noise;
    }

    public List<List<Double>> getPolarization() {
        return polarization;
    }

    public void setPolarization(List<List<Double>> polarization) {
        this.polarization = polarization;
    }

    public Double getDensity() {
        return density;
    }

    public void setDensity(Double density) {
        this.density = density;
    }

    public Integer getN() {
        return N;
    }

    public void setN(Integer n) {
        N = n;
    }

    public Double getNoise() {
        return noise;
    }

    public void setNoise(Double noise) {
        this.noise = noise;
    }
}