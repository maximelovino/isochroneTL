package ch.epfl.isochrone.tiledmap;

import static ch.epfl.isochrone.math.Math.*;

import java.awt.image.BufferedImage;

/**
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class TransparentTileProvider extends FilteringTileProvider {
    
    private final double alphaChannel;
    private final TileProvider tp;
    
    /**
     * @param alphaChannel
     * 		Opacity
     * @param tp
     * 		A TileProvider
     */
    public TransparentTileProvider(double alphaChannel, TileProvider tp){
        if(alphaChannel<0||alphaChannel>1){
            throw new IllegalArgumentException("the alpha value is not valid");
        }
        this.tp=tp;
        this.alphaChannel=alphaChannel;
    }

    /* (non-Javadoc)
     * @see ch.epfl.isochrone.tiledmap.TileProvider#tileAt(int, int, int)
     */
    @Override
    public Tile tileAt(int zoom, int x, int y) {
        // TODO Auto-generated method stub
        BufferedImage i=tp.tileAt(zoom, x, y).getImage();
        BufferedImage transformed=new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        for(int j=0;j<i.getHeight();j++){
            for(int k=0;k<i.getWidth();k++){
                int color=i.getRGB(k, j);
                color= transformARGB(color);
                transformed.setRGB(k, j, color);
            }
        }
        
        return new Tile(zoom, x, y, transformed);
    }

    /* (non-Javadoc)
     * @see ch.epfl.isochrone.tiledmap.FilteringTileProvider#transformARGB(int)
     * 
     */
    @Override
    public int transformARGB(int colorARGB) {        
        int a=(1/255)*modF(divF(colorARGB,(int)Math.pow(2,24)),(int)Math.pow(2,8));
        int r=(1/255)*modF(divF(colorARGB,(int)Math.pow(2,16)),(int)Math.pow(2,8));
        int g=(1/255)*modF(divF(colorARGB,(int)Math.pow(2,8)),(int)Math.pow(2,8));
        int b=(1/255)*modF(colorARGB,(int)Math.pow(2,8));
        
        return (int)Math.pow(2, 24)*(int)Math.round(255*a)+(int)Math.pow(2,16)*(int)Math.round(255*r)+(int)Math.pow(2, 8)*(int)Math.round(255*g)+(int)Math.round(255*b);
    }

}
