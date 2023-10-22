package Metaheuristics.AVOA;
import Metaheuristics.ObjectiveFunction;

import java.util.Arrays;

public class AVOAExample {

    public static void main(String[] args) {
        int pop_size = 30;
        int max_iter = 100;

        // Define your objective function's details here
        ObjectiveFunction fobj = new ObjectiveFunction();
        int variables_no = 10;
        double[] lower_bound = new double[variables_no];
        Arrays.fill(lower_bound, -100);
        double[] upper_bound = new double[variables_no];
        Arrays.fill(upper_bound, 100);

        AVOA avoa = new AVOA(pop_size, max_iter, lower_bound, upper_bound, variables_no, fobj);

        double[] result = avoa.run();
//        double Best_vulture1_F = (double) result[0];
        double[] Best_vulture1_X = result;
//        double[] convergence_curve = (double[]) result[2];

        // Best optimal values for the decision variables
        // parallelcoords(Best_vulture1_X)
        System.out.println("Decision variables:");
        for (double value : Best_vulture1_X) {
            System.out.println(value);
        }

        // Best convergence curve
        // plot(convergence_curve);
        System.out.println("\nConvergence curve of AVOA:");
//        for (double value : convergence_curve) {
//            System.out.println(value);
//        }
    }

}
