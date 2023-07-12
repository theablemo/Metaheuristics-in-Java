package Metaheuristics.AVOA;

public class ObjectiveFunction {
    /**
     * Evaluates the objective function for the given response time, wait time, and makespan.
     *
//     * @param responseTime The response time
//     * @param waitTime     The wait time
//     * @param makespan     The makespan
     * @return The weighted sum of response time, wait time, and makespan
     */
//    public static double evaluate(double responseTime, double waitTime, double makespan) {
    public static double evaluate(double[] felan){
        // Define the weights for each component
        double weightResponseTime = 0.5;
        double weightWaitTime = 0.3;
        double weightMakespan = 0.2;

        // Calculate the weighted sum
        return 2.0;
//        return weightResponseTime * responseTime +
//                weightWaitTime * waitTime +
//                weightMakespan * makespan;
    }
}
