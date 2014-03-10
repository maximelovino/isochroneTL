package ch.epfl.isochrone.timetable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Graph {

    private Graph(Set<Stop> stops, Map<Stop, List<GraphEdge>> outgoingEdges){
        
    }
    
    public final class Builder{
        private final Set<Stop> stops;
        private final List<GraphEdge> tripEdges;
        
        public Builder(Set<Stop> stops){
            this.stops=new HashSet<Stop>(stops);
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
