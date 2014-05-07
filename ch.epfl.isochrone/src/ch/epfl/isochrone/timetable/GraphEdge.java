package ch.epfl.isochrone.timetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static ch.epfl.isochrone.math.Math.*;

/**The edge of a graph
 * 
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
final class GraphEdge {
    private final ArrayList<Integer> packedTrips;
    private final Stop destination;
    private final int walkingTime;

    /**
     * @param destination
     *      The destination
     * @param walkingTime
     *      The time to go to the destination walking
     * @param packedTrips
     *      The set of possible trips from that edge to that destination
     * @throws IllegalArgumentException
     *      If walking time is smaller than -1
     */
    public GraphEdge(Stop destination, int walkingTime, Set<Integer> packedTrips){
        if(walkingTime<-1){
            throw new IllegalArgumentException();
        }
        this.destination=destination;
        this.walkingTime=walkingTime;
        this.packedTrips=new ArrayList<Integer>(packedTrips); 
        Collections.sort(this.packedTrips);
    }

    /**
     * @param departureTime
     *      The time of departure
     * @param arrivalTime
     *      The time of arrival
     * @return
     *      The trip in the encoded format, with the departureTime for the high power bits and the lenght for the low power bits
     * @throws IllegalArgumentException
     *      If the departureTime is negative or exceeds the 107999 and if the length is negative or exceeds 9999
     */
    public static int packTrip(int departureTime, int arrivalTime) throws IllegalArgumentException{
        if(departureTime<0||departureTime>107999){
            throw new IllegalArgumentException("invalid departuretime");
        }
        if(arrivalTime-departureTime<0||arrivalTime-departureTime>9999){
            throw new IllegalArgumentException("invalid trip length");
        }

        int packedTrip=departureTime*10000+(arrivalTime-departureTime);

        return packedTrip;
    }

    /**
     * @param packedTrip
     *      A trip in encoded format
     * @return
     *      The departure time decoded
     */
    public static int unpackTripDepartureTime(int packedTrip){
        return divF(packedTrip,10000);
    }

    /**
     * @param packedTrip
     *      A trip in encoded format
     * @return
     *      The duration decoded
     */
    public static int unpackTripDuration(int packedTrip){
        return modF(packedTrip,10000);
    }

    /**
     * @param packedTrip
     *      A trip in encoded format
     * @return
     *      The arrival time decoded
     */
    public static int unpackTripArrivalTime(int packedTrip){
        return unpackTripDepartureTime(packedTrip)+unpackTripDuration(packedTrip);
    }

    /**
     * @return the destination
     */
    public Stop destination(){
        return destination;
    }

    /**
     * @param departureTime
     *      The time of departure
     * @return The earliest arrival time to the destination, either walking or using public transportation
     */
    public int earliestArrivalTime(int departureTime){

        //      binary search: O(log(n)) complexity
        int index=Collections.binarySearch(this.packedTrips,departureTime*10000);

        if(index<0){
            index=(index+1)*(-1);
        }
        
        if (walkingTime<0 && (this.packedTrips.isEmpty()||index==this.packedTrips.size())){
            return SecondsPastMidnight.INFINITE;
        }else if((this.packedTrips.isEmpty()||index==this.packedTrips.size())){
            return this.walkingTime+departureTime;
        }else{
            if (this.walkingTime+departureTime<unpackTripArrivalTime(this.packedTrips.get(index))){
                return this.walkingTime+departureTime;
            }else{
                return unpackTripArrivalTime(this.packedTrips.get(index));
            }
        }

    }

    /**The static nested builder class for a GraphEdge
     * 
     * @author Maxime Lovino (236726)
     * @author Julie Djeffal (193164)
     *
     */
    public static class Builder{
        private final Stop destination;
        private int walkingTime;
        private final Set<Integer> packedTrips;

        /**
         * @param destination The destination
         */
        public Builder(Stop destination){
            this.destination=destination;
            this.packedTrips=new HashSet<Integer>();
            this.walkingTime=-1;
        }

        /**
         * @param newWalkingTime
         *      The walking time that we want to set
         * @return The builder in construction (this) so we can chain method calls
         */
        public Builder setWalkingTime(int newWalkingTime){
            if (newWalkingTime<-1){
                throw new IllegalArgumentException("the walking time is smaller than -1");
            }
            this.walkingTime=newWalkingTime;
            return this;
        }

        /**
         * @param departureTime
         *      The departure time of the trip that we want to add
         * @param arrivalTime
         *      The arrival time of the trip that we want to add
         * @return The builder in construction (this) so we can chain method calls
         */
        public Builder addTrip(int departureTime, int arrivalTime){
            packedTrips.add(packTrip(departureTime, arrivalTime));
            return this;
        }

        /**
         * @return A graphEdge of the form of the graphEdge in construction
         */
        public GraphEdge build(){
            return new GraphEdge(this.destination, this.walkingTime, this.packedTrips);
        }

    }


}
