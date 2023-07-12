package Metaheuristics.AVOA;

public class BoundaryCheck {
    /**
     * Performs boundary check on the matrix X.
     *
     * @param X  The input matrix
     * @param lb The lower bounds vector
     * @param ub The upper bounds vector
     * @return The modified matrix after boundary check
     */
    public static double[][] boundaryCheck(double[][] X, double[] lb, double[] ub) {
        int numRows = X.length;
        int numCols = X[0].length;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                boolean FU = X[i][j] > ub[j];  // Check if element exceeds the upper bound
                boolean FL = X[i][j] < lb[j];  // Check if element is below the lower bound

                // Perform the boundary check and assign the appropriate value to X[i][j]
                X[i][j] = (X[i][j] * ((!(FU || FL)) ? 1 : 0)) + ub[j] * (FU ? 1 : 0) + lb[j] * (FL ? 1 : 0);
            }
        }

        return X;
    }
}
