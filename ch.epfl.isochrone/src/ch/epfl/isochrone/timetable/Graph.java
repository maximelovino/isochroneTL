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
            
            obtainBuilder(fromStop, toStop).addTrip(departureTime, arrivalTime);
            
            return this;
        }
        
        public Builder addAllWalkEdges(int maxWalkingTime, double walkingSpeed) throws IllegalArgumentException{
            if(maxWalkingTime<0||walkingSpeed<=0){
                throw new IllegalArgumentException();
            }
            ArrayList<Stop> stopsList=new ArrayList<Stop>(stops);
            double maxWalkingDistance=maxWalkingTime*walkingSpeed;
            
            for(int i=0; i<stopsList.size();i++){
                for (int j=i+1;j<stopsList.size();j++){
                    double d=stopsList.get(i).position().distanceTo(stopsList.get(j).position());
                    if(d<maxWalkingDistance){
                        obtainBuilder(stopsList.get(i),stopsList.get(j)).setWalkingTime((int)(d*walkingSpeed));
                        obtainBuilder(stopsList.get(j),stopsList.get(i)).setWalkingTime((int)(d*walkingSpeed));
                    }
                }
            }

            return this;
        }
        
        private GraphEdge.Builder obtainBuilder(Stop fromStop, Stop toStop){
            Map<Stop, GraphEdge.Builder> m= buildingEdges.get(fromStop);
            
            if(m==null){
                Map<Stop, GraphEdge.Builder> newM=new HashMap();
                m=newM;
            }
            
            GraphEdge.Builder builder=m.get(toStop);
            if(builder==null){
                GraphEdge.Builder b=new GraphEdge.Builder(toStop);
                builder=b;
            }
            
            return builder;
        }
        
        public Graph build(){
            return new Graph();
        }
    }
}
