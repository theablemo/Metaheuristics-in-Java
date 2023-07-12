package Metaheuristics.AVOA;

import java.util.Random;

import static Metaheuristics.AVOA.BoundaryCheck.boundaryCheck;
import static Metaheuristics.AVOA.Exploitation.exploitation;
import static Metaheuristics.AVOA.Exploration.exploration;
import static Metaheuristics.AVOA.Initialization.initialization;
import static Metaheuristics.AVOA.RandomSelection.randomSelect;

public class AVOA {
    /**
     * Runs the Artificial Vulture Optimization Algorithm (AVOA) to find the global optimum.
     *
     * @param pop_size       The population size.
     * @param max_iter       The maximum number of iterations.
     * @param lower_bound    The lower bound of the variables.
     * @param upper_bound    The upper bound of the variables.
     * @param variables_no   The number of variables.
     * @param fobj           The objective function.
     * @return               An array containing the best vulture 1 fitness, best vulture 1 position,
     *                       and the convergence curve of the algorithm.
     */
    public static Object[] AVOA(int pop_size, int max_iter, double[] lower_bound, double[] upper_bound,
                                         int variables_no, ObjectiveFunction fobj) {
        // Initialize Best_vulture1, Best_vulture2
        double[] Best_vulture1_X = new double[variables_no];
        double Best_vulture1_F = Double.POSITIVE_INFINITY;
        double[] Best_vulture2_X = new double[variables_no];
        double Best_vulture2_F = Double.POSITIVE_INFINITY;

        // Initialize the first random population of vultures
        double[][] X = initialization(pop_size, variables_no, upper_bound, lower_bound);

        // Controlling parameters
        double p1 = 0.6;
        double p2 = 0.4;
        double p3 = 0.6;
        double alpha = 0.8;
        double beta = 0.2;
        double gamma = 2.5;

        // Main loop
        int current_iter = 0; // Loop counter
        double[] convergence_curve = new double[max_iter];

        while (current_iter < max_iter) {
            for (int i = 0; i < X.length; i++) {
                // Calculate the fitness of the population
                double[] current_vulture_X = X[i];
                double current_vulture_F = fobj.evaluate(current_vulture_X);

                // Update the first best two vultures if needed
                if (current_vulture_F < Best_vulture1_F) {
                    Best_vulture1_F = current_vulture_F; // Update the first best vulture
                    System.arraycopy(current_vulture_X, 0, Best_vulture1_X, 0, variables_no);
                }
                if (current_vulture_F > Best_vulture1_F && current_vulture_F < Best_vulture2_F) {
                    Best_vulture2_F = current_vulture_F; // Update the second-best vulture
                    System.arraycopy(current_vulture_X, 0, Best_vulture2_X, 0, variables_no);
                }
            }

            Random rand = new Random();
            double a = rand.nextDouble() * 4 - 2;
            a *= (Math.pow(Math.sin(Math.PI / 2 * (current_iter / (double)max_iter)), gamma) +
                    Math.cos(Math.PI / 2 * (current_iter / (double)max_iter)) - 1);
            double P1 = (2 * rand.nextDouble() + 1) * (1 - (current_iter / (double)max_iter)) + a;

            // Update the location
            for (int i = 0; i < X.length; i++) {
                double[] current_vulture_X = X[i].clone(); // Pick the current vulture back to the population
                double F = P1 * (2 * rand.nextDouble() - 1);

                double[] random_vulture_X = randomSelect(Best_vulture1_X, Best_vulture2_X, alpha, beta);

                if (Math.abs(F) >= 1) { // Exploration
                    current_vulture_X = exploration(current_vulture_X, random_vulture_X, F, p1, upper_bound, lower_bound);
                } else if (Math.abs(F) < 1) { // Exploitation
                    current_vulture_X = exploitation(current_vulture_X, Best_vulture1_X, Best_vulture2_X, random_vulture_X, F, p2, p3, variables_no);
                }

                X[i] = current_vulture_X; // Place the current vulture back into the population
            }

            current_iter++;
            convergence_curve[current_iter - 1] = Best_vulture1_F;

            X = boundaryCheck(X, lower_bound, upper_bound);

            System.out.printf("In Iteration %d, best estimation of the global optimum is %4.4f \n", current_iter, Best_vulture1_F);
        }


        return new Object[]{Best_vulture1_F, Best_vulture1_X, convergence_curve};
    }
}
