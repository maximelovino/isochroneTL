package ch.epfl.isochrone.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import ch.epfl.isochrone.geo.PointOSM;

public final class TiledMapComponent extends JComponent {
    private int zoomLevel; 
    
    public TiledMapComponent(int zoomLevel){
        if(zoomLevel<10||zoomLevel>19){
            throw new IllegalArgumentException("niveau de zoom incorrect");
        }
        this.zoomLevel=zoomLevel;
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
        
        double maxX=r.getMaxX();
        double maxY=r.getMaxY();
        
        
    }

}
