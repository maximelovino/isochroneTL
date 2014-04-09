package ch.epfl.isochrone.geo;

import static java.lang.Math.*;
import static ch.epfl.isochrone.math.Math.*;



/**
 * A point in the WGS84 coordinates
 * 
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 */
public final class PointWGS84 {
    private final int R=6378137;
    private final double longitude;
    private final double latitude;

    /**
     * @param longitude
     *      The longitude of the point
     * @param latitude
     *      The latitude of the point
     * @throws IllegalArgumentException
     *      If the latitude is not in [-PI/2,PI/2] or the longitude is not in [-PI,PI]
     */
    public PointWGS84(double longitude, double latitude) throws IllegalArgumentException{
        if(longitude<-1*(PI)||longitude>PI||latitude<-1*(PI/2)||latitude>(PI/2)){
            throw new IllegalArgumentException("invalid arguments for the creation of a WGS84 point");
        }
        
        this.latitude=latitude;
        this.longitude=longitude;
    }
    
    /**
     * @return The longitude of the point
     */
    public double longitude(){
        return longitude;
    }
    
    /**
     * @return The latitude of the point
     */
    public double latitude(){
        return latitude;
    }
    
    /**
     * @param that
     *      The point that we want to know the distance to
     * @return The distance between this point and the one in parameter(that)
     */
    public double distanceTo(PointWGS84 that){
        double distance=2*R*asin(sqrt(haversin(latitude-that.latitude())+cos(latitude)*cos(that.latitude())*haversin(longitude-that.longitude())));
        return distance;
    }
    
    /**
     * @param zoom
     *      The zoom level that we want our OSM point to be in 
     * @return The point in OSM coordinates
     * @throws IllegalArgumentException
     *      If zoom is negative
     */
    public PointOSM toOSM(int zoom) throws IllegalArgumentException{
        if(zoom<0){
            throw new IllegalArgumentException("invalid zoom level");
        }
        
        int s=(int)pow(2, zoom+8);
        double x=(s/(2*PI))*(longitude+PI);
        double y=(s/(2*PI))*(PI-asinh(tan(latitude)));
        
        return new PointOSM(zoom,x,y);        
    }
    

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return "("+toDegrees(longitude)+","+toDegrees(latitude)+")";        
    }
}
