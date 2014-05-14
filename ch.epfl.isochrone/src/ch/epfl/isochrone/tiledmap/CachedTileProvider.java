package ch.epfl.isochrone.tiledmap;

public final class CachedTileProvider implements TileProvider {
    private final OSMTileProvider osmTiles;
    TileCache cache;
    
    /**
     * @param osmTiles
     * 		The Address of the server
     * @param cacheSize
     * 		The size of the TileCahe
     */
    public CachedTileProvider(OSMTileProvider osmTiles, int cacheSize){
        this.osmTiles=osmTiles;
        this.cache=new TileCache(cacheSize);
    }

    /* (non-Javadoc)
     * @see ch.epfl.isochrone.tiledmap.TileProvider#tileAt(int, int, int)
     */
    @Override
    public Tile tileAt(int zoom, int x, int y) {
        Tile tile=cache.get(zoom, x, y);
        
        if(tile==null){
            tile=osmTiles.tileAt(zoom, x, y);
            cache.put(zoom, x, y, tile);
        }
        return tile;
    }

}
