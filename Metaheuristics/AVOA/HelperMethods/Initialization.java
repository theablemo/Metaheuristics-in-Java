package Metaheuristics.AVOA.HelperMethods;

import java.util.Random;

public class Initialization {
    /**
     * Initialize the first population of search agents.
     *
     * @param N   The population size
     * @param dim The dimensionality of the search space
     * @param ub  The upper bounds vector
     * @param lb  The lower bounds vector
     * @return The initialized population matrix
     */
    public static double[][] initialization(int N, int dim, double[] ub, double[] lb) {
        int boundaryNo = ub.length; // Number of boundaries

        Random rand = new Random();
        double[][] X = new double[N][dim]; // Initialized population matrix

        // If the boundaries of all variables are equal and the user enters a single number for both ub and lb
        if (boundaryNo == 1) {
            double upperBound = ub[0];
            double lowerBound = lb[0];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < dim; j++) {
                    X[i][j] = rand.nextDouble() * (upperBound - lowerBound) + lowerBound;
                }
            }
        }

        // If each variable has a different lb and ub
        if (boundaryNo > 1) {
            for (int i = 0; i < dim; i++) {
                double upperBound = ub[i];
                double lowerBound = lb[i];
                for (int j = 0; j < N; j++) {
                    X[j][i] = rand.nextDouble() * (upperBound - lowerBound) + lowerBound;
                }
            }
        }

        return X;
    }
}
