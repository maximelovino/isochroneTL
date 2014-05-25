package ch.epfl.isochrone.tiledmap;

/**
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class CachedTileProvider implements TileProvider {
    private final OSMTileProvider osmTiles;
    private final TileCache cache;

    /**
     * @param osmTiles
     * 		An OSM tile provider that we want to cache
     * @param cacheSize
     * 		The size of the TileCache
     */
    public CachedTileProvider(OSMTileProvider osmTiles, int cacheSize) {
        this.osmTiles = osmTiles;
        this.cache = new TileCache(cacheSize);
    }

    /* (non-Javadoc)
     * @see ch.epfl.isochrone.tiledmap.TileProvider#tileAt(int, int, int)
     */
    @Override
    public Tile tileAt(int zoom, int x, int y) {
        Tile tile = cache.get(zoom, x, y);
        //if present in the cache, get it from there, otherwise get it from osm and put in the cache
        if (tile == null) {
            tile = osmTiles.tileAt(zoom, x, y);
            cache.put(zoom, x, y, tile);
        }
        return tile;
    }

}
