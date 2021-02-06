import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Project: Coursera Algorithms by Standford University: Greedy Algorithms, Minimum Spanning Trees, and Dynamic Programming.
 * Description: Week 3 Programming Assignment.
 *              In this programming problem you'll code up the dynamic programming algorithm for
 *              computing a maximum-weight independent set of a path graph.
 *
 *              Find the text file in data folder.
 *              mwis.txt
 *              This file describes the weights of the vertices in a path graph
 *              (with the weights listed in the order in which vertices appear in the path).
 *              It has the following format:
 *
 *              [number_of_vertices]
 *              [weight of first vertex]
 *              [weight of second vertex]
 *              ...
 *
 *              For example, the third line of the file is "6395702,"
 *              indicating that the weight of the second vertex of the graph is 6395702.

 *              Your task in this problem is to run the dynamic programming algorithm
 *              (and the reconstruction procedure) from lecture on this data set.
 *              The question is: of the vertices 1, 2, 3, 4, 17, 117, 517, and 997,
 *              which ones belong to the maximum-weight independent set?
 *              (By "vertex 1" we mean the first vertex of the graph---there is no vertex 0.)
 *              In the box below, enter a 8-bit string, where the ith bit should be 1
 *              if the ith of these 8 vertices is in the maximum-weight independent set, and 0 otherwise.
 *              For example, if you think that the vertices 1, 4, 17, and 517 are
 *              in the maximum-weight independent set and the other four vertices are not,
 *              then you should enter the string 10011010 in the box below.
 *
 * @author : Zitao He
 * @date : 2021-02-06 10:27
 **/
public class IndependentSetFinder {
    int numVertices;
    int[] weights;
    int[] maxWIS; // i_th value is the maximum weight of a independent set in subgraph that has i_th first vertices

    /**
     * Construct the path graph using external txt file. File format can be found in class description
     * @param fileInputName file name to be imported
     * @throws FileNotFoundException throws error if file not found
     */
    public IndependentSetFinder(String fileInputName) throws FileNotFoundException {
        Scanner fileScanner;
        try {
            fileScanner = new Scanner(new File(fileInputName));
        } catch (IOException e) {
            throw new FileNotFoundException("Error: Input file is not found.");
        }
        numVertices = Integer.parseInt(fileScanner.nextLine());
        weights = new int[numVertices];
        maxWIS = new int[numVertices+1];
        int index = 0;
        while (fileScanner.hasNextLine()) {
            int currWeight = Integer.parseInt(fileScanner.nextLine());
            weights[index++] = currWeight;
        }
    }

    /**
     * Run dynamic programming algorithm to find the max weight of independent set in the path graph
     */
    public void runFindWIS(){
        maxWIS[0] = 0;
        maxWIS[1] = weights[0];
        for (int i = 2; i <= numVertices; i++){
            maxWIS[i] = Math.max(maxWIS[i-1], maxWIS[i-2] + weights[i-1]);
        }
    }

    /**
     * Reconstruct the independent set
     * @return the array list that contains the ID of each vertex that is in the max weighted independent set
     */
    public ArrayList<Integer> reconstructIS(){
        ArrayList<Integer> is = new ArrayList<>();
        int i = numVertices;
        while(i >= 1){
            if (i == 1){
                is.add(i);
                break;
            }
            if (maxWIS[i-1] >= maxWIS[i-2] + weights[i-1]){
                i -= 1;
            }
            else{
                is.add(i);
                i -= 2;
            }
        }
        return is;
    }

    /**
     * Helper print method for debugging
     */
    public void printMaxWis(){
        System.out.println("Max weight of independent set is: " + maxWIS[maxWIS.length-1]);
    }

    /**
     * Helper print method for debugging
     */
    public void printWeights(){
        System.out.println("Printing number of weight: ");
        System.out.println(weights.length);
        System.out.println("Printing weight array: ");
        for (int weight : weights) {
            System.out.println(weight);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        //mwis-test1.txt
        //Max sum: 2616. Chosen points (position): [2, 4, 6, 8, 10]
        //IndependentSetFinder tester = new IndependentSetFinder("data/mwis-test1.txt");

        //mwis-test2.txt
        //Max sum: 2533. Chosen points (position): [1, 3, 6, 9]
        //IndependentSetFinder tester = new IndependentSetFinder("data/mwis-test2.txt");

        IndependentSetFinder tester = new IndependentSetFinder("data/mwis.txt");
        tester.runFindWIS();
        ArrayList<Integer> is = tester.reconstructIS();
        int[] vertices = {1, 2, 3, 4, 17, 117, 517, 997};
        String result = "";
        for (int vertex : vertices) {
            if (is.contains(vertex)) {
                result += "1";
            } else {
                result += "0";
            }
        }
        System.out.println("Final result is: " + result);
        //correct answer is 10100110
    }
}
