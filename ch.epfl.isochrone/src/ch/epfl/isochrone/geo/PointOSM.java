package ch.epfl.isochrone.geo;

import static java.lang.Math.*;

/**
 * A point in OSM coordinates
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
     *      The level of zoom of the point
     * @param x
     *      The x-axis coordinate
     * @param y
     *      The y-axis coordinate
     * @throws IllegalArgumentException
     *      If the zoom is negative or the point is outside of the map
     */
    public PointOSM(int zoom, double x, double y) throws IllegalArgumentException {
        if (zoom < 0 || x < 0 || y < 0 || x > maxXY(zoom) || y > maxXY(zoom)) {
            throw new IllegalArgumentException("invalid arguments for the creation of an OSM point");
        }
        this.zoom = zoom;
        this.x = x;
        this.y = y;
    }
    
    /**
     * @param zoom
     *      The zoom level of the map
     * @return
     *      The size of a side of the map
     * @throws IllegalArgumentException
     *      If the zoom is negative
     */
    public static int maxXY(int zoom) throws IllegalArgumentException {
        if(zoom < 0) {
            throw new IllegalArgumentException("invalid zoom level");
        }
        return (int) pow(2, zoom+8);
    }
    
    /**
     * @return The x-axis coordinate of the point 
     */
    public double x() {
        return x;
    }
    
    /**
     * @return The x-axis coordinate rounded to the closest integer
     */
    public int roundedX() {
        return (int) round(x);
    }

    /**
     * @return The y-axis coordinate of the point
     */
    public double y() {
        return y;
    }
    
    /**
     * @return The y-axis coordinate rounded to the closest integer
     */
    public int roundedY() {
        return (int) round(y);
    }
    
    /**
     * @return The zoom level of the point
     */
    public int zoom() {
        return zoom;
    }
    
    /**
     * @param newZoom
     *      The zoom level that we want to convert the point to
     * @return The point at that level of zoom
     * @throws IllegalArgumentException
     *      If the new level of zoom is negative
     */
    public PointOSM atZoom(int newZoom) throws IllegalArgumentException {
        if (newZoom < 0) {
            throw new IllegalArgumentException("invalid zoom level");
        }
        
        double newX = this.x * pow(2, newZoom - this.zoom);
        double newY = this.y * pow(2, newZoom - this.zoom);
        
        return new PointOSM(newZoom, newX, newY);
    }
    
    /**
     * @return The point in WGS84 coordinates
     */
    public PointWGS84 toWGS84() {
        int s = (int) pow(2, zoom + 8);
        double longitude = (((2 * PI) / s) * x) - PI;
        double latitude = atan(sinh(PI - ((2 * PI) / s) * y));
        
        return new PointWGS84(longitude, latitude);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(" + zoom + "," + x + "," + y + ")";
    }
}
