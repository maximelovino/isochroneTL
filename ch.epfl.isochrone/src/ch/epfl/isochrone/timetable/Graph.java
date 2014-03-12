package ch.epfl.isochrone.timetable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Graph {

    private Graph(Set<Stop> stops, Map<Stop, List<GraphEdge>> outgoingEdges){
        
    }
//    totally false certainly, need to understand how it works
    public final class Builder{
        private final Set<Stop> stops;
        private final List<GraphEdge> tripEdges;
        private Map<Stop, Map<Stop, GraphEdge.Builder>> buildingEdges;
        private Map<Stop, GraphEdge.Builder> nestBuildingEdges;
        
        public Builder(Set<Stop> stops){
            this.stops=new HashSet<Stop>(stops);
            this.tripEdges=new ArrayList<GraphEdge>();
            for(Stop aStop:stops){
                nestBuildingEdges.put(aStop, new GraphEdge.Builder(aStop));
                buildingEdges.put(aStop, nestBuildingEdges);
            }
            
        }
        
        public Builder addTripEdge(Stop fromStop, Stop toStop, int departureTime, int arrivalTime){
            
            return this;
        }
        
        public Builder addAllWalkEdges(int maxWalkingTime, double walkingSpeed){
            return this;
        }
        
        public Graph build(){
            return new Graph();
        }
    }
}
