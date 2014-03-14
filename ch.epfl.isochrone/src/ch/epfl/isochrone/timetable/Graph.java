package ch.epfl.isochrone.timetable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Graph {

    private Graph(Set<Stop> stops, Map<Stop, List<GraphEdge>> outgoingEdges){
        
    }
    
    public final class Builder{
        private final Set<Stop> stops;
        private final List<GraphEdge> tripEdges;
        private Map<Stop, Map<Stop, GraphEdge.Builder>> buildingEdges;
        
        public Builder(Set<Stop> stops){
            this.stops=new HashSet<Stop>(stops);
            this.tripEdges=new ArrayList<GraphEdge>();
            this.buildingEdges=new HashMap<Stop, Map<Stop, GraphEdge.Builder>>();            
        }
        
        public Builder addTripEdge(Stop fromStop, Stop toStop, int departureTime, int arrivalTime) throws IllegalArgumentException{
            if(!this.stops.contains(fromStop)||!this.stops.contains(toStop)||departureTime<0||arrivalTime<0||arrivalTime<departureTime){
                throw new IllegalArgumentException();
            }
            
            
            return this;
        }
        
        public Builder addAllWalkEdges(int maxWalkingTime, double walkingSpeed) throws IllegalArgumentException{
            if(maxWalkingTime<0||walkingSpeed<=0){
                throw new IllegalArgumentException();
            }
            return this;
        }
        
        private Map<Stop, GraphEdge.Builder> createMap(Stop toStop, int departureTime, int arrivalTime){
            GraphEdge.Builder b=new GraphEdge.Builder(toStop);
            b.addTrip(departureTime, arrivalTime);
            
            Map<Stop, GraphEdge.Builder> m=new HashMap<Stop, GraphEdge.Builder>();
            m.put(toStop, b);
            
            return m;
        }
        
        public Graph build(){
            return new Graph();
        }
    }
}
