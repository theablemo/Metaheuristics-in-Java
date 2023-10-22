package Metaheuristics.GEO;

import Metaheuristics.ObjectiveFunction;

import java.util.Arrays;
import java.util.Random;

public class GEO {

    private final int varSize;
    private final int popSize;
    private final int maxIter;
    private final ObjectiveFunction fitFunc;
    private final double[] lb;
    private final double[] ub;

    public static double[] vecNorm(double[][] A, int p, int dim) {
        int numRows = A.length;
        int numCols = A[0].length;

        double[] N = new double[dim == 1 ? numRows : numCols];

        if (dim == 1) {
            for (int i = 0; i < numRows; i++) {
                double sum = 0;
                for (int j = 0; j < numCols; j++) {
                    sum += Math.pow(A[i][j], p);
                }
                N[i] = Math.pow(sum, 1.0 / p);
            }
        } else if (dim == 2) {
            for (int j = 0; j < numCols; j++) {
                double sum = 0;
                for (int i = 0; i < numRows; i++) {
                    sum += Math.pow(A[i][j], p);
                }
                N[j] = Math.pow(sum, 1.0 / p);
            }
        } else {
            throw new IllegalArgumentException("Invalid dimension. Use 1 for rows or 2 for columns.");
        }

        return N;
    }

    public static double[] linspace(double start, double end, int numPoints) {
        double[] result = new double[numPoints];
        double step = (end - start) / (numPoints - 1);
        for (int i = 0; i < numPoints; i++) {
            result[i] = start + i * step;
        }
        return result;
    }

    public static int[] shuffleArray(int length) {
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = i;
        }
        Random rnd = new Random();
        for (int i = length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }

    public static double sum(double[] arr) {
        double sum = 0;
        for (double num : arr) {
            sum += num;
        }
        return sum;
    }

    public static double[][] divide(double[][] arr, double[] divisor) {
        double[][] result = new double[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                result[i][j] = arr[i][j] / divisor[i];
            }
        }
        return result;
    }

    public static double min(double[] arr) {
        double minValue = arr[0];
        for (double value : arr) {
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }

    public static int argmin(double[] arr) {
        int index = 0;
        double minValue = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < minValue) {
                index = i;
                minValue = arr[i];
            }
        }
        return index;
    }

    public static int datasample(double[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Input array must not be empty.");
        }

        Random random = new Random();
        int index = random.nextInt(array.length);
        return index;
    }

    public static double vecNorm2(double[] vector) {
        double sum = 0;
        for (double value : vector) {
            sum += Math.pow(value, 2);
        }
        return Math.sqrt(sum);
    }

    double[] attackPropensity = new double[]{0.1, 0.9};
    double[] cruisePropensity = new double[]{0.1, 0.9};

    public double[] getAttackPropensity() {
        return attackPropensity;
    }

    public double[] getCruisePropensity() {
        return cruisePropensity;
    }

    public GEO(ObjectiveFunction objectiveFunction, int popSize, int varSize, int maxIter, double[] lb, double[] ub){
        this.popSize = popSize;
        this.maxIter = maxIter;
        this.lb = lb;
        this.ub = ub;
        this.varSize = varSize;
        this.fitFunc = objectiveFunction;
    }

    public double[] run() {
        int populationSize = popSize;
        int maxIterations = maxIter;
        double[] convergenceCurve = new double[maxIterations];

        double[][] x = new double[populationSize][varSize];
        Random random = new Random();

        // Initialization
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < varSize; j++) {
                x[i][j] = lb[j] + random.nextDouble() * (ub[j] - lb[j]);
            }
        }

//        double[] fitnessScores = fun.evaluate(x);
        double[] fitnessScores = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            fitnessScores[i] = fitFunc.evaluate(x[i]);
        }
        double[] flockMemoryF = fitnessScores.clone();
        double[][] flockMemoryX = x.clone();

        double[] attackPropensity = linspace(getAttackPropensity()[0], getAttackPropensity()[1], maxIterations);
        double[] cruisePropensity = linspace(getCruisePropensity()[0], getCruisePropensity()[1], maxIterations);

        // Main loop
        for (int currentIteration = 0; currentIteration < maxIterations; currentIteration++) {
            // Prey selection (one-to-one mapping)
            int[] destinationEagle = shuffleArray(populationSize);

            // Calculate AttackVectorInitial
            double[][] attackVectorInitial = new double[populationSize][varSize];
            for (int i = 0; i < populationSize; i++) {
                for (int j = 0; j < varSize; j++) {
                    attackVectorInitial[i][j] = flockMemoryX[destinationEagle[i]][j] - x[i][j];
                }
            }

            // Calculate Radius
            double[] radius = vecNorm(attackVectorInitial, 2, 1);

            // Determine converged and unconverged eagles
            boolean[] convergedEagles = new boolean[populationSize];
            boolean[] unconvergedEagles = new boolean[populationSize];
            for (int i = 0; i < populationSize; i++) {
                convergedEagles[i] = sum(attackVectorInitial[i]) == 0;
                unconvergedEagles[i] = !convergedEagles[i];
            }

            // Initialize CruiseVectorInitial
            double[][] cruiseVectorInitial = new double[populationSize][varSize];
            for (int i = 0; i < populationSize; i++) {
                for (int j = 0; j < varSize; j++) {
                    cruiseVectorInitial[i][j] = 2 * random.nextDouble() - 1; // [-1, 1]
                }
            }

            // Correct vectors for converged eagles
            for (int i = 0; i < populationSize; i++) {
                if (convergedEagles[i]) {
                    Arrays.fill(attackVectorInitial[i], 0);
                    Arrays.fill(cruiseVectorInitial[i], 0);
                }
            }

            // Determine constrained and free variables
            for (int i1 = 0; i1 < populationSize; i1++) {
                if (unconvergedEagles[i1]) {
                    boolean[] vConstrained = new boolean[varSize];
                    int idx = datasample(attackVectorInitial[i1]);
                    vConstrained[idx] = true;
                    boolean[] vFree = new boolean[varSize];
                    for (int j = 0; j < varSize; j++) {
                        if (!vConstrained[j]) {
                            vFree[j] = true;
                        }
                    }
                    double sum = 0;
                    for (int j = 0; j < varSize; j++) {
                        if (vFree[j]) {
                            sum += attackVectorInitial[i1][j] * cruiseVectorInitial[i1][j];
                        }
                    }
                    for (int j = 0; j < varSize; j++) {
                        if (vConstrained[j]) {
                            cruiseVectorInitial[i1][j] = -sum / attackVectorInitial[i1][j]; // (Eq. 4 in paper)
                        }
                    }
                }
            }

            // Calculate unit vectors
//            double[][] attackVectorUnit = divide(attackVectorInitial, vecNorm(attackVectorInitial, 2, 2));
//            double[][] cruiseVectorUnit = divide(cruiseVectorInitial, vecNorm(cruiseVectorInitial, 2, 2));
            double[][] attackVectorUnit = new double[populationSize][varSize];
            double[][] cruiseVectorUnit = new double[populationSize][varSize];
            for (int i = 0; i < populationSize; i++) {
                double normAttackVectorInitial = vecNorm2(attackVectorInitial[i]);
                double normCruiseVectorInitial = vecNorm2(cruiseVectorInitial[i]);
                for (int j = 0; j < varSize; j++) {
                    attackVectorUnit[i][j] = attackVectorInitial[i][j] / normAttackVectorInitial;
                    cruiseVectorUnit[i][j] = cruiseVectorInitial[i][j] / normCruiseVectorInitial;
                }
            }

            // Correct vectors for converged eagles
            for (int i = 0; i < populationSize; i++) {
                if (convergedEagles[i]) {
                    Arrays.fill(attackVectorUnit[i], 0);
                    Arrays.fill(cruiseVectorUnit[i], 0);
                }
            }

            // Calculate movement vectors
            double[] attackVector = new double[populationSize];
            double[] cruiseVector = new double[populationSize];
            double[] stepVector = new double[populationSize];
            for (int i = 0; i < populationSize; i++) {
                attackVector[i] = random.nextDouble() * attackPropensity[currentIteration] * radius[i] * attackVectorUnit[i][0]; // First term of Eq. 6 in paper
                cruiseVector[i] = random.nextDouble() * cruisePropensity[currentIteration] * radius[i] * cruiseVectorUnit[i][0]; // Second term of Eq. 6 in paper
                stepVector[i] = attackVector[i] + cruiseVector[i];
            }

            // Calculate new x
            for (int i = 0; i < populationSize; i++) {
                for (int j = 0; j < varSize; j++) {
                    x[i][j] += stepVector[i];
                }
            }

            // Enforce bounds
            for (int i = 0; i < populationSize; i++) {
                for (int j = 0; j < varSize; j++) {
                    x[i][j] = Math.max(lb[j], Math.min(ub[j], x[i][j]));
                }
            }

            // Calculate fitness
            for (int i = 0; i < x.length; i++) {
                fitnessScores[i] = fitFunc.evaluate(x[i]);
            }
            // Update memory
            for (int i = 0; i < populationSize; i++) {
                if (fitnessScores[i] < flockMemoryF[i]) {
                    flockMemoryF[i] = fitnessScores[i];
                    for (int j = 0; j < varSize; j++) {
                        flockMemoryX[i][j] = x[i][j];
                    }
                }
            }

            // Update convergence curve
            convergenceCurve[currentIteration] = min(flockMemoryF);
        }

        // Return values
        int fvalIndex = argmin(flockMemoryF);
        double[] fval = flockMemoryF.clone();
        double[] result = new double[varSize];
        for (int j = 0; j < varSize; j++) {
            result[j] = flockMemoryX[fvalIndex][j];
        }

        return result;
    }

}