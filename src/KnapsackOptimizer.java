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
        solutionMatrix = new int[numItems+1][knapsackSize+1];
    }
    public void runOptimization(){
        for (int i = 0; i < solutionMatrix[0].length; i++){
            solutionMatrix[0][i] = 0;
        }
        //System.out.println(Arrays.deepToString(solutionMatrix));
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

    public int getOptimalValueSum(){
        return solutionMatrix[numItems][knapsackSize];
    }
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

        //correct answer of knapsack1.txt is 2493893
        KnapsackOptimizer tester = new KnapsackOptimizer("data/knapsack1.txt");

        //tester.printOptimizer();
        tester.runOptimization();
        System.out.println("Total value of optimal solution is: " + tester.getOptimalValueSum());

    }

}
