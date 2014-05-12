package ch.epfl.isochrone.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import sun.java2d.loops.DrawRect;
import ch.epfl.isochrone.geo.PointOSM;

public final class TiledMapComponent extends JComponent {
    private int zoomLevel; 
    
    public TiledMapComponent(int zoomLevel){
        this.zoomLevel=zoomLevel;
    }
    
    public Dimension getPreferredSize(){
        int sideSize=PointOSM.maxXY(this.zoomLevel);
        return new Dimension(sideSize, sideSize);
    }
    
    public void paintComponent(Graphics g0){
        
    }

}
