package ch.epfl.isochrone.tiledmap;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import ch.epfl.isochrone.geo.PointOSM;
import ch.epfl.isochrone.timetable.FastestPathTree;
import ch.epfl.isochrone.timetable.Stop;

/**
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class IsochroneTileProvider implements TileProvider {
    private FastestPathTree path;
    private final ColorTable colors;
    private final double walkingSpeed;


    /**
     * @param path 
			The fastest path tree
     * @param colors 
     * 		A color table
     * @param walkingSpeed 
     * 		A walking speed
     */
    public IsochroneTileProvider(FastestPathTree path, ColorTable colors, double walkingSpeed) {
        this.path = path;
        this.colors = colors;
        this.walkingSpeed = walkingSpeed;
    }

    /**
     * @param newPath
     * 		The Fastest path tree that we want to set
     */
    public void setPath(FastestPathTree newPath) {
        this.path = newPath;
    }


    /* (non-Javadoc)
     * @see ch.epfl.isochrone.tiledmap.TileProvider#tileAt(int, int, int)
     */
    @Override
    public Tile tileAt(int zoom, int x, int y) {
        BufferedImage i = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = i.createGraphics();
        //calculation of the size of a pixel
        double xLength = (new PointOSM(zoom, x * 256, y * 256).toWGS84()).distanceTo(new PointOSM(zoom, x * 256 + 1, y * 256).toWGS84());
        double yLength = (new PointOSM(zoom, x * 256, y * 256).toWGS84()).distanceTo(new PointOSM(zoom, x * 256, y * 256 + 1).toWGS84());

        for (int k = colors.slicesNumber() - 1; k > 0; k--) {
            int time = k * colors.slicesLength();

            for (Stop stop: path.stops()) {
                int timeRemainder = time - (path.arrivalTime(stop) - path.startingTime());

                if (timeRemainder > 0) {
                    PointOSM p = stop.position().toOSM(zoom);

                    //coordinates of the circle that we are going to draw
                    double xCoordinate = p.x() - (walkingSpeed * timeRemainder / xLength) - new PointOSM(zoom, x * 256, y * 256).roundedX();
                    double yCoordinate = p.y() - (walkingSpeed * timeRemainder / yLength) - new PointOSM(zoom, x * 256, y * 256).roundedY();


                    g.setColor(colors.colorForSlice(k - 1));

                    g.fill(new Ellipse2D.Double(xCoordinate, yCoordinate, (walkingSpeed * timeRemainder / xLength) * 2, (walkingSpeed * timeRemainder / yLength) * 2));


                }
            }


        }
        return new Tile(zoom, x, y, i);
    }

}
