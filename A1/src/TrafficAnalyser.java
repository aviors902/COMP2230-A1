/*
 * COMP2230 - Algorithms
 * Assignment 1 - Answers
 * @author  Mathieu Guisard - c3256835
 * @version 1.0
 * 
 * This is where you will write your code for the assignment. The required methods have been started for you, you may add additional helper methods and classes as required.
 */
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.HashSet;
import java.util.Set;

 public class TrafficAnalyser {
    private final MapGenerator mapGen;
    public String cityMap = null;  // We make this public in order to access it for testing, you would not normally do this

    private List<Road> RoadList;
    private List<String> IntersectionList;
    private DisjointSetsRank InnerOuterMap;
    private List<String> InnerCityIntersections = new ArrayList<>();
    private List<Road> InnerCityRoads = new ArrayList<>();
    private HashMap<String, List<Road>> InnerCityIntersectionsMap = new HashMap<>(); //  A hashmap of intersections form only within the inner city with the stored value being the immediately adjacent roads
    private HashMap<String, List<Road>> EntireCityIntersectionsMap = new HashMap<>(); // A hashmap of intersections including the entire city with the stored value being the immediately adjacent roads


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
        RoadList = RoadMapUtils.generateRoadMap(cityMap); 
        IntersectionList = RoadMapUtils.generateIntersectionsList(cityMap);    
        InnerOuterMap = RoadMapUtils.rankInnerCityOuterCity(RoadList, IntersectionList);

        for (String intersection : IntersectionList){
            if (isInInnerCity(intersection)) InnerCityIntersections.add(intersection);
        }

        InnerCityRoads = RoadMapUtils.mapInnerCityRoads(InnerCityIntersections, RoadList);
        // Generates a Hash Map of the inner city intersections. Key is the intersection name, stored value is all the immediately adjacent roads
        InnerCityIntersectionsMap = RoadMapUtils.mapInnerCityIntersectionsToRoads(InnerCityIntersections, RoadList);
        // Generates a Hash Map of the entire city's intersections. Key is the intersection name, stored value is all the immediately adjacent roads.
        EntireCityIntersectionsMap = RoadMapUtils.mapEntireCityIntersectionsToRoads(RoadList);


    
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
        Boolean isInnerCity = false;

        // Checks if the supplied node's rank is equal to it's highest parent's rank. If true, the node is located in the inner city.
        int intersectionIndex = IntersectionList.indexOf(intersectionName);
        if((InnerOuterMap.getRank(intersectionIndex) == InnerOuterMap.getHighestRank())) isInnerCity = true;
        return isInnerCity;

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
        int count = 0;
        for (Road road : InnerCityRoads){
            if (road.getTravelTime() > threshold) count++;
        }
        return count;
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
        String[] bottleNecks;
        Set<String> BottleNeckIntersectionsList = new HashSet<>();
        Set<String> BottleNeckRoadsList = new HashSet<>();
        List<String> adjacentIntersectionsList = new ArrayList<>();


        /*  The following for loop checks every road in the inner city if it is a bottleneck
            There are 2 loops contained within. They are identical but performed on each endpoint of the road being queried
            The loops collect a list all the intersections which are immediately adjacent to the target endpoint. If those intersections have 2 or less adjacent roads (Including the one being queried), 
            then they are an intersection connected to a bottleneck road
        */
        for (Road key : InnerCityRoads){
            String endpoint1 = key.getEnd1();
            String endpoint2 = key.getEnd2();

            adjacentIntersectionsList = RoadMapUtils.getAdjacentIntersections(endpoint1, InnerCityIntersectionsMap);
            for (String intersection : adjacentIntersectionsList){
                int numberOfAdjacentRoads = InnerCityIntersectionsMap.get(intersection).size();
                if (numberOfAdjacentRoads <= 2) BottleNeckIntersectionsList.add(endpoint1);
            }
            adjacentIntersectionsList = RoadMapUtils.getAdjacentIntersections(endpoint2, InnerCityIntersectionsMap);
            for (String intersection : adjacentIntersectionsList){
                int numberOfAdjacentRoads = InnerCityIntersectionsMap.get(intersection).size();
                if (numberOfAdjacentRoads <= 2) BottleNeckIntersectionsList.add(endpoint2);
            }
        }

        /* The For loop below takes every intersection that was identified to be attached to a bottleneck road and if it has more than 2 roads, it is ignored
         * This is because the roads attached to it are not all bottlenecks
         * If there are 2 or less roads attached to the node, those roads are likely to be a bottleneck
         * The problem with this is if there is in fact a cycle formed in the graph e.g. a circle. Each node would appear as a bottleneck which is not strictly true, as a path would still exist if it was removed
         * Assuming the map is being viewed as a tree instead of as a graph, then the rules will apply.
         */
        for (String intersection : BottleNeckIntersectionsList){
            if (InnerCityIntersectionsMap.get(intersection).size() <= 2){
                for (Road roadName : InnerCityIntersectionsMap.get(intersection)){
                    BottleNeckRoadsList.add(roadName.getRoadName());
                }
            }
        }
        // A set was used to avoid duplicates. Converting the set to an array of strings allows the correct type to be returned.
        bottleNecks = BottleNeckRoadsList.toArray(new String[BottleNeckRoadsList.size()]);

        return bottleNecks;
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
        List<String> listed = RoadMapUtils.getRoadsWithinHops(hops, intersectionName, EntireCityIntersectionsMap);
        return listed.toArray(new String[listed.size()]);
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