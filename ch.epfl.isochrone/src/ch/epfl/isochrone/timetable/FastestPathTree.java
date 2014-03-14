package ch.epfl.isochrone.timetable;

import java.util.Map;

public final class FastestPathTree {
    private final Stop startingStop;
    private final Map<Stop, Integer> arrivalTime;
    private final Map<Stop, Stop> predecessor;
    
    public FastestPathTree(Stop startingStop, Map<Stop, Integer> arrivalTime, Map<Stop, Stop> predecessor){
        this.startingStop=startingStop;
        this.arrivalTime=arrivalTime;
        this.predecessor=predecessor;
    }
    
    public Stop startingStop(){
        return this.startingStop;
    }
    
    public int startingTime(){
        return this.arrivalTime.get(this.startingStop);
    }
    
    public Set<Stop> stops(){
        
    }
    
    public int arrivalTime(Stop stop){
        
    }
    
    public List<Stop> pathTo(Stop stop) throws IllegalArgumentException{
        
    }
    
    public static class Builder{
        private final Stop startingStop;
        private final int startingTime;
        
        public Builder(Stop startingStop, int startingTime)throws IllegalArgumentException{
            if(startingTime<0){
                throw new IllegalArgumentException();
            }
        }
        
        public Builder setArrivalTime(Stop stop, int time, Stop predecessor) throws IllegalArgumentException{
            if(time<this.startingTime){
                throw new IllegalArgumentException();
            }
            return this;
        }
        
        public int arrivalTime(Stop stop){
            
        }
        
        public FastestPathTree build(){
            
        }
    }
}
