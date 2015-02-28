package ch.epfl.isochrone.timetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class FastestPathTree {
    private final Stop startingStop;
    private final Map<Stop, Integer> arrivalTime;
    private final Map<Stop, Stop> predecessor;

    /**
     * @param startingStop
     *      The starting stop
     * @param arrivalTime
     *      A mapping of an arrival stop with its time of arrival
     * @param predecessor
     *      A mapping of a stop with its predecessor in the path
     */
    public FastestPathTree(Stop startingStop, Map<Stop, Integer> arrivalTime, Map<Stop, Stop> predecessor) {
        Set<Stop> predSet = new HashSet<Stop>(predecessor.keySet());
        predSet.add(startingStop);
        if (!(arrivalTime.keySet().equals(predSet))){
            throw new IllegalArgumentException("the map of arrivalTimes and predecessors don't have the same key");
        }

        this.startingStop = startingStop;
        this.arrivalTime = new HashMap<Stop, Integer>(arrivalTime);
        this.predecessor = new HashMap<Stop, Stop>(predecessor);
    }

    /**
     * @return The starting stop of the path
     */
    public Stop startingStop() {
        return this.startingStop;
    }

    /**
     * @return The starting time of the trip
     */
    public int startingTime() {
        return this.arrivalTime.get(this.startingStop);
    }

    /**
     * @return A set of the stops for which an arrival time exists
     */
    public Set<Stop> stops() {
        Set<Stop> stops = new HashSet<Stop>(this.arrivalTime.keySet());
        return stops;
    }

    /**
     * @param stop
     *      The stop of which we want to know the time of arrival
     * @return The arrival time at that stop 
     * or SecondsPastMidnight.INFINITE if there is no arrival time defined
     */
    public int arrivalTime(Stop stop) {
        if (!arrivalTime.containsKey(stop)) {
            return SecondsPastMidnight.INFINITE;
        } else {
            return arrivalTime.get(stop);
        }
    }

    /**
     * @param stop
     *      An arrival stop
     * @return The fastest path to that stop in the form of an arraylist 
     * @throws IllegalArgumentException
     *      If the Map of arrival times doesn't contain a key for that stop
     */
    public List<Stop> pathTo(Stop stop) throws IllegalArgumentException {
        if( !(arrivalTime.containsKey(stop))) {
            throw new IllegalArgumentException("the stop is not contained in the mapping of arrivalTimes");
        }

        ArrayList<Stop> path = new ArrayList<Stop>();

        while (!(stop == null)) {
            path.add(stop);
            stop = predecessor.get(stop);            
        }

        Collections.reverse(path);
        return path;
    }

    /**A static nested builder class for the fastest path tree
     * 
     * @author Maxime Lovino (236726)
     * @author Julie Djeffal (193164)
     *
     */
    public static class Builder {
        private final Stop startingStop;
        private final int startingTime;
        private final Map<Stop, Integer> arrivalTime;
        private final Map<Stop, Stop> predecessor;

        /**
         * @param startingStop
         *      The starting stop of the trip
         * @param startingTime
         *      The starting time of the trip
         * @throws IllegalArgumentException
         *      If the starting time is negative
         */
        public Builder(Stop startingStop, int startingTime) throws IllegalArgumentException {
            if (startingTime < 0) {
                throw new IllegalArgumentException("the starting time is negative");
            }
            this.startingStop = startingStop;
            this.startingTime = startingTime;
            this.arrivalTime = new HashMap<Stop, Integer>();
            this.arrivalTime.put(startingStop, startingTime);
            this.predecessor = new HashMap<Stop, Stop>();
        }

        /**
         * @param stop
         *      The stop that we want to set an arrival time to
         * @param time
         *      The time of arrival to that stop
         * @param predecessor
         *      The predecessor stop to that stop in the path
         * @return
         *      The builder (this) so we can chain method calls
         * @throws IllegalArgumentException
         *      If the time of arrival is inferior 
         *      to the starting time of the trip
         */
        public Builder setArrivalTime(Stop stop, int time, Stop predecessor) throws IllegalArgumentException {
            if (time < this.startingTime) {
                throw new IllegalArgumentException("time of arrival<startingTime");
            }
            
            this.arrivalTime.put(stop, time);
            this.predecessor.put(stop, predecessor);

            return this;
        }

        /**
         * @param stop
         *      The stop that we want to know the arrival time to
         * @return
         *      The current arrival time to that stop if it exists,
         *      or SecondsPastMidnight.INFINITE
         */
        public int arrivalTime(Stop stop) {
            Integer time=arrivalTime.get(stop);
            
            if (time == null) {
                return SecondsPastMidnight.INFINITE;
            } else {
                return time;
            }
        }

        /**
         * @return A fastest path tree built from the builder
         */
        public FastestPathTree build() {
            return new FastestPathTree(startingStop, arrivalTime, predecessor);
        }
    }
}
