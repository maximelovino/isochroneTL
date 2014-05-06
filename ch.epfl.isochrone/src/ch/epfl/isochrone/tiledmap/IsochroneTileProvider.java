package ch.epfl.isochrone.tiledmap;

import ch.epfl.isochrone.timetable.FastestPathTree;

public final class IsochroneTileProvider {
    private final FastestPathTree path;
    private final ColorTable colors;
    private final int walkingSpeed;
    
    
    public IsochroneTileProvider(FastestPathTree path, ColorTable colors, int walkingSpeed){
        this.path=path;
        this.colors=colors;
        this.walkingSpeed=walkingSpeed;
    }
    
    public Tile
}
