

/** disjoint sets
 * @author Kieran Molnar
 * @author M A Hakim Newton
 * 
 * Implemented by Mathieu Guisard with minor modifications to the constructor to allow the class to be used in conjunctions with the RoadMapUtils class
 * Modification is the addition of an argument int Size, rather than having a pre-determined array size 
 * 
 */


public class DisjointSetsRank{

    // to store the parent of each node
    private int [] parent;			
    private int [] rank;
    private final int setSize;
    
    /** constructor
     */
    public DisjointSetsRank(int size)
    {
        parent = new int[size];
        rank = new int[size];
        setSize = size;
    }

    /** make a singleton for a node
     * @param k the node
     */
    public void make(int k) 
    {
        parent[k] = k;
        rank[k] = 0;
    }

    /** find the parent of a node
     * @param k the node
     */
    public int find(int k) 
    {   
        int r = k;
        while (r != parent[r]) 
            r = parent[r];
        while(parent[k] != r)
        {
            int kk = parent[k];
            parent[k] = r;
            k = kk;
        }
        return r;
    }

    // Added by Mathieu Guisard
    // Returns the rank of the node at index k
    public int getRank(int k){
        return rank[find(k)];
    }

    // Added by Mathieu Guisard
    // Returns the highest rank in the set

    public int getHighestRank(){
        int max = 0;
        for (int i = 0; i < rank.length; i++){
            if(rank[i] > max) max = i;
        }
        return max;
    }

    /** find the union of two nodes
     * @param i one node
     * @param j another node
     */
    public void union(int i, int j) 
    {
        i = find(i);	// find the root of the set 
        j = find(j);	// find the root of the set
        // make one root child of another
        if (rank[i] < rank[j]){
            parent[i] = j; 
        }
        else if (rank[i] > rank[j]){
            parent[j] = i;
        }
        else{
            parent[i] = j;
            rank[j] = rank[j] + 1;
        }
    }

    public void print()
    {
        System.out.print("  nodes:");
        for(int k = 0; k < setSize; k++)
            System.out.print(" " + k);
        System.out.print("\nparents:");
        for(int k = 0; k < setSize; k++)
            System.out.print(" " + parent[k]);
        System.out.print("\n  ranks:");
        for(int k = 0; k < setSize; k++)
            System.out.print(" " + rank[k]);
        System.out.println();
    }

    public int size() {
        return parent.length;
    }

}
