package ch.epfl.isochrone.tiledmap;

import java.util.LinkedHashMap;
import java.util.Map;

import ch.epfl.isochrone.geo.PointOSM;

/**
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class TileCache {
    private final Map<Long, Tile> cache;
    private final int MAX_SIZE;
    
    /**
     * @param maxSize
     *      The max size of the cache
     */
    public TileCache(int maxSize) {
        this.MAX_SIZE = maxSize;
        this.cache = new LinkedHashMap<Long, Tile>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Tile> e) {
                return size() < MAX_SIZE;
            }
        };
    }
    
    /**
     * @param zoom
     * 		The zoom of the tile
     * @param x
     * 		The first coordinate of the tile
     * @param y
     * 		The second coordinate of the tile
     * @param tile 
     * 		The tile 
     */
    public void put(int zoom, int x, int y, Tile tile) {
        cache.put(encodeCoordinates(zoom, x, y), tile);
    }
    
    /**
     * @param zoom
     * 		The zoom
     * @param x
     * 		The first coordinate
     * @param y 
     * 		The second coordinate
     * @return The tile associated by the coordinates if it exists
     */
    public Tile get(int zoom, int x, int y) {
        Tile tile = cache.get(encodeCoordinates(zoom, x, y));
        
        return tile;
    }
    
    
    /**
     * @param zoom 
     * 		Zoom of the OSM Point
     * @param x
     * 		First coordinate of the OSM Point
     * @param y
     * 		Second coordinate of the OSM Point
     * @return
     * 		The three parameters encoded in a Long
     */
    private static Long encodeCoordinates(int zoom, int x, int y) {
    	Long encoded = new Long(x *  10000000 + y * 100 + zoom);
    	
        return encoded;
    }

}
