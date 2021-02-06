import java.util.Arrays;

/**
 * Project: Coursera Algorithms by Standford University: Greedy Algorithms, Minimum Spanning Trees, and Dynamic Programming.
 * Description: Helper class. This class represents a union-find data structure that offers two methods: find and union
 *
 * @author : Zitao He
 * @date : 2021-02-04 20:10
 **/
public class UnionFind {
    private int numClusters;
    private int[] clusterRoot;
    private int[] clusterSize;

    /**
     * Constructor of UnionFind
     * @param n the number of elements that will be in UnionFind
     */
    public UnionFind(int n){
        this.numClusters = n;
        this.clusterRoot = new int[n];
        this.clusterSize = new int[n];
        for (int i=0; i<n; i++){
            clusterRoot[i] = i; //if the cluster has only one element, then its root is itself
            clusterSize[i] = 1; //all clusters have only one element initially, so the sizes of clusters are all 1
        }
    }

    /**
     * Find the root/leader element of a given element
     * @param id the element to find its root/leader
     * @return the root/leader element of element id
     */
    public int find(int id){
        while(id != clusterRoot[id]){
            id = clusterRoot[id];
        }
        return id;
    }

    /**
     * Merge/union two separated elements into one cluster
     * @param i one element to merge
     * @param j the other element to merge
     */
    public void union(int i, int j){
        if (find(i) == find(j)){ //if i and j are already in the same cluster, do nothing
            return;
        }
        if(clusterSize[i] < clusterSize[j]){ //add smaller cluster to the root of larger cluster
            clusterRoot[i] = j;
            clusterSize[j] += clusterSize[i];
        }
        else{
            clusterRoot[j] = i;
            clusterSize[i] += clusterSize[j];
        }
        numClusters --;
    }

    /**
     * Check if two elements are in the same cluster (are connected)
     * @param a one element
     * @param b the other element
     * @return true if two elements are connected, false if two elements are not connected
     */
    public boolean connnected(int a, int b){
        return (find(a) == find(b));
    }

    public void printUnionFind(){
        System.out.println("Printing UnionFind root array: " + Arrays.toString(clusterRoot));
        System.out.println("Printing UnionFind size array: " + Arrays.toString(clusterSize));
        System.out.println("Printing UnionFind cluster counts: " + numClusters);

    }

    public int getNumClusters() {
        return numClusters;
    }
}
