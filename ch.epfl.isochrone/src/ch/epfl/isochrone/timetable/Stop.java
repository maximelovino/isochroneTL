package ch.epfl.isochrone.timetable;

import ch.epfl.isochrone.geo.PointWGS84;

public final class Stop {
    private final String name;
    private final PointWGS84 position;
    
    public Stop(String name, PointWGS84 position){
        this.name=name;
        this.position=position;
    }
    
    public String name(){
        return name;
    }
    
    public PointWGS84 position(){
        return position;
    }
    
    public String toString(){
        return name;
    }
}