package Metaheuristics.AVOA;

import static Metaheuristics.AVOA.RouletteWheelSelection.rouletteWheelSelection;

public class RandomSelection {
    /**
     * Performs random selection between two vulture positions based on given probabilities.
     *
     * @param Best_vulture1_X The position of the first best vulture
     * @param Best_vulture2_X The position of the second best vulture
     * @param alpha           The probability of selecting the first best vulture
     * @param beta            The probability of selecting the second best vulture
     * @return The randomly selected vulture position
     */
    public static double[] randomSelect(double[] Best_vulture1_X, double[] Best_vulture2_X, double alpha, double beta) {
        double[] probabilities = {alpha, beta};

        int selectedIndex = rouletteWheelSelection(probabilities);

        // Random selection based on the chosen index
        double[] random_vulture_X;
        if (selectedIndex == 0) {
            random_vulture_X = Best_vulture1_X;
        } else {
            random_vulture_X = Best_vulture2_X;
        }

        return random_vulture_X;
    }
}
