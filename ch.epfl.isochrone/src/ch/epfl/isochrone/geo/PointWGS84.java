package ch.epfl.isochrone.geo;

import static java.lang.Math.*;
import static ch.epfl.isochrone.math.Math.*;



/**
 * Un point en coordonnées WGS84
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
     *      La longitude du point
     * @param latitude
     *      La latitude du point
     * @throws IllegalArgumentException
     *      Si la latitude n'est pas dans [-PI/2,PI/2] ou que la longitude n'est pas dans [-PI,PI]
     */
    public PointWGS84(double longitude, double latitude) throws IllegalArgumentException{
        if(longitude<-1*(PI)||longitude>PI||latitude<-1*(PI/2)||latitude>(PI/2)){
            throw new IllegalArgumentException();
        }
        
        this.latitude=latitude;
        this.longitude=longitude;
    }
    
    /**
     * @return La longitude du point
     */
    public double longitude(){
        return longitude;
    }
    
    /**
     * @return La latitude du point
     */
    public double latitude(){
        return latitude;
    }
    
    /**
     * @param that
     *      Le point par rapport auquel on veut savoir la distance
     * @return La distance entre les deux points
     */
    public double distanceTo(PointWGS84 that){
        double distance=2*R*asin(sqrt(haversin(latitude-that.latitude())+cos(latitude)*cos(that.latitude())*haversin(longitude-that.longitude())));
        return distance;
    }
    
    /**
     * @param zoom
     *      Le zoom avec lequel on veut le point en coordonnées OSM 
     * @return Le point en coordonnées OSM
     * @throws IllegalArgumentException
     *      Si le zoom est négatif
     */
    public PointOSM toOSM(int zoom) throws IllegalArgumentException{
        if(zoom<0){
            throw new IllegalArgumentException();
        }
        
        int s=(int)pow(2, zoom+8);
        double x=(s/(2*PI))*(longitude+PI);
        double y=(s/(2*PI))*(PI-asinh(tan(latitude)));
        
        return new PointOSM(zoom,x,y);        
    }
    

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString(){
        return "("+toDegrees(longitude)+","+toDegrees(latitude)+")";        
    }
}
