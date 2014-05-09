package ch.epfl.isochrone.tiledmap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ch.epfl.isochrone.timetable.FastestPathTree;

public final class IsochroneTileProvider implements TileProvider{
    private final FastestPathTree path;
    private final ColorTable colors;
    private final int walkingSpeed;


    public IsochroneTileProvider(FastestPathTree path, ColorTable colors, int walkingSpeed){
        this.path=path;
        this.colors=colors;
        this.walkingSpeed=walkingSpeed;
    }


    @Override
    public Tile tileAt(int zoom, int x, int y) {
        path.
        
        BufferedImage i =
                new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = i.createGraphics();
    }

}
