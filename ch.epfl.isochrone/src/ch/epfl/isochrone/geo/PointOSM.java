package ch.epfl.isochrone.geo;

import static java.lang.Math.*;

/**
 * Un point en coordonnées OSM
 * 
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class PointOSM {

    private final int zoom;
    private final double x;
    private final double y;
    
    /**
     * @param zoom
     *      Le niveau de zoom du point
     * @param x
     *      La coordonnée selon l'axe x
     * @param y
     *      La coordonnée selon l'axe y
     * @throws IllegalArgumentException
     *      Si le zoom est négatif ou si les coordonnées sortent de la surface de la carte
     */
    public PointOSM(int zoom, double x, double y) throws IllegalArgumentException{
        if (zoom<0||x<0||y<0||x>maxXY(zoom)||y>maxXY(zoom)){
            throw new IllegalArgumentException();
        }
        this.zoom=zoom;
        this.x=x;
        this.y=y;
    }
    
    /**
     * @param zoom
     *      Le niveau de zoom du point 
     * @return
     *      La taille du côté de la surface de la carte
     * @throws IllegalArgumentException
     *      Si le zoom est négatif
     */
    public static int maxXY(int zoom) throws IllegalArgumentException{
        if(zoom<0){
            throw new IllegalArgumentException();
        }
        return (int)pow(2, zoom+8);
    }
    
    /**
     * @return La coordonnée x du point 
     */
    public double x(){
        return x;
    }
    
    /**
     * @return La coordonnée y du point
     */
    public double y(){
        return y;
    }
    
    /**
     * @return La coordonnée x du point arrondie au plus proche entier
     */
    public int roundedX(){
        return (int)round(x);
    }

    /**
     * @return La coordonnée y du point arrondie au plus proche entier
     */
    public int roundedY(){
        return (int)round(y);
    }
    
    /**
     * @return Le niveau de zoom du point
     */
    public int zoom(){
        return zoom;
    }
    
    /**
     * @param newZoom
     *      Le niveau de zoom auquel on veut convertir le point
     * @return Le point avec le niveau de zoom voulu
     * @throws IllegalArgumentException
     *      Si le zoom est négatif
     */
    public PointOSM atZoom(int newZoom) throws IllegalArgumentException{
        if(newZoom<0){
            throw new IllegalArgumentException();
        }
        
        double newX=this.x*pow(2, newZoom-this.zoom);
        double newY=this.y*pow(2, newZoom-this.zoom);
        
        return new PointOSM(newZoom, newX, newY);
    }
    
    /**
     * @return Le point en coordonnées WGS84
     */
    public PointWGS84 toWGS84(){
        int s=(int)pow(2, zoom+8);
        double longitude=(((2*PI)/s)*x)-PI;
        double latitude=atan(sinh(PI-((2*PI)/s)*y));
        
        return new PointWGS84(longitude,latitude);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return "("+zoom+","+x+","+y+")";
    }
}
