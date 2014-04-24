package ch.epfl.isochrone.tiledmap;

public final class CachedTileProvider implements TileProvider {
    private final OSMTileProvider osmTiles;
    TileCache cache;
    
    public CachedTileProvider(OSMTileProvider osmTiles, int cacheSize){
        this.osmTiles=osmTiles;
        this.cache=new TileCache(cacheSize);
    }

    @Override
    public Tile tileAt(int zoom, int x, int y) {
        Tile tile=cache.get(zoom, x, y);
        
        if(tile==null){
            tile=osmTiles.tileAt(zoom, x, y);
        }
        return tile;
    }

}
