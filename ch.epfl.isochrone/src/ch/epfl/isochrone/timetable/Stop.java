package ch.epfl.isochrone.timetable;

import ch.epfl.isochrone.geo.PointWGS84;

/**
 * A transit stop
 * 
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 * 
 *
 */
public final class Stop {
    private final String name;
    private final PointWGS84 position;
    
    /**
     * @param name
     *      The name of the stop
     * @param position
     *      The position in the WGS84 coordinates
     */
    public Stop(String name, PointWGS84 position){
        this.name=name;
        this.position=position;
    }
    
    /**
     * @return The name of the stop
     */
    public String name(){
        return name;
    }
    
    /**
     * @return The position of the stop as WGS84 point
     */
    public PointWGS84 position(){
        return position;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString(){
        return name;
    }
}