package ch.epfl.isochrone.timetable;

final class GraphEdge {
    
    
    public GraphEdge(Stop destination, int walkingTime, Set<Integer> packedTrips) throws IllegalArgumentException{
        
    }
    
    public static int packTrip(int departureTime, int arrivalTime) throws IllegalArgumentException{
        
    }
    
    public static int unpackDepartureTime(int packedTrip){
        
    }
    
    public static int unpackTripDuration(int packedTrip){
        
    }
    
    public static int unpackArrivalTime(int packedTrip){
        
    }
    
    private Stop destination(){
        
    }
    
    private int earliestArrivalTime(int departureTime){
        
    }
    
    public static class Builder{
        
        public Builder(Stop destination){
            
        }
        
        public Builder setWalkingTime(int newWalkingTime){
            return this;
        }
        
        public Builder addTrip(int departureTime, int arrivalTime){
            return this;
        }
        
        public GraphEdge build(){
            return new GraphEdge(destination, walkingTime, packedTrips);
        }
        
    }
    
    
}
