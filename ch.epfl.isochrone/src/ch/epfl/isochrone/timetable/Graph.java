package ch.epfl.isochrone.timetable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class Graph {
    private final Set<Stop> stops;
    private final Map<Stop, List<GraphEdge>> outgoingEdges;

    /**
     * @param stops
     *      A set of the stops of the graph
     * @param outgoingEdges
     *      A mapping of a stop of departure with all its outgoing edges
     */
    private Graph(Set<Stop> stops, Map<Stop, List<GraphEdge>> outgoingEdges){
        this.stops=new HashSet<Stop>(stops);

        this.outgoingEdges=new HashMap<Stop, List<GraphEdge>>();
        
        for (Iterator<Stop> it = outgoingEdges.keySet().iterator(); it.hasNext();) {
            Stop stop = (Stop) it.next();
            this.outgoingEdges.put(stop, new ArrayList<GraphEdge>(outgoingEdges.get(stop)));
        }
    }

    /**
     * @param startingStop
     *      The stop that we start from
     * @param departureTime
     *      The departure time of our trip
     * @return The fastest path from that stop at that time for all the other stops
     * @throws IllegalArgumentException
     *      If the startingStop is not in the set of stops or if the departureTime is smaller than 0
     */
    public FastestPathTree fastestPath(Stop startingStop, int departureTime) throws IllegalArgumentException{
        if(!(stops.contains(startingStop))||departureTime<0){
            throw new IllegalArgumentException("the stop is not in the set of stops or the departureTime is negative");
        }
        final FastestPathTree.Builder treeBuilder=new FastestPathTree.Builder(startingStop, departureTime);
        
        PriorityQueue<Stop> queue=new PriorityQueue<Stop>(stops.size(),new Comparator<Stop>() {

            @Override
            public int compare(Stop o1, Stop o2) {
                return Integer.compare(treeBuilder.arrivalTime(o1), treeBuilder.arrivalTime(o2));
            }
        
        });
        
        queue.addAll(stops);
        

        while(!queue.isEmpty()){
            Stop actualStop=queue.remove();
            int actualTime=treeBuilder.arrivalTime(actualStop);
            
            if(actualTime==SecondsPastMidnight.INFINITE){
                break;
            }
            
            List<GraphEdge> listEdge=this.outgoingEdges.get(actualStop);
            
            if(listEdge!=null){
                for (GraphEdge edge : listEdge) {
                    Stop aStop=edge.destination();
                    int earlyTime=edge.earliestArrivalTime(actualTime);
                    if(earlyTime<treeBuilder.arrivalTime(aStop)){
                        treeBuilder.setArrivalTime(aStop, earlyTime, actualStop);
                        queue.remove(aStop);
                        queue.add(aStop);
                    }
                }
            }else{
                continue;
            }
            
        }
        return treeBuilder.build();       
    }

    /**A static nested builder class for the graph
     * 
     * @author Maxime Lovino (236726)
     * @author Julie Djeffal (193164)
     *
     */
    public final static class Builder{
        private final Set<Stop> stops;
        private Map<Stop, Map<Stop, GraphEdge.Builder>> buildingEdges;

        /**
         * @param stops
         *      The set of the stops of the graph
         */
        public Builder(Set<Stop> stops){
            this.stops=new HashSet<Stop>(stops);
            this.buildingEdges=new HashMap<Stop, Map<Stop, GraphEdge.Builder>>();            
        }

        /**
         * @param fromStop
         *      The departure Stop of the trip
         * @param toStop
         *      The arrival Stop of the trip
         * @param departureTime
         *      The departure time of the trip
         * @param arrivalTime
         *      The arrival time of the trip
         * @return
         *      The builder (this) so we can chain method calls
         * @throws IllegalArgumentException
         *      If the departure Stop or the arrival Stop are not contained in the set of stops of the graph OR if one of the time is negative OR if the arrival time is inferior to the departure time 
         */
        public Builder addTripEdge(Stop fromStop, Stop toStop, int departureTime, int arrivalTime) throws IllegalArgumentException{
            if(!this.stops.contains(fromStop)||!this.stops.contains(toStop)||departureTime<0||arrivalTime<0||arrivalTime<departureTime){
                throw new IllegalArgumentException("one of the stops is not in the set of stops or one of the times is negative");
            }

            obtainBuilder(fromStop, toStop).addTrip(departureTime, arrivalTime);
            
            return this;
        }

        /**
         * @param maxWalkingTime
         *      The maximum time we can walk
         * @param walkingSpeed
         *      The walking speed
         * @return
         *      The builder (this) so we can chain method calls
         * @throws IllegalArgumentException
         *      If the maximum walking time is negative OR the walking speed is inferior or equal to 0
         */
        public Builder addAllWalkEdges(int maxWalkingTime, double walkingSpeed) throws IllegalArgumentException{
            if(maxWalkingTime<0||walkingSpeed<=0){
                throw new IllegalArgumentException("the walking time is negative or the walking speed<=0");
            }
            ArrayList<Stop> stopsList=new ArrayList<Stop>(stops);
            double maxWalkingDistance=maxWalkingTime*walkingSpeed;

            for(int i=0; i<stopsList.size();i++){
                for (int j=i+1;j<stopsList.size();j++){
                    double d=stopsList.get(i).position().distanceTo(stopsList.get(j).position());
                    if(d<=maxWalkingDistance){
                        int time=(int)Math.round(d/walkingSpeed);
                        obtainBuilder(stopsList.get(i),stopsList.get(j)).setWalkingTime(time);
                        obtainBuilder(stopsList.get(j),stopsList.get(i)).setWalkingTime(time);
                    }
                }
            }

            return this;
        }

        /**
         * @param fromStop
         *      The departure stop
         * @param toStop
         *      The arrival stop
         * @return
         *      A GraphEdge.Builder for that trip
         */
        private GraphEdge.Builder obtainBuilder(Stop fromStop, Stop toStop){
            //          We get the map of the GraphEdge.Builder for the departure stop
            Map<Stop, GraphEdge.Builder> m= buildingEdges.get(fromStop);

            //          if it's null, we create it and we continue with the one we created
            if(m==null){
                m=new HashMap<Stop,GraphEdge.Builder>();
            }

            //          We get the GraphEdge.Builder for the arrivalStop from the map
            GraphEdge.Builder builder=m.get(toStop);
            //          if it's null, we create it and we continue with the one we created
            if(builder==null){
                builder=new GraphEdge.Builder(toStop);
            }

            //          We are always sure to have a builder, because if it doesn't exist, we create one, and if it exists, we pick it
            return builder;
        }

        /**
         * @return A graph built from the Builder
         */
        public Graph build(){
            Map<Stop, List<GraphEdge>> mapTripEdges=new HashMap<Stop, List<GraphEdge>>();

            for(Map.Entry<Stop, Map<Stop, GraphEdge.Builder>> entryMap : buildingEdges.entrySet()){
                ArrayList<GraphEdge> edges=new ArrayList<GraphEdge>();
                for(GraphEdge.Builder edge: entryMap.getValue().values()){
                    edges.add(edge.build());
                }
                mapTripEdges.put(entryMap.getKey(),edges);
            }
            return new Graph(stops, mapTripEdges);
        }
    }
}
