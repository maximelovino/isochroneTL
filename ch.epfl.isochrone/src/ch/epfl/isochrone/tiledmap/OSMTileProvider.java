package ch.epfl.isochrone.tiledmap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public final class OSMTileProvider implements TileProvider {
    private final String baseResourceName;
    
    public OSMTileProvider(String baseResourceName){
        this.baseResourceName=baseResourceName;
    }

    @Override
    public Tile tileAt(int zoom, int x, int y) {
        int xt=(int)Math.floor(x/256);
        int yt=(int)Math.floor(y/256);    
        
        BufferedImage image = null;
        try {
            image = ImageIO.read(new URL(baseResourceName+"/"+zoom+"/"+x+"/"+y+".png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            try {
                image=ImageIO.read(getClass().getResource("/images/error-tile.png"));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
                
        return new Tile(zoom,xt,yt,image);
    }

}
