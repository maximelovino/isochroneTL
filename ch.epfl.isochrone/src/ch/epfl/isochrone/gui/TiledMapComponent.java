package ch.epfl.isochrone.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import ch.epfl.isochrone.geo.PointOSM;
import ch.epfl.isochrone.tiledmap.TileProvider;

import static ch.epfl.isochrone.math.Math.*;

public final class TiledMapComponent extends JComponent {
    private final int zoomLevel;
    private final List<TileProvider> providers;
    
    public TiledMapComponent(int zoomLevel){
        if(zoomLevel<10||zoomLevel>19){
            throw new IllegalArgumentException("niveau de zoom incorrect");
        }
        this.zoomLevel=zoomLevel;
        this.providers=new ArrayList<TileProvider>();
    }
    
    public Dimension getPreferredSize(){
        int sideSize=PointOSM.maxXY(this.zoomLevel);
        return new Dimension(sideSize, sideSize);
    }
    
    public void paintComponent(Graphics g0){
        Graphics2D g=(Graphics2D) g0;
        
        Rectangle r=getVisibleRect();
        
                
        double minX=r.getMinX();
        double minY=r.getMinY();
        
        int tileNumLeft=divF((int)minX,256);
        
        int tileNumUp=divF((int)minY,256);       
        
        double maxX=r.getMaxX();
        double maxY=r.getMaxY();
        
        int tileNumDown=(divF((int)maxY,256)+1);
        
        int tileNumRight=(divF((int)maxX,256)+1);        
        
        for(int x=tileNumLeft;x<=tileNumRight;x++){
            for(int y=tileNumUp;y<=tileNumDown;y++){
                for(TileProvider tp:providers){
                    g.drawImage(tp.tileAt(zoomLevel, x, y).getImage(),x*256,y*256,null);
                }
            }
        }
        
    }
    

    public void addTileProvider(TileProvider tp){
        providers.add(tp);
        repaint();
    }
    
    public int zoom(){
        return zoomLevel;
    }

}
