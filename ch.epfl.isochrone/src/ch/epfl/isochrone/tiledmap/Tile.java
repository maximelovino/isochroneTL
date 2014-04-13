package ch.epfl.isochrone.tiledmap;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public final class Tile{
    private final BufferedImage image;
    private final int x;
    private final int y;
    private final int zoom;
    
    public Tile(int zoom, int x, int y, BufferedImage image){  
        this.zoom=zoom;
        this.x=x;
        this.y=y;
        
        this.image=image;
    }
}
