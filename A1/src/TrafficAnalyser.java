/*
 * COMP2230 - Algorithms
 * Assignment 1 - Answers
 * @author  Mathieu Guisard - c3256835
 * @version 1.0
 * 
 * This is where you will write your code for the assignment. The required methods have been started for you, you may add additional helper methods and classes as required.
 */
import java.util.*; 

 public class TrafficAnalyser {
    private final MapGenerator mapGen;
    public String cityMap = null;  // We make this public in order to access it for testing, you would not normally do this

    private List<Road> RoadList;
    private disjointSetsRank cityMapRank;



    public TrafficAnalyser(int seed){
        mapGen = new MapGenerator(seed); // Pass a seed to the random number generator, allows for reproducibility
    }

    /*
     * This method must load the city map from the map generator and store it
     * You may assume that all marking scripts will call this method before any others.
     * Do not modify the code above the comment line in this method - this will be used to manually insert specific maps for marking purposes.
     * Write your code below the comment line where indicated.
     */
    public void loadMap(){
        if (cityMap == null) {
            cityMap = mapGen.generateMap();
        }
        
        // My work begins here



        
    }

    /*
     * @param intersectionName The name of the intersection to check
     * @return true if the intersection is in the inner city, false otherwise
     * 
     * The 'inner city' is defined as the largest connected component of intersections in the map.
     * 
     */
    public boolean isInInnerCity(String intersectionName) {
        // Your code here
        throw new UnsupportedOperationException("Not implemented yet"); // Get rid of this line when you start implementing

    }

    /*
     * @param threshold The travel time threshold in minutes
     * @return The number of roads in the 'inner city' that have a travel time strictly greater (>) than the threshold
     * 
     * The 'inner city' is defined as the largest connected component of intersections in the map.
     * 
     */
    public int countInnerCitySlowRoads(double threshold) {
        // Your code here

        throw new UnsupportedOperationException("Not implemented yet"); // Get rid of this line when you start implementing
    }    

    /* 
     * @return An array of road names that represent roads currently in the 'inner city' which, if any single one is closed, would result in an intersection no longer being reachable from the 'inner city'
     * 
     * The goal here is to identify roads that are critical to the connectivity of the city, that is, if any of these roads are closed, the inner city will be split into more disconnected components.
     * Remember that the 'inner city' is defined as the largest connected component of intersections in the map.
     *  
     */
    public String[] cityBottleneckRoads() {
        // Your code here

        throw new UnsupportedOperationException("Not implemented yet"); // Get rid of this line when you start implementing
    }

    /*
     * @param intersectionName The name of the intersection to check
     * @param hops The distance from the intersection to lockdown
     * @return An array of road names that have endpoints within 'hops' of the intersection
     * 
     * Which roads do we need to lockdown to secure the minister for transport's visit?
     * Note: You are required to use Breadth First Search (BFS) as part of your solution.
     * 
     */
    public String[] lockdownIntersection(String intersectionName, int hops) {
        // Your code here

        throw new UnsupportedOperationException("Not implemented yet"); // Get rid of this line when you start implementing
    }

    /*
     * @param intersectionNames An array of intersection names where protests are occurring
     * @return The number of roads that need to be closed to contain the protests, according to the rules below
     * 
     * Implement this as a simulation - every time step, you move first - then the protesters move.
     * On your move, you must identify a connected component of intersections blocked by protesters, and close the minimum number of roads to separate it from the rest of the city.
     * On the protesters move, they will spread to all connected intersections with a road open to the current intersection. They will only travel a single 'hop' per time step.
     * 
     * Specifically, on your move, you must identify the connected component of protesters that will spread to the *largest* number of intersections on the protesters' next move.
     * This is the one you must close off. If there is a tie, choose the one that requires the least number of roads to be closed in order to contain it. If there is still a tie, you may choose any of the tied components.
     *  
     */
    public int containProtests(String[] intersectionNames){
        // Your code here

        throw new UnsupportedOperationException("Not implemented yet"); // Get rid of this line when you start implementing
    }
}


class Road {
    private String[] roadArray;
    private  String Endpoint1;
    private String Endpoint2;
    private String RoadName;
    private float travelTime;

    // Constructor for new roadmap
    public Road(String roadString){
        roadString = roadString.replace("{", "");
        roadString = roadString.replace("}", "");
        roadArray = roadString.split(", ");

        RoadName = roadArray[0];
        Endpoint1 = roadArray[1];
        Endpoint2 = roadArray[2];
        travelTime = Float.parseFloat(roadArray[3]);

    }

    // Overloaded constructor for road cloning
    public Road(String Name, String End1, String End2, Float TravelTime){
        RoadName = Name;
        Endpoint1 = End1;
        Endpoint2 = End2;
        travelTime = TravelTime;
    }

    public void setRoadName(String[] roadArray){
        RoadName = roadArray[0];
    }

    public void setEnd1(String[] roadArray){
        Endpoint1 = roadArray[1];
    }

    public void setEnd2(String[] roadArray){
        Endpoint2 = roadArray[2];
    }

    public void setTravelTime(String[] roadArray){
        travelTime = Integer.parseInt(roadArray[3]);
    }

    public String getRoadName(){
        return RoadName;
    }

    public List<String> getEnds(){
        List<String> endsList = new ArrayList<>();
        endsList.add(Endpoint1);
        endsList.add(Endpoint2);
        return endsList;
    }

    public String getEnd1(){
        return Endpoint1;
    }

    public String getEnd2(){
        return Endpoint2;
    }

    public float getTravelTime(){
        return travelTime;
    }

    public List<String> getAdjacent(){
        List<String> adjacent = new ArrayList<>();
        adjacent.add(Endpoint1);
        adjacent.add(Endpoint2);

        return adjacent;
    }

}



class RoadMapUtils {
    public boolean checkRoadIntersection(Road road1, Road road2){
        boolean present = false;

        if (road1.getEnd1().equals(road2.getEnd1())) present = true;
        else if (road1.getEnd1().equals(road2.getEnd2())) present = true;
        else if (road1.getEnd1().equals(road2.getEnd1())) present = true;
        else if (road1.getEnd2().equals(road2.getEnd2())) present = true;

        return present;
    }

    public List<Road> generateRoadMap(String cityMap){
        int i = 0;
        List<Road> RoadList = new ArrayList<>();

        cityMap = cityMap.replace("{{", "{");
        cityMap = cityMap.replace("}}", "}");

        String[] StreetList = cityMap.split("\\}, \\{");

        while (i < StreetList.length){
            Road tempRoad = new Road(StreetList[i]);
            RoadList.add(tempRoad);
            i++;
        }

        return RoadList;
    }

    public Road cloneRoad(Road Target){
        Road NewRoad = new Road(Target.getRoadName(), Target.getEnd1(), Target.getEnd2(), Target.getTravelTime());
        return NewRoad;
    }
    

}


class disjointSetsRank{

    // to store the parent of each node
    private int [] parent;			
    private int [] rank;	
    
    /** constructor
     */
    public disjointSetsRank(int size)
    {
        parent = new int[2*size];
        rank = new int[2*size];
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

    /** find the union of two nodes
     * @param i one node
     * @param j another node
     */
    public void union(int i, int j) 
    {
        i = find(i);	// find the root of the set 
        j = find(j);	// find the root of the set
        // make one root child of another
        if (rank[i] < rank[j])
            parent[i] = j; 
        else if (rank[i] > rank[j])
            parent[j] = i;
        else
        {
            parent[i] = j;
            rank[j] = rank[j] + 1;
        }
    }

}
