package ch.epfl.isochrone.tiledmap;

public interface TileProvider {

    Tile tileAt(int zoom, int x, int y);
    
}
