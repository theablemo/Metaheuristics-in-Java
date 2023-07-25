package Metaheuristics.AVOA;

public class ObjectiveFunction {
    public static double evaluate(double[] sth){
        //Define your objective function here
        // Define the weights for each component
        double weightResponseTime = 0.5;
        double weightWaitTime = 0.3;
        double weightMakespan = 0.2;

        // Calculate the weighted sum
        return 0.0;
//        return weightResponseTime * responseTime +
//                weightWaitTime * waitTime +
//                weightMakespan * makespan;
    }
}
