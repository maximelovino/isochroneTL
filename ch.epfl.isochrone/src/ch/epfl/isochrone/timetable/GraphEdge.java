package ch.epfl.isochrone.timetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static ch.epfl.isochrone.math.Math.*;

final class GraphEdge {
    private final ArrayList<Integer> packedTrips;
    private final Stop destination;
    private final int walkingTime;
    
    public GraphEdge(Stop destination, int walkingTime, Set<Integer> packedTrips) throws IllegalArgumentException{
        this.destination=destination;
        this.walkingTime=walkingTime;
        this.packedTrips=new ArrayList<Integer>(packedTrips);
        Collections.sort(this.packedTrips);        
    }
    
    public static int packTrip(int departureTime, int arrivalTime) throws IllegalArgumentException{
        if(departureTime<0||departureTime>107999){
            throw new IllegalArgumentException();
        }
        if(arrivalTime-departureTime<0||arrivalTime-departureTime>9999){
            throw new IllegalArgumentException();
        }
        
        int packedTrip=departureTime*10000+(arrivalTime-departureTime);
        
        return packedTrip;
    }
    
    public static int unpackDepartureTime(int packedTrip){
        return divF(packedTrip,10000);
    }
    
    public static int unpackTripDuration(int packedTrip){
        return modF(packedTrip,10000);
    }
    
    public static int unpackArrivalTime(int packedTrip){
        return unpackDepartureTime(packedTrip)+unpackTripDuration(packedTrip);
    }
    
    public Stop destination(){
        return destination;
    }
    
    public int earliestArrivalTime(int departureTime){
//      binary search: O(log(n)) complexity
        int index=Collections.binarySearch(this.packedTrips,departureTime);
        
        if(index<0){
            index=(index+1)*-1;
        }
        
        if (this.walkingTime+departureTime<unpackArrivalTime(packedTrips.get(index))){
            return walkingTime+departureTime;
        }else{
            return unpackArrivalTime(packedTrips.get(index));
        }
    }
    
    public static class Builder{
        private final Stop destination;
        private int walkingTime;
        private final Set<Integer> packedTrips;
        
        public Builder(Stop destination){
            this.destination=destination;
            this.packedTrips=new HashSet<Integer>();
        }
        
        public Builder setWalkingTime(int newWalkingTime){
            this.walkingTime=newWalkingTime;
            return this;
        }
        
        public Builder addTrip(int departureTime, int arrivalTime){
            packedTrips.add(packTrip(departureTime, arrivalTime));
            return this;
        }
        
        public GraphEdge build(){
            return new GraphEdge(this.destination, this.walkingTime, this.packedTrips);
        }
        
    }
    
    
}
