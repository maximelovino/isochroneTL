package ch.epfl.isochrone.tiledmap;

/**
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public interface TileProvider {

    /**
     * @param zoom
     *      The zoom level
     * @param x
     *      The x coordinate
     * @param y
     *      The y coordinate
     * @return
     *      The tile at these coordinates
     */
    Tile tileAt(int zoom, int x, int y);
    
}
