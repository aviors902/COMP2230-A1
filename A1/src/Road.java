/*
 * COMP2230 - Algorithms
 * Assignment 1 - Road class
 * @author  Mathieu Guisard - c3256835
 * @version 1.0
 * 
 * Road Class designed to represent a road ini a city map. Roads contain 2 endpoints, a road name and average traversal time
 */

import java.util.ArrayList;
import java.util.List;

public class Road {
    private String[] roadArray;
    private  String Endpoint1;
    private String Endpoint2;
    private String RoadName;
    private double travelTime;

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

    public double getTravelTime(){
        return travelTime;
    }

    public List<String> getAdjacent(){
        List<String> adjacent = new ArrayList<>();
        adjacent.add(Endpoint1);
        adjacent.add(Endpoint2);

        return adjacent;
    }
 // Overriding the .equals(obj) method
    public boolean equals(Road road){
        if((this.getRoadName().equals(road.getRoadName())) && (this.getTravelTime() == road.getTravelTime()))return true;
        else return false;
    }

}
