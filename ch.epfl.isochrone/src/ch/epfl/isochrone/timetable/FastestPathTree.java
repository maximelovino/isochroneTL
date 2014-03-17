package ch.epfl.isochrone.timetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class FastestPathTree {
    private final Stop startingStop;
    private final Map<Stop, Integer> arrivalTime;
    private final Map<Stop, Stop> predecessor;

    public FastestPathTree(Stop startingStop, Map<Stop, Integer> arrivalTime, Map<Stop, Stop> predecessor){
        if (!(arrivalTime.keySet().equals(predecessor.keySet()))){
            throw new IllegalArgumentException();
        }

        this.startingStop=startingStop;
        this.arrivalTime=new HashMap<Stop, Integer>(arrivalTime);
        this.predecessor=new HashMap<Stop, Stop>(predecessor);
    }

    public Stop startingStop(){
        return this.startingStop;
    }

    public int startingTime(){
        return this.arrivalTime.get(this.startingStop);
    }

    public Set<Stop> stops(){
        Set<Stop> stops=new HashSet<Stop>();

        for(Stop aStop: this.arrivalTime.keySet()){
            if(!(arrivalTime.get(aStop)==null)){
                stops.add(aStop);
            }
        }

        return stops;
    }

    public int arrivalTime(Stop stop){
        if(arrivalTime.get(stop)==null){
            return SecondsPastMidnight.INFINITE;
        }else{
            return arrivalTime.get(stop);
        }
    }

    public List<Stop> pathTo(Stop stop) throws IllegalArgumentException{
        if(!(arrivalTime.containsKey(stop))){
            throw new IllegalArgumentException();
        }

        ArrayList<Stop> path=new ArrayList<Stop>();

        while(!(stop==null)){
            path.add(stop);
            stop=predecessor.get(stop);            
        }

        Collections.reverse(path);
        return path;
    }

    public static class Builder{
        private final Stop startingStop;
        private final int startingTime;
        private final Map<Stop, Integer> arrivalTime;
        private final Map<Stop, Stop> predecessor;

        public Builder(Stop startingStop, int startingTime)throws IllegalArgumentException{
            if(startingTime<0){
                throw new IllegalArgumentException();
            }
            this.startingStop=startingStop;
            this.startingTime=startingTime;
            this.arrivalTime=new HashMap<Stop, Integer>();
            this.predecessor=new HashMap<Stop, Stop>();
        }

        public Builder setArrivalTime(Stop stop, int time, Stop predecessor) throws IllegalArgumentException{
            if(time<this.startingTime){
                throw new IllegalArgumentException();
            }
            
            this.arrivalTime.put(stop, time);
            this.predecessor.put(stop, predecessor);

            return this;
        }

        public int arrivalTime(Stop stop){
            Integer time=arrivalTime.get(stop);
            
            if(time==null){
                return SecondsPastMidnight.INFINITE;
            }else{
                return time;
            }
        }

        public FastestPathTree build(){
            return new FastestPathTree(startingStop, arrivalTime, predecessor);
        }
    }
}
