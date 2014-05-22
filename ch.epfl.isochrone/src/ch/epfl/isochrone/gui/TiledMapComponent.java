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

/**
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class TiledMapComponent extends JComponent {
    private int zoomLevel;
    private final List<TileProvider> providers;
    
    
    /**
     * @param zoomLevel
     * 		zoom level of the map we display
     */
    public TiledMapComponent(int zoomLevel) {
        if (zoomLevel < 10 || zoomLevel > 19) {
            throw new IllegalArgumentException("niveau de zoom incorrect");
        }
        this.zoomLevel = zoomLevel;
        this.providers = new ArrayList<TileProvider>();
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        int sideSize = PointOSM.maxXY(this.zoomLevel);
        return new Dimension(sideSize, sideSize);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        
        Rectangle r = getVisibleRect();
        
                
        double minX = r.getMinX();
        double minY = r.getMinY();
        
        int tileNumLeft = divF((int) minX, 256);
        
        int tileNumUp = divF((int) minY, 256);       
        
        double maxX = r.getMaxX();
        double maxY = r.getMaxY();
        
        int tileNumDown = (divF((int) maxY, 256) + 1);
        
        int tileNumRight = (divF((int) maxX, 256) + 1);        
        
        for (int x = tileNumLeft; x <= tileNumRight; x++) {
            for (int y = tileNumUp; y <= tileNumDown; y++) {
                for (TileProvider tp: providers) {
                    g.drawImage(tp.tileAt(zoomLevel, x, y).getImage(), x * 256,y * 256, null);
                }
            }
        }
        
    }
    

    /**
     * @param tp
     * 		A Tile provider
     */
    public void addTileProvider(TileProvider tp) {
        providers.add(tp);
        repaint();
    }
    
    /**
     * @return the zoom level of the map
     */
    public int zoom() {
        return zoomLevel;
    }
    
    /**
     * @param newZoom 
     * 		A new zoom level
     */
    public void setZoom(int newZoom) {
        if (zoomLevel < 10 || zoomLevel > 19) {
            throw new IllegalArgumentException("niveau de zoom incorrect");
        }
        zoomLevel = newZoom;
    }

}
