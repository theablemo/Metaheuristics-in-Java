package Metaheuristics.OOA;

import Metaheuristics.ObjectiveFunction;

import java.util.Random;

public class OOA{

    private final int popSize;
    private final int maxIter;
    private final double[] lb;
    private final double[] ub;
    private final int varSize;

    private final ObjectiveFunction fitFunc;

    public OOA(ObjectiveFunction objectiveFunction, int popSize, int varSize, int maxIter, double[] lb, double[] ub){
        this.popSize = popSize;
        this.maxIter = maxIter;
        this.lb = lb;
        this.ub = ub;
        this.varSize = varSize;
        this.fitFunc = objectiveFunction;
    }

    public double[] run() {
        // Initialization
        double[][] X = new double[popSize][varSize];
        double[] fit = new double[popSize];

        Random random = new Random();

        for (int i = 0; i < popSize; i++) {
            for (int j = 0; j < varSize; j++) {
                X[i][j] = lb[j] + random.nextDouble() * (ub[j] - lb[j]);
            }
            fit[i] = fitFunc.evaluate(X[i]); // Calculate fitness for each agent
        }

        double[] bestPos = new double[varSize];
        double bestScore = Double.MAX_VALUE;

        // Main optimization loop
        for (int t = 0; t < maxIter; t++) {
            // Update the best proposed solution
            double fBest = Double.MAX_VALUE;
            int bLocation = -1;

            for (int i = 0; i < popSize; i++) {
                if (fit[i] < fBest) {
                    fBest = fit[i];
                    bLocation = i;
                }
            }

            if (t == 0 || fBest < bestScore) {
                bestScore = fBest;
                bestPos = X[bLocation].clone();
            }

            for (int i = 0; i < popSize; i++) {
                // Phase 1: Position Identification and Hunting the Fish (Exploration)
                int[] fishPosition = new int[0];
                for (int j = 0; j < popSize; j++) {
                    if (fit[j] < fit[i]) {
                        int[] temp = new int[fishPosition.length + 1];
                        System.arraycopy(fishPosition, 0, temp, 0, fishPosition.length);
                        temp[fishPosition.length] = j;
                        fishPosition = temp;
                    }
                }

                double[] selectedFish;
                if (fishPosition.length == 0) {
                    selectedFish = bestPos.clone();
                } else {
                    if (random.nextDouble() < 0.5) {
                        selectedFish = bestPos.clone();
                    } else {
                        int k = random.nextInt(fishPosition.length);
                        selectedFish = X[fishPosition[k]].clone();
                    }
                }

                int I = random.nextInt(2) + 1;
                double[] XNewP1 = new double[varSize];

                for (int j = 0; j < varSize; j++) {
                    XNewP1[j] = X[i][j] + random.nextDouble() * (selectedFish[j] - I * X[i][j]);
                    XNewP1[j] = Math.max(XNewP1[j], lb[j]);
                    XNewP1[j] = Math.min(XNewP1[j], ub[j]);
                }

                double fitNewP1 = fitFunc.evaluate(XNewP1);

                if (fitNewP1 < fit[i]) {
                    X[i] = XNewP1.clone();
                    fit[i] = fitNewP1;
                }

                // Phase 2: Carrying the Fish to the Suitable Position (Exploitation)
                double[] XNewP2 = new double[varSize];

                for (int j = 0; j < varSize; j++) {
                    XNewP2[j] = X[i][j] + (lb[j] + random.nextDouble() * (ub[j] - lb[j])) / (t + 1);
                    XNewP2[j] = Math.max(XNewP2[j], lb[j]);
                    XNewP2[j] = Math.min(XNewP2[j], ub[j]);
                }

                double fitNewP2 = fitFunc.evaluate(XNewP2);

                if (fitNewP2 < fit[i]) {
                    X[i] = XNewP2.clone();
                    fit[i] = fitNewP2;
                }
            }
        }

        double[] OOA_Curve = new double[maxIter];
        for (int t = 0; t < maxIter; t++) {
            OOA_Curve[t] = bestScore;
        }

//        // Output the results
//        System.out.println("Best Score: " + bestScore);
//        System.out.print("Best Position: [");
//        for (int i = 0; i < varSize; i++) {
//            System.out.print(bestPos[i]);
//            if (i < varSize - 1) {
//                System.out.print(", ");
//            }
//        }
//        System.out.println("]");
//
//        // OOA Curve
//        System.out.println("OOA Curve:");
//        for (int t = 0; t < maxIter; t++) {
//            System.out.println("Iteration " + (t + 1) + ": " + OOA_Curve[t]);
//        }
        return bestPos;
    }
}
