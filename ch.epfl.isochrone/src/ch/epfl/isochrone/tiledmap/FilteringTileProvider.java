package ch.epfl.isochrone.tiledmap;

/**
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public abstract class FilteringTileProvider implements TileProvider {

    /**
     * @param colorARGB
     *      A color in int ARGB format
     * @return
     *      The color transformed
     */
    public abstract int transformARGB(int colorARGB);

}
