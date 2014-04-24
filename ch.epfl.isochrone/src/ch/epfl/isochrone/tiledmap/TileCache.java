package ch.epfl.isochrone.tiledmap;

import java.util.LinkedHashMap;
import java.util.Map;

import ch.epfl.isochrone.geo.PointOSM;

public final class TileCache {
    private final Map<PointOSM, Tile> cache;
    private final int MAX_SIZE;
    
    public TileCache(int maxSize){
        this.MAX_SIZE=maxSize;
        this.cache=new LinkedHashMap<PointOSM, Tile>(){
            @Override
            protected boolean removeEldestEntry(Map.Entry<PointOSM, Tile> e){
                return size()<MAX_SIZE;
            }
        };
    }
    
    public void put(int zoom, int x, int y, Tile tile){
        cache.put(encodeCoordinates(zoom, x, y), tile);
    }
    
    public Tile get(int zoom, int x, int y){
        Tile tile=cache.get(encodeCoordinates(zoom, x, y));
        
        return tile;
    }
    
    
//    TODO: encode in a long
    private static PointOSM encodeCoordinates(int zoom, int x, int y){
//        Long encoded=new Long("0");
//        
//        encoded+=zoom;
//        
//        encoded=Long.valueOf(zoom) << 64;
//        
//        encoded+=int x;
        
        return new PointOSM(zoom, x, y);
    }

}
