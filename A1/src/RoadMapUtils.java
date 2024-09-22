/*
 * COMP2230 - Algorithms
 * Assignment 1 - RoadMapUtils class
 * @author  Mathieu Guisard - c3256835
 * @version 1.0
 * 
 * Utilities class for use with the Road class. Contains functions which will create data structures organising the roads into a map
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RoadMapUtils {

    // Takes the string cityMap and extracts data from it creating a list of Road objects
    public static List<Road> generateRoadMap(String cityMap){
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

    // Similar to generateRoadMap but instead of road objects being returned, it is a list of Intersections stored as Strings
    public static List<String> generateIntersectionsList(String cityMap) {
        Set<String> intersectionSet = new HashSet<>();
        // String cleanup for processing
        cityMap = cityMap.replace("{{", "{");
        cityMap = cityMap.replace("}}", "}");
        String[] StreetList = cityMap.split("\\}, \\{");
        // Takes the list of roads and their endpoints, adds each endpoint to a SET to ensure no duplicates, then converts the set to an ArrayList
        // Returns the arraylist
        for(String street : StreetList){
            // String cleanup for processing
            street = street.replace("{", "");
            street = street.replace("}", "");
            String[] streetArray = street.split(", ");
            // Adding the actual endpoints of each street to the set
            intersectionSet.add(streetArray[1]);
            intersectionSet.add(streetArray[2]);
        }
        return new ArrayList<>(intersectionSet);
    }

    // Crteates a Ranked Disjoint Set and returns the ranked set.
    // Argument roadsList is a List of Road Objects
    // Argument IntersectionsList is a List of Strings denoting the names of all the intersections in the city
    public static DisjointSetsRank rankInnerCityOuterCity(List<Road> roadsList, List<String> IntersectionsList){

        DisjointSetsRank cityMap = new DisjointSetsRank(IntersectionsList.size());
        // Checking size of the array vs number of nodes 
        for (String intersection : IntersectionsList){
            cityMap.make(IntersectionsList.indexOf(intersection));
        }
        // Checks every road and union the two intersections that are reachable
        for(Road road : roadsList){
           cityMap.union(IntersectionsList.indexOf(road.getEnd1()), IntersectionsList.indexOf(road.getEnd2()));
        }
        return cityMap;
    }

    // Deep copy method for cloning road objects
    public static Road cloneRoad(Road Target){
        Road NewRoad = new Road(Target.getRoadName(), Target.getEnd1(), Target.getEnd2(), Target.getTravelTime());
        return NewRoad;
    }

    // Method to generate a list of the roads that are considered part of the inner city
    public static List<Road> mapInnerCityRoads(List<String> InnerCityIntersections, List<Road> RoadList){
        List<Road> InnerCityRoads = new ArrayList<>(); 
        // Generating a list of roads based off the list of intersections
        for(Road road : RoadList){
            if (InnerCityIntersections.contains(road.getEnd1()) || InnerCityIntersections.contains(road.getEnd2())) InnerCityRoads.add(road);
        }
        return InnerCityRoads;
    }

    // A method to add a road to a hashed list of intersections. An intersection can have one or more roads attached to it. Key is the intersection name, value is a list containing the attached roads
    // Arg key is the intersection name. Arg newRoad is the road being added to the list
    public static void addToHashedArrayList(HashMap<String, List<Road>> map, String key, Road newRoad) {
        List<Road> oldList = map.get(key);
        // If the key doesn't exist, create a new ArrayList
        if (oldList == null) oldList = new ArrayList<>();
    
         oldList.add(newRoad);
        
         map.put(key, oldList);
    }


    // Method to map all the inner city intersections and the roads they are attached to.
    // Returns a hashmap where the key is an intersection and the stored value is a list of all adjacent roads
    public static HashMap<String, List<Road>> mapInnerCityIntersectionsToRoads(List<String> innerCityIntersections, List<Road> roads){
        HashMap<String, List<Road>> map = new HashMap<>();
        for (Road road : roads){
            if(innerCityIntersections.contains(road.getEnd1())) addToHashedArrayList(map, road.getEnd1(), road);
            if(innerCityIntersections.contains(road.getEnd2())) addToHashedArrayList(map, road.getEnd2(), road);
        }
        return map;
    }    

    // Similar to the mapInnerCityIntersectionsToRoads, but does not include an isInnerCity() check, instead just maps the entire city
    public static HashMap<String, List<Road>> mapEntireCityIntersectionsToRoads(List<Road> roadList){
        HashMap<String, List<Road>> allCityIntersectionsMap = new HashMap<>();
        for (Road road : roadList){
            addToHashedArrayList(allCityIntersectionsMap, road.getEnd1(), road);
            addToHashedArrayList(allCityIntersectionsMap, road.getEnd2(), road);
        }
        return allCityIntersectionsMap;
    }
    
    // Takes the argument intersection and returns a list of all the immediately adjacent intersections found in the HashMap.
    // Arg targetIntersection is the intersection you are using BFS on to locate all immediately adjacent intersections
    // Arg map is the hashmap of all immediately adjacent roads
    public static List<String> getAdjacentIntersections(String targetIntersection, HashMap<String, List<Road>> map){
        Set<String> adjacentIntersections = new HashSet<>();
        List<Road> adjacentRoads = new ArrayList<>();

        // Gets the list of roads adjacent to the target intersection
        adjacentRoads = map.get(targetIntersection);
        // Gets every endpoint from the roads, *NOT* including the target intersection
        for (Road road : adjacentRoads){
            if (!road.getEnd1().equals(targetIntersection)) adjacentIntersections.add(road.getEnd1());
            if (!road.getEnd2().equals(targetIntersection)) adjacentIntersections.add(road.getEnd2());
        }
    
        return new ArrayList<>(adjacentIntersections);
    }

    // Will locate all the roads that are within a specified number of intersections from the target intersection.
    public static List<String> getRoadsWithinHops(int MaxHops, String targetIntersection, HashMap<String, List<Road>> map){
        Set<String> roadNamesList = new HashSet<>();

        Set<String> intersectionsWithinRadius = new HashSet<>();
        List<String> immediatleyAdjacentIntersections = new ArrayList<>();

        // Setting the target intersection to be the only intersection in the list to be iterated ober
        immediatleyAdjacentIntersections.add(targetIntersection);
        int totalHopsTaken = 0; // gets all the immediately adjacent intersections. 

        // Loop only triggers if there is more than 1 hop required 
        while (totalHopsTaken < MaxHops) {
            List<String> newAdjacentIntersections = new ArrayList<>(); // Temporary list for new adjacent intersections
    
            // Iterating over current adjacent intersections
            for (String intersection : immediatleyAdjacentIntersections) {
                if (!intersectionsWithinRadius.contains(intersection)) {
                    // Get adjacent intersections and add to the temporary list
                    newAdjacentIntersections.addAll(getAdjacentIntersections(intersection, map));
                    intersectionsWithinRadius.add(intersection); // Mark as visited
                }
            }
            // Update the list for the next loop with newly discovered adjacent intersections
            immediatleyAdjacentIntersections = newAdjacentIntersections;
            totalHopsTaken++;
        }

        intersectionsWithinRadius.addAll(immediatleyAdjacentIntersections);

        // Collecting the list of roads attached to the intersections within specified number of hops
        for (String intersection : intersectionsWithinRadius){
            List<Road> intersectionRoads = map.get(intersection);
            for (Road road : intersectionRoads){
                roadNamesList.add(road.getRoadName());
            }
        }
        return new ArrayList<>(roadNamesList);
    }
}




