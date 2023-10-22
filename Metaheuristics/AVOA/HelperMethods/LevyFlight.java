package Metaheuristics.AVOA.HelperMethods;

import org.apache.commons.math3.special.Gamma;
import java.util.Random;

public class LevyFlight {
    /**
     * Generates a Levy flight of length d.
     *
     * @param d The length of the Levy flight
     * @return The Levy flight as an array
     */
    public static double[] levyFlight(int d) {
        double beta = 3.0 / 2.0;

        // Calculate the sigma value
        double sigma = Math.pow((Gamma.gamma(1 + beta) * Math.sin(Math.PI * beta / 2) /
                (Gamma.gamma((1 + beta) / 2) * beta * Math.pow(2, (beta - 1) / 2))), 1 / beta);

        Random rand = new Random();

        // Generate random values for u and v
        double[] u = new double[d];
        double[] v = new double[d];
        for (int i = 0; i < d; i++) {
            u[i] = rand.nextGaussian() * sigma;
            v[i] = rand.nextGaussian();
        }

        // Calculate the step values
        double[] step = new double[d];
        for (int i = 0; i < d; i++) {
            step[i] = u[i] / Math.pow(Math.abs(v[i]), 1 / beta);
        }

        return step;
    }
}
