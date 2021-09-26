package ar.edu.itba.ss.integrations;
import ar.edu.itba.ss.models.AcceleratedParticle;
import ar.edu.itba.ss.models.TriFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class Gear implements Integration {

    @Override
    public AcceleratedParticle update(AcceleratedParticle current, AcceleratedParticle previous, double dt, TriFunction<Double, Double, Double, Double> calculateAcceleration) {

        int order = 5;
        List<Double> nextPredictedPositions = new ArrayList<>();
        double derivatives[][] = calculateDerivatives(current);
        double taylor_coeffs[] = calculateTaylorPolynomialCoeffs(dt, order);

        // Predict
        for(int i=0; i < order+1; i++ ){

        }

        // Evaluate

        // Correct

        return current.clone();
    }

    private double[][] calculateDerivatives(AcceleratedParticle current){
        double mass = current.getMass();
        double derivatives[][] = {
            {current.getX(), current.getVx(), current.getForceX()/mass, 0, 0}, // x
            {current.getY(), current.getVy(), current.getForceY()/mass, 0, 0}, // Y
        };

        return derivatives;
    }

    private double[] calculateTaylorPolynomialCoeffs(double dt, int order) {
        double taylor_coeffs[] = new double[order+1];
        for(int i = 0; i <= order; i++){
            taylor_coeffs[i] = Math.pow(dt, i)/factorial(i);
        }
        return taylor_coeffs;
    }

    private long factorial(int n) {
        return LongStream.rangeClosed(1, n)
                .reduce(1, (long x, long y) -> x * y);
    }

}
