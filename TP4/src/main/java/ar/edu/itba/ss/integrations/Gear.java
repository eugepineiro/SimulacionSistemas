package ar.edu.itba.ss.integrations;
import ar.edu.itba.ss.models.AcceleratedParticle;

import java.util.List;
import java.util.stream.LongStream;

public class Gear implements Integration {

    @Override
    public AcceleratedParticle update(List<AcceleratedParticle> allParticles, AcceleratedParticle current, AcceleratedParticle previous, double dt) {

        int order = 2;
        double[] nextPredictedDerivativesX = new double[order+1];
        double[] nextPredictedDerivativesY = new double[order+1];
        double[] taylorCoeffs = calculateTaylorPolynomialCoeffs(dt, order);

        // Predict

        for(int i = 0; i <= order; i++ ){
            for(int j = i; j <= order; j++){
                nextPredictedDerivativesX[i] += current.getPositionDerivativeX(j, allParticles) * taylorCoeffs[j-i];
                nextPredictedDerivativesY[i] += current.getPositionDerivativeY(j, allParticles) * taylorCoeffs[j-i];
            }
        }

        // Evaluate

        double nextAccelerationWithPredictedX, nextAccelerationWithPredictedY;

        AcceleratedParticle predictedNext = current.clone();
        predictedNext.setX(nextPredictedDerivativesX[0]);
        predictedNext.setVx(nextPredictedDerivativesX[1]);
        predictedNext.setY(nextPredictedDerivativesY[0]);
        predictedNext.setVy(nextPredictedDerivativesY[1]);

        nextAccelerationWithPredictedX = predictedNext.getPositionDerivativeX(2, allParticles);  //etPositionDerivativeX(2, nextPredictedDerivativesX[0], nextPredictedDerivativesX[1], mass);
        nextAccelerationWithPredictedY = predictedNext.getPositionDerivativeY(2, allParticles); // calculateAcceleration.apply(nextPredictedDerivativesY[0], nextPredictedDerivativesY[1], mass);

        double deltaAccelerationX, deltaAccelerationY;

        deltaAccelerationX = nextAccelerationWithPredictedX - nextPredictedDerivativesX[2];
        deltaAccelerationY = nextAccelerationWithPredictedY - nextPredictedDerivativesY[2];

        double deltaR2X, deltaR2Y;

        deltaR2X = deltaAccelerationX * Math.pow(dt,2) / 2;
        deltaR2Y = deltaAccelerationY * Math.pow(dt,2) / 2;

        // Correct

        double[] nextCorrectedDerivativesX = new double[order+1];
        double[] nextCorrectedDerivativesY = new double[order+1];

        double[] gearCoeffs = {0, 1, 1};

        for (int i = 0; i <= order; i++) {
            nextCorrectedDerivativesX[i] = nextPredictedDerivativesX[i] + gearCoeffs[i] * deltaR2X / taylorCoeffs[i];
            nextCorrectedDerivativesY[i] = nextPredictedDerivativesY[i] + gearCoeffs[i] * deltaR2Y / taylorCoeffs[i];
        }

        AcceleratedParticle next = current.clone();

        next.setX(nextCorrectedDerivativesX[0]);
        next.setY(nextCorrectedDerivativesY[0]);

        next.setVx(nextCorrectedDerivativesX[1]);
        next.setVy(nextCorrectedDerivativesY[1]);

        next.setForceX(current.getMass() * nextCorrectedDerivativesX[2]);
        next.setForceY(current.getMass() * nextCorrectedDerivativesX[2]);

        return next;
    }

    private double[] calculateTaylorPolynomialCoeffs(double dt, int order) {
        double[] taylorCoeffs = new double[order+1];
        for(int i = 0; i <= order; i++){
            taylorCoeffs[i] = Math.pow(dt, i)/factorial(i);
        }
        return taylorCoeffs;
    }

    private long factorial(int n) {
        return LongStream.rangeClosed(1, n)
                .reduce(1, (long x, long y) -> x * y);
    }

}
