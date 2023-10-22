package Metaheuristics.AVOA.HelperMethods;

import java.util.Random;

public class Exploration {
    /**
     * Updates the current vulture position based on exploration.
     *
     * @param current_vulture_X  The current vulture position array to be updated.
     * @param random_vulture_X   The random vulture position array.
     * @param F                  The exploration factor.
     * @param p1                 The probability threshold for exploration.
     * @param upper_bound        The upper bound of the vulture position range.
     * @param lower_bound        The lower bound of the vulture position range.
     * @return                   The updated current vulture position array.
     */
    public static double[] exploration(double[] current_vulture_X, double[] random_vulture_X, double F, double p1, double[] upper_bound, double[] lower_bound) {
        Random rand = new Random();

        // Check if a randomly generated value is less than p1
        if (rand.nextDouble() < p1) {
            // Exploration based on random vulture position
            for (int i = 0; i < current_vulture_X.length; i++) {
                double randomFactor = 2 * rand.nextDouble(); // Generate a random factor between 0 and 2
                // Update the current vulture position using the random factor and F
                current_vulture_X[i] = random_vulture_X[i] - Math.abs((randomFactor * random_vulture_X[i]) - current_vulture_X[i]) * F;
            }
        } else {
            // Exploration based on random values within bounds
            for (int i = 0; i < current_vulture_X.length; i++) {
                double randomFactor = rand.nextDouble(); // Generate a random factor between 0 and 1
                double range = upper_bound[i] - lower_bound[i]; // Calculate the range for the current dimension
                // Update the current vulture position using the random factor, F, upper bound, and lower bound
                current_vulture_X[i] = random_vulture_X[i] - F + randomFactor * (range * rand.nextDouble() + lower_bound[i]);
            }
        }

        return current_vulture_X;
    }
}