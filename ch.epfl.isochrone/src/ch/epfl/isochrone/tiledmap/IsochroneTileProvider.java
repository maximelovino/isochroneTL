package ch.epfl.isochrone.tiledmap;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import ch.epfl.isochrone.geo.PointOSM;
import ch.epfl.isochrone.timetable.FastestPathTree;
import ch.epfl.isochrone.timetable.Stop;

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
        BufferedImage i=new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g=i.createGraphics();

        double xLength= (new PointOSM(zoom, x*256,y*256).toWGS84()).distanceTo(new PointOSM(zoom, x*256+1, y*256).toWGS84());
        double yLength= (new PointOSM(zoom, x*256,y*256).toWGS84()).distanceTo(new PointOSM(zoom, x*256, y*256+1).toWGS84());

        for(int k=colors.slicesNumber()-2;k>0;k--){
            int time=k*colors.slicesLength();

            for(Stop stop: path.stops()){
                int timeRemainder=time-(path.arrivalTime(stop)-path.startingTime());

                if(timeRemainder>0){
                    PointOSM p=stop.position().toOSM(zoom);


                    double xCoordinate=p.x()-(walkingSpeed*timeRemainder/xLength)-new PointOSM(zoom, x*256,y*256).roundedX();
                    double yCoordinate=p.y()-(walkingSpeed*timeRemainder/yLength)-new PointOSM(zoom, x*256,y*256).roundedY();

                    g.setColor(colors.colorForSlice(k-1));

                    g.fill(new Ellipse2D.Double(xCoordinate,yCoordinate,(walkingSpeed*timeRemainder/xLength)*2,(walkingSpeed*timeRemainder/yLength)*2));


                }
            }


        }
        return new Tile(zoom,x,y,i);
    }

}
