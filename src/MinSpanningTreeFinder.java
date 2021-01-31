import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Project: Coursera Algorithms by Standford University: Greedy Algorithms, Minimum Spanning Trees, and Dynamic Programming.
 * Description: Week 1 Programming Assignment. Prim's minimum spanning tree algorithm.
 *
 *              The text file in data folder "edges.txt" is used for the quiz.
 *              This file describes an undirected graph with integer edge costs. It has the format:
 *              [number_of_nodes] [number_of_edges]
 *              [one_node_of_edge_1] [other_node_of_edge_1] [edge_1_cost]
 *              [one_node_of_edge_2] [other_node_of_edge_2] [edge_2_cost]
 *              ...
 *              For example, the third line of the file is "2 3 -8874",
 *              indicating that there is an edge connecting vertex #2 and vertex #3 that has cost -8874.
 *
 *              You should NOT assume that edge costs are positive, nor should you assume that they are distinct.
 *
 *              Your task is to run Prim's minimum spanning tree algorithm on this graph.
 *              You should report the overall cost of a minimum spanning tree --- an integer,
 *              which may or may not be negative.
 *
 *              Run time of this algorithm is O(m*n), where m is the number of edges and n is the number of vertices.
 *              Fast O(m*log(n)) can be achieved by implementing heap data structure in min-cost edge search at each iteration.
 *
 * @author : Zitao He
 * @date : 2021-01-31 14:36
 **/
public class MinSpanningTreeFinder {
    private HashMap<Integer, ArrayList<Integer>> vertices; //Graph representation. List is the adjacency list
    private ArrayList<GraphEdge> edges; //Edges in this graph

    /**
     * Constructor using file input name
     * @param fileInputName file name of input .txt file
     * @throws FileNotFoundException throws exception when file not found
     */
    public MinSpanningTreeFinder(String fileInputName) throws FileNotFoundException {
        Scanner fileScanner;
        vertices = new HashMap<>();
        edges = new ArrayList<>();
        try{
            fileScanner = new Scanner(new File(fileInputName));
        }
        catch (IOException e) {
            throw new FileNotFoundException("Error: Input file is not found.");
        }

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
    }

    /**
     * Calculate the sum of costs of each edge in minimum spanning tree
     * @return the sum of costs of each edge in minimum spanning tree
     */
    public int costOfMST(){
        int costSum = 0;
        ArrayList<Integer> verticesInTree = new ArrayList<>();
        ArrayList<GraphEdge> minSpanningTree = new ArrayList<>();

        verticesInTree.add(1); //arbitrarily add one vertex to initial(here we add vertex zero)
        while(verticesInTree.size() < vertices.size()){
            int minEdgeCost = Integer.MAX_VALUE;
            int endOfMinEdge = 0;
            GraphEdge minEdge = edges.get(0);
            for(GraphEdge edge : edges){
                if (verticesInTree.contains(edge.getStartID()) && !verticesInTree.contains(edge.getEndID())){
                    if (edge.getLength() < minEdgeCost){
                        minEdgeCost = edge.getLength();
                        endOfMinEdge = edge.getEndID();
                        minEdge = edge;
                    }
                }
            }
            verticesInTree.add(endOfMinEdge);
            minSpanningTree.add(minEdge);
            costSum += minEdge.getLength();
        }
        return costSum;
    }

    public void printGraph(){
        System.out.println(edges);
        System.out.println(vertices);
    }

    public static void main(String[] args) throws FileNotFoundException{
//        MinSpanningTreeFinder tester = new MinSpanningTreeFinder("data/edges-test.txt"); //overall cost of MST: 7
        MinSpanningTreeFinder tester = new MinSpanningTreeFinder("data/edges.txt"); //overall cost of MST: 7

        //tester.printGraph();
        System.out.println("Sum of costs of minimum spanning tree is: " + tester.costOfMST());
    }
}
