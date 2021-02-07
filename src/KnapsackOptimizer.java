import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Project: Coursera Algorithms by Standford University: Greedy Algorithms, Minimum Spanning Trees, and Dynamic Programming.
 * Description: Week 4 Programming Assignment.
 *              In this programming problem and the next you'll code up the knapsack algorithm from lecture.
 *              Find the text file below in data folder.
 *
 *              knapsack1.txt
 *              This file describes a knapsack instance, and it has the following format:
 *
 *              [knapsack_size][number_of_items]
 *              [value_1] [weight_1]
 *              [value_2] [weight_2]
 *              ...
 *
 *              For example, the third line of the file is "50074 659",
 *              indicating that the second item has value 50074 and size 659, respectively.
 *
 *              You can assume that all numbers are positive.
 *              You should assume that item weights and the knapsack capacity are integers.
 *
 *              In the box below, type in the value of the optimal solution.
 *
 *              ADVICE: If you're not getting the correct answer, try debugging your algorithm using some small test cases.
 *              And then post them to the discussion forum!
 *
 * @author : Zitao He
 * @date : 2021-02-06 16:47
 **/

public class KnapsackOptimizer {

    int knapsackSize;
    int numItems;
    int[] value;
    int[] weight;
    int[][] solutionMatrix;
    int[][] fastSolutionMatrix;

    /**
     * Construct the optimizer using external txt file. File format can be founded in class description
     * @param fileInputName file name
     * @throws FileNotFoundException throws error if file not found
     */
    public KnapsackOptimizer(String fileInputName) throws FileNotFoundException{
        Scanner fileScanner;
        try{
            fileScanner = new Scanner(new File(fileInputName));
        }
        catch (IOException e) {
            throw new FileNotFoundException("Error: Input file is not found.");
        }
        String[] info = fileScanner.nextLine().split(" ");
        knapsackSize = Integer.parseInt(info[0]);
        numItems = Integer.parseInt(info[1]);
        value = new int[numItems];
        weight = new int[numItems];
        int i = 0;
        while(fileScanner.hasNextLine()){
            String[] valueAndWeight = fileScanner.nextLine().split(" ");
            value[i] = Integer.parseInt(valueAndWeight[0]);
            weight[i] = Integer.parseInt(valueAndWeight[1]);
            i++;
        }
        //solutionMatrix = new int[numItems+1][knapsackSize+1];
        fastSolutionMatrix = new int[2][knapsackSize+1];
    }

    /**
     * Implementing a dynamic programming algorithm to find the optimal solution.
     * A 2D array is used to store the local optimal result
     */
    public void runOptimization(){
        for (int i = 1; i <= numItems; i++){
            for (int x = 0; x <= knapsackSize; x++){
                if (weight[i-1] > x) {
                    solutionMatrix[i][x] = solutionMatrix[i-1][x];
                }
                else{
                    solutionMatrix[i][x] = Math.max(solutionMatrix[i-1][x], solutionMatrix[i-1][x-weight[i-1]]+value[i-1]);
                }
            }
        }

    }

    /**
     * Implementing a faster dynamic programming algorithm to find the optimal solution.
     * A 2 by n array is used to store the local optimal result. n is the number of items in optimizer.
     */
    public void runFastOptimization(){
        int i = 1;
        while (i <= numItems){
            for (int x = 0; x <= knapsackSize; x++){
                int[] temp = fastSolutionMatrix[0];
                if (weight[i-1] > x) {
                    fastSolutionMatrix[1][x] = fastSolutionMatrix[0][x];
                }
                else{
                    fastSolutionMatrix[1][x] = Math.max(fastSolutionMatrix[0][x], fastSolutionMatrix[0][x-weight[i-1]]+value[i-1]);
                }
                fastSolutionMatrix[0] = temp;
            }
            i++;
            for (int j = 0; j < fastSolutionMatrix[1].length; j ++){
                fastSolutionMatrix[0][j] = fastSolutionMatrix[1][j];
            }
        }
    }

    /**
     * Get the final optimal total value of optimal solution for 2D array algorithm
     * @return final optimal total value
     */
    public int getOptimalValueSum(){
        return solutionMatrix[numItems][knapsackSize];
    }

    /**
     * Get the final optimal total value of optimal solution for 2*n array algorithm
     * @return final optimal total value
     */
    public int getFastOptimalValueSum(){
        return fastSolutionMatrix[1][knapsackSize];
    }

    /**
     * Helper print method for debugging
     */
    public void printOptimizer(){
        System.out.println("Printing number of items: " + numItems);
        System.out.println("Printing knapsack size: " + knapsackSize);
        System.out.println("Printing values and weights: ");
        for (int i = 0; i<weight.length; i++){
            System.out.println(value[i] + ". " + weight[i]);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        //correct answer of knapsack-test.txt is 150 using 3 items:
        //(value 50, weight 75), (value 50, weight 59), (value 50, weight 56)
        //KnapsackOptimizer tester = new KnapsackOptimizer("data/knapsack-test1.txt");

        //correct answer of knapsack-test1.txt is 14
        //KnapsackOptimizer tester = new KnapsackOptimizer("data/knapsack-test2.txt");

        //KnapsackOptimizer tester = new KnapsackOptimizer("data/knapsack-test3.txt");

        //correct answer of knapsack1.txt is 2493893
        //KnapsackOptimizer tester = new KnapsackOptimizer("data/knapsack1.txt");

        //correct answer of knapsack_big.txt is 4243395
        KnapsackOptimizer tester = new KnapsackOptimizer("data/knapsack_big.txt");

        //tester.runOptimization();
        //System.out.println("Total value of optimal solution is: " + tester.getOptimalValueSum());

        tester.runFastOptimization();
        System.out.println("Total value of optimal solution is: " + tester.getFastOptimalValueSum());
    }
}
