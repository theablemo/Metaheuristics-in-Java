package Metaheuristics.AVOA;

import Metaheuristics.ObjectiveFunction;

import java.util.Random;

import static Metaheuristics.AVOA.HelperMethods.BoundaryCheck.boundaryCheck;
import static Metaheuristics.AVOA.HelperMethods.Exploitation.exploitation;
import static Metaheuristics.AVOA.HelperMethods.Exploration.exploration;
import static Metaheuristics.AVOA.HelperMethods.Initialization.initialization;
import static Metaheuristics.AVOA.HelperMethods.RandomSelection.randomSelect;

public class AVOA {

    private final int varSize;
    private final int popSize;
    private final int maxIter;
    private final ObjectiveFunction fitFunc;
    private final double[] lb;
    private final double[] ub;

    public AVOA(int popSize, int maxIter, double[] lb, double[] ub,
                int varSize, ObjectiveFunction fitFunc){
        this.popSize = popSize;
        this.maxIter = maxIter;
        this.lb = lb;
        this.ub = ub;
        this.varSize = varSize;
        this.fitFunc = fitFunc;
    }
    public double[] run() {
        // Initialize Best_vulture1, Best_vulture2
        double[] Best_vulture1_X = new double[varSize];
        double Best_vulture1_F = Double.POSITIVE_INFINITY;
        double[] Best_vulture2_X = new double[varSize];
        double Best_vulture2_F = Double.POSITIVE_INFINITY;

        // Initialize the first random population of vultures
        double[][] X = initialization(popSize, varSize, ub, lb);

        // Controlling parameters
        double p1 = 0.6;
        double p2 = 0.4;
        double p3 = 0.6;
        double alpha = 0.8;
        double beta = 0.2;
        double gamma = 2.5;

        // Main loop
        int current_iter = 0; // Loop counter
        double[] convergence_curve = new double[maxIter];

        while (current_iter < maxIter) {
            for (int i = 0; i < X.length; i++) {
                // Calculate the fitness of the population
                double[] current_vulture_X = X[i];
                double current_vulture_F = fitFunc.evaluate(current_vulture_X);

                // Update the first best two vultures if needed
                if (current_vulture_F < Best_vulture1_F) {
                    Best_vulture1_F = current_vulture_F; // Update the first best vulture
                    System.arraycopy(current_vulture_X, 0, Best_vulture1_X, 0, varSize);
                }
                if (current_vulture_F > Best_vulture1_F && current_vulture_F < Best_vulture2_F) {
                    Best_vulture2_F = current_vulture_F; // Update the second-best vulture
                    System.arraycopy(current_vulture_X, 0, Best_vulture2_X, 0, varSize);
                }
            }

            Random rand = new Random();
            double a = rand.nextDouble() * 4 - 2;
            a *= (Math.pow(Math.sin(Math.PI / 2 * (current_iter / (double)maxIter)), gamma) +
                    Math.cos(Math.PI / 2 * (current_iter / (double)maxIter)) - 1);
            double P1 = (2 * rand.nextDouble() + 1) * (1 - (current_iter / (double)maxIter)) + a;

            // Update the location
            for (int i = 0; i < X.length; i++) {
                double[] current_vulture_X = X[i].clone(); // Pick the current vulture back to the population
                double F = P1 * (2 * rand.nextDouble() - 1);

                double[] random_vulture_X = randomSelect(Best_vulture1_X, Best_vulture2_X, alpha, beta);

                if (Math.abs(F) >= 1) { // Exploration
                    current_vulture_X = exploration(current_vulture_X, random_vulture_X, F, p1, ub, lb);
                } else if (Math.abs(F) < 1) { // Exploitation
                    current_vulture_X = exploitation(current_vulture_X, Best_vulture1_X, Best_vulture2_X, random_vulture_X, F, p2, p3, varSize);
                }

                X[i] = current_vulture_X; // Place the current vulture back into the population
            }

            current_iter++;
            convergence_curve[current_iter - 1] = Best_vulture1_F;

            X = boundaryCheck(X, lb, ub);

            System.out.printf("In Iteration %d, best estimation of the global optimum is %4.4f \n", current_iter, Best_vulture1_F);
        }


//        return new Object[]{Best_vulture1_F, Best_vulture1_X, convergence_curve};
        return Best_vulture1_X;
    }
}
