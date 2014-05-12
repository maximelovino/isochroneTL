package ch.epfl.isochrone.tiledmap;

public abstract class FilteringTileProvider implements TileProvider {
    
    public abstract int transformARGB(int colorARGB);

}
