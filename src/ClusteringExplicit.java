import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Project: Coursera Algorithms by Standford University: Greedy Algorithms, Minimum Spanning Trees, and Dynamic Programming.
 * Description: Week 2 Programming Assignment.
 *              In this programming problem and the next you'll code up the clustering algorithm from lecture for computing a max-spacing kk-clustering.
 *
 *              Find the text file in data folder.
 *              clustering1.txt
 *              This file describes a distance function (equivalently, a complete graph with edge costs).
 *              It has the following format:
 *
 *              [number_of_nodes]
 *              [edge 1 node 1] [edge 1 node 2] [edge 1 cost]
 *              [edge 2 node 1] [edge 2 node 2] [edge 2 cost]
 *              ...
 *
 *              There is one edge (i,j) for each choice of 1 <= i <= j <= n, where n is the number of nodes.
 *              For example, the third line of the file is "1 3 5250",
 *              indicating that the distance between nodes 1 and 3 (equivalently, the cost of the edge (1,3)) is 5250.
 *              You can assume that distances are positive, but you should NOT assume that they are distinct.
 *
 *              Your task in this problem is to run the clustering algorithm from lecture on this data set,
 *              where the target number kk of clusters is set to 4.
 *              What is the maximum spacing of a 4-clustering?
 *
 *              ADVICE: If you're not getting the correct answer,
 *              try debugging your algorithm using some small test cases.
 *              And then post them to the discussion forum!
 *
 * @author : Zitao He
 * @date : 2021-01-31 17:39
 **/
public class ClusteringExplicit {
    private HashMap<Integer, ArrayList<Integer>> vertices; //Graph representation. List is the adjacency list
    private ArrayList<GraphEdge> edges; //Edges in this graph
    private UnionFind clusters;

    /**
     * Construct the graph using external txt file. File format can be found in class description
     * @param fileInputName file name to be imported
     * @throws FileNotFoundException throws error if file not found
     */
    public ClusteringExplicit(String fileInputName) throws FileNotFoundException {
        Scanner fileScanner;
        vertices = new HashMap<>();
        edges = new ArrayList<>();

        try{
            fileScanner = new Scanner(new File(fileInputName));
        }
        catch (IOException e) {
            throw new FileNotFoundException("Error: Input file is not found.");
        }
        fileScanner.nextLine(); //skip first line, because the first line does not represent edge/vertex
        while (fileScanner.hasNextLine()){ // add all vertices to graph
            String[] info = fileScanner.nextLine().split(" ");
            vertices.put(Integer.parseInt(info[0]), new ArrayList<>());
            vertices.put(Integer.parseInt(info[1]), new ArrayList<>());
        }

        fileScanner = new Scanner(new File(fileInputName));
        fileScanner.nextLine(); //skip first line, because the first line does not represent edge/vertex
        while(fileScanner.hasNextLine()){
            String[] info = fileScanner.nextLine().split(" ");
            int edgeStart = Integer.parseInt(info[0]);
            int edgeEnd = Integer.parseInt(info[1]);
            int edgeLength = Integer.parseInt(info[2]);
            GraphEdge edge = new GraphEdge(edgeStart, edgeEnd, edgeLength);
            GraphEdge edgeDummy = new GraphEdge(edgeEnd, edgeStart, edgeLength);

            this.edges.add(edge);
            this.edges.add(edgeDummy);

            vertices.get(edgeStart).add(edgeEnd);
            vertices.get(edgeEnd).add(edgeStart);
        }
        clusters = new UnionFind(vertices.keySet().size());
        Collections.sort(edges);
    }

    /**
     * Run clustering until a certain amount of clusters are reached
     * @param desiredNumClusters the number of clusters desired
     */
    public void clustering(int desiredNumClusters){
        for (int i = 0; i<edges.size(); i++){
            if (clusters.getNumClusters() <= desiredNumClusters){
                break;
            }
            GraphEdge currEdge = edges.get(i);
            clusters.union(currEdge.getStartID()-1, currEdge.getEndID()-1); //id in UnionFind and id for vertex is shifted by 1
        }
        clusters.printUnionFind();
    }

    /**
     * Calculate the max spacing in clustered graph. Max spacing is the min distance between two
     * vertices that are not in the same cluster
     * @return max spacing in clustered graph
     */
    public int getClusterMaxSpacing(){
        int maxSpacing = -1;
        for (int i = 0; i<edges.size(); i++){
            GraphEdge currEdge = edges.get(i);
            if (!clusters.connnected(currEdge.getStartID()-1, currEdge.getEndID()-1)){
                maxSpacing = currEdge.getLength();
                return maxSpacing;
            }
        }
        return maxSpacing;
    }

    /**
     * Helper print method for debugging
     */
    public void printGraph(){
        System.out.println("********************   Printing edge list    ********************");
        for (GraphEdge edge : edges){
            System.out.println(edge);
        }
        System.out.println("********************   Printing adjacency list    ********************");
        for (int key : vertices.keySet()){
            System.out.println(key + ": " + vertices.get(key));
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        //Running clustering algorithm on "data/clustering1-test.txt" with k=2
        // should give a maximum spacing of 100 for a 2-clustering
        //ClusteringExplicit tester = new ClusteringExplicit("data/clustering1-test.txt");

        //expected output for 2 clusters: 5
        //expected output for 4 clusters: 3
        //ClusteringExplicit tester = new ClusteringExplicit("data/clustering1-test2.txt");

        ClusteringExplicit tester = new ClusteringExplicit("data/clustering1.txt");

        //tester.printGraph();
        tester.clustering(4);
        System.out.println(tester.getClusterMaxSpacing());//correct answer is 106
    }
}
