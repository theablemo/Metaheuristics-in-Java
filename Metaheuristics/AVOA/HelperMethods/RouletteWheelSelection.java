package Metaheuristics.AVOA.HelperMethods;

import java.util.Random;

public class RouletteWheelSelection {
    /**
     * Performs roulette wheel selection based on the given probability distribution.
     *
     * @param x The probability distribution array
     * @return The selected index
     */
    public static int rouletteWheelSelection(double[] x) {
        Random rand = new Random();
        double cumulativeSum = 0.0;
        int selectedIndex = -1;

        // Calculate the cumulative sum of the probability distribution array
        double[] cumulativeSumArray = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            cumulativeSum += x[i];
            cumulativeSumArray[i] = cumulativeSum;
        }

        // Perform the roulette wheel selection
        double randomValue = rand.nextDouble();
        for (int i = 0; i < x.length; i++) {
            if (randomValue <= cumulativeSumArray[i]) {
                selectedIndex = i;
                break;
            }
        }

        return selectedIndex;
    }
}
