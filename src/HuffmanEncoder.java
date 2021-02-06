import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PublicKey;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Project: Coursera Algorithms by Standford University: Greedy Algorithms, Minimum Spanning Trees, and Dynamic Programming.
 * Description: Week 3 Programming Assignment.
 *              In this programming problem and the next you'll code up the greedy algorithm from the lectures on Huffman coding.
 *              Find huffman.txt in data folder.
 *              This file describes an instance of the problem. It has the following format:
 *
 *              [number_of_symbols]
 *              [weight of symbol #1]
 *              [weight of symbol #2]
 *              ...
 *
 *              For example, the third line of the file is "6852892," indicating that
 *              the weight of the second symbol of the alphabet is 6852892.
 *              (We're using weights instead of frequencies, like in the "A More Complex Example" video.)
 *
 *              Your task in this problem is to run the Huffman coding algorithm from lecture on this data set.
 *              What is the maximum length of a codeword in the resulting Huffman code?
 *
 *              ADVICE: If you're not getting the correct answer,
 *              try debugging your algorithm using some small test cases.
 *              And then post them to the discussion forum!
 *
 * @author : Zitao He
 * @date : 2021-02-05 21:26
 **/
public class HuffmanEncoder {

    private int numSymbols;
    private int[] weights;
    private HashSet<Node> nodes;
    private PriorityQueue<Node> tree;

    public HuffmanEncoder(String fileInputName) throws FileNotFoundException {
        Scanner fileScanner;
        nodes = new HashSet<>();
        tree = new PriorityQueue<>();
        try{
            fileScanner = new Scanner(new File(fileInputName));
        }
        catch (IOException e) {
            throw new FileNotFoundException("Error: Input file is not found.");
        }
        numSymbols = Integer.parseInt(fileScanner.nextLine());
        weights = new int[numSymbols];
        int index = 0;
        while (fileScanner.hasNextLine()){
            int currWeight = Integer.parseInt(fileScanner.nextLine());
            weights[index++] = currWeight;
            Node currnNode = new Node(currWeight, null, null, true);
            nodes.add(currnNode);
            tree.add(currnNode);
        }
    }

    /**
     * Encode the Huffman tree. Find two nodes that have the lowest weights. Merge them to form
     * a parent node. Do this for all nodes until a tree is formed.
     * Each leaf in the Huffman tree represents one symbol. The height-1 of each left is the number of bits
     * it needs to encode the message.
     */
    public void encode(){
        while (!tree.isEmpty() && tree.size() >= 2){
            Node nodeX = tree.poll();
            Node nodeY = tree.poll();
            Node nodeXY = new Node(nodeX.getWeight()+nodeY.getWeight(), nodeX, nodeY, false);
            nodeX.incrementChildrenHeight();
            nodeY.incrementChildrenHeight();
            nodes.add(nodeXY);
            tree.add(nodeXY);
        }
    }

    /**
     * Get the minimum number of bits of a symbol in the complete Huffman tree
     * @return minimum number of bits
     */
    public int getMinCodeBits(){
        int minHeight = Integer.MAX_VALUE;
        for (Node node : nodes){
            if(node.isLeaf() && node.getHeight()<minHeight){
                minHeight = node.getHeight();
            }
        }
        return minHeight-1;
    }

    /**
     * Get the maximum number of bits of a symbol in the complete Huffman tree
     * @return maximum number of bits
     */
    public int getMaxCodeBits(){
        int maxHeight = Integer.MIN_VALUE;
        for (Node node : nodes){
            if(node.isLeaf() && node.getHeight()>maxHeight){
                maxHeight = node.getHeight();
            }
        }
        return maxHeight-1;
    }

    public void printEncoder(){
        System.out.println("This encoder has # of symbols: " + numSymbols);
        System.out.println("The weights array of this encoder is: ");

        for (int i = 0; i<numSymbols; i++){
            System.out.println(weights[i]);
        }
        System.out.println("Encoder tree is: ");

        for (Node node : nodes){
            System.out.println(node);
        }
    }

    /**
     * Helper method to define a node in Huffman tree
     */
    private static class Node implements Comparable<Node> {
        //private final int id;
        private final int weight;
        private final Node left, right;
        private final boolean isLeaf;
        private int height;

        /**
         * Construct a node
         * @param weight the weight of the node
         * @param left the left child of the node
         * @param right the right child of the node
         * @param isLeaf if the node to be created is a leaf
         */
        Node(int weight, Node left, Node right, boolean isLeaf) {
            if (isLeaf == true && (left != null || right != null)){
                throw new IllegalArgumentException("Error: when the node is a leaf, " +
                        "both left and right node have to be null");
            }
            this.weight = weight;
            this.left  = left;
            this.right = right;
            this.isLeaf = isLeaf;
            this.height = 1;
        }

        /**
         * Increment all children's height by 1 (when this node is merged with another)
         */
        public void incrementChildrenHeight(){
            height++;
            if (left == null && right == null){
                return;
            }
            this.left.incrementChildrenHeight();
            this.right.incrementChildrenHeight();

        }

        private boolean isLeaf() {
            return isLeaf;
        }

        public int compareTo(Node other) {
            return this.weight - other.weight;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "weight=" + weight +
                    ", left=" + left +
                    ", right=" + right +
                    ", isLeaf=" + isLeaf +
                    ", height=" + height +
                    '}';
        }
        public int getHeight(){
            return height;
        }

        public int getWeight(){
            return weight;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        HuffmanEncoder tester = new HuffmanEncoder("data/huffman.txt");
        //HuffmanEncoder tester = new HuffmanEncoder("data/huffman-test1.txt"); //Min code length = 2. Max code length = 5
        //HuffmanEncoder tester = new HuffmanEncoder("data/huffman-test2.txt"); // Min code length = 3. Max code length = 6
        tester.encode();
        System.out.println("Min code bits is: " + tester.getMaxCodeBits());
        System.out.println("Min code bits is: " + tester.getMinCodeBits());
        //correct answer is 19 and 9

    }
}
