package ch.epfl.isochrone.tiledmap;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
@SuppressWarnings("unused")
public final class Tile {
    private final BufferedImage image;
    private final int x;
    private final int y;
    private final int zoom;
    
    
    /**
     * @param zoom 
     * 		The zoom of the Tile
     * @param x
     * 		The first coordinate of the Tile
     * @param y
     * 		The second coordinate of the Tile
     * @param image
     * 		The image of the Tile
     */
    public Tile(int zoom, int x, int y, BufferedImage image) {  
        this.zoom = zoom;
        this.x = x;
        this.y = y;
        
        this.image = image;
    }
    
    /**
     * @return The image of the Tile
     */
    public BufferedImage getImage() {
        return this.image;
    }
}
