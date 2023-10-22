package Metaheuristics.GTO;

import Metaheuristics.ObjectiveFunction;

import java.util.Random;

public class GTO{

    private final int popSize;
    private final int maxIter;
    private final double[] lb;
    private final double[] ub;
    private final int varSize;

    private final ObjectiveFunction fitFunc;
    double[] silverback;
    double silverbackScore = Double.POSITIVE_INFINITY;

    public GTO(ObjectiveFunction objectiveFunction, int popSize, int varSize, int maxIter, double[] lb, double[] ub) {
        this.popSize = popSize;
        this.maxIter = maxIter;
        this.lb = lb;
        this.ub = ub;
        this.varSize = varSize;
        this.fitFunc = objectiveFunction;
    }

    public double[] run() {
        silverback = new double[varSize];
        // Initialize the first random population of Gorilla
        double[][] x = initialization(popSize, varSize, ub, lb);


        double[] popFit = new double[popSize];

        // Main loop
        for (int it = 1; it <= maxIter; it++) {
            double a = (Math.cos(2 * Math.random()) + 1) * (1 - (double) it / maxIter);
            double c = a * (2 * Math.random() - 1);

            // Exploration phase
            for (int i = 0; i < popSize; i++) {
                if (Math.random() < 0.03) {
                    for (int j = 0; j < varSize; j++) {
                        x[i][j] = (ub[j] - lb[j]) * Math.random() + lb[j];
                    }
                } else {
                    if (Math.random() >= 0.5) {
                        double[] z = new double[varSize];
                        for (int j = 0; j < varSize; j++) {
                            z[j] = Math.random() * (2 * a) - a;
                        }

                        double[] h = new double[varSize];
                        for (int j = 0; j < varSize; j++) {
                            h[j] = z[j] * x[i][j];
                        }

                        int randomIndex = new Random().nextInt(popSize);
                        double[] randX = x[randomIndex];

                        double[] gx = new double[varSize];
                        for (int j = 0; j < varSize; j++) {
                            gx[j] = (Math.random() - a) * randX[j] + c * h[j];
                        }

                        x[i] = gx;
                    } else {
                        double[] gx = new double[varSize];
                        for (int j = 0; j < varSize; j++) {
                            gx[j] = x[i][j] - c * (c * (x[i][j] - x[new Random().nextInt(popSize)][j]) + Math.random() * (x[i][j] - x[new Random().nextInt(popSize)][j]));
                        }
                        x[i] = gx;
                    }
                }
            }

            boundaryCheck(x, lb, ub);

            // Group formation operation
            for (int i = 0; i < popSize; i++) {
                double newFit = fitFunc.evaluate(x[i]);
                popFit[i] = newFit;

                if (newFit < silverbackScore) {
                    silverbackScore = newFit;
                    silverback = x[i].clone();
                }
            }

            // Exploitation phase
            for (int i = 0; i < popSize; i++) {
                if (a >= 0.8) {
                    double g = Math.pow(2, c);
                    double[] delta = new double[varSize];
                    for (int j = 0; j < varSize; j++) {
                        delta[j] = Math.pow(Math.abs(mean(x, j)), g) * Math.pow(1.0 / varSize, 1.0 / g);
                    }

                    double[] gx = new double[varSize];
                    for (int j = 0; j < varSize; j++) {
                        gx[j] = c * delta[j] * (x[i][j] - silverback[j]) + x[i][j];
                    }

                    x[i] = gx;
                } else {
                    double[] gx = new double[varSize];
                    for (int j = 0; j < varSize; j++) {
                        if (Math.random() >= 0.5) {
                            gx[j] = silverback[j] - (silverback[j] * (2 * Math.random() - 1) - x[i][j] * (2 * Math.random() - 1)) * (3 * h() / 10);
                        } else {
                            gx[j] = silverback[j] - (silverback[j] * (2 * Math.random() - 1) - x[i][j] * (2 * Math.random() - 1)) * (1 / h());
                        }
                    }
                    x[i] = gx;
                }
            }

            boundaryCheck(x, lb, ub);

            // Group formation operation
            for (int i = 0; i < popSize; i++) {
                double newFit = fitFunc.evaluate(x[i]);
                popFit[i] = newFit;

                if (newFit < silverbackScore) {
                    silverbackScore = newFit;
                    silverback = x[i].clone();
                }
            }

            System.out.printf("In Iteration %d, best estimation of the global optimum is %4.4f\n", it, silverbackScore);
        }

        return silverback;
    }

    private static double[][] initialization(int N, int dim, double[] ub, double[] lb) {
        double[][] X = new double[N][dim];
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < dim; j++) {
                X[i][j] = random.nextDouble() * (ub[j] - lb[j]) + lb[j];
            }
        }
        return X;
    }

    private static void boundaryCheck(double[][] X, double[] lb, double[] ub) {
        for (int i = 0; i < X.length; i++) {
            for (int j = 0; j < X[i].length; j++) {
                if (X[i][j] < lb[j]) {
                    X[i][j] = lb[j];
                } else if (X[i][j] > ub[j]) {
                    X[i][j] = ub[j];
                }
            }
        }
    }

    private static double mean(double[][] X, int j) {
        double sum = 0;
        for (double[] x : X) {
            sum += x[j];
        }
        return sum / X.length;
    }

    private static double h() {
        if (Math.random() >= 0.5) {
            return 1.0;
        } else {
            return 1.0 / 10.0;
        }
    }
}
