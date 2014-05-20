package ch.epfl.isochrone.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import ch.epfl.isochrone.geo.PointOSM;
import ch.epfl.isochrone.geo.PointWGS84;
import ch.epfl.isochrone.tiledmap.CachedTileProvider;
import ch.epfl.isochrone.tiledmap.ColorTable;
import ch.epfl.isochrone.tiledmap.IsochroneTileProvider;
import ch.epfl.isochrone.tiledmap.OSMTileProvider;
import ch.epfl.isochrone.tiledmap.TileProvider;
import ch.epfl.isochrone.tiledmap.TransparentTileProvider;
import ch.epfl.isochrone.timetable.Date;
import ch.epfl.isochrone.timetable.Date.Month;
import ch.epfl.isochrone.timetable.FastestPathTree;
import ch.epfl.isochrone.timetable.Graph;
import ch.epfl.isochrone.timetable.SecondsPastMidnight;
import ch.epfl.isochrone.timetable.Service;
import ch.epfl.isochrone.timetable.Stop;
import ch.epfl.isochrone.timetable.TimeTable;
import ch.epfl.isochrone.timetable.TimeTableReader;

public final class IsochroneTL {
    private static final String OSM_TILE_URL = "http://b.tile.openstreetmap.org/";
    private static final int INITIAL_ZOOM = 11;
    private static final PointWGS84 INITIAL_POSITION = new PointWGS84(Math.toRadians(6.476), Math.toRadians(46.613));
    private static final String INITIAL_STARTING_STOP_NAME = "Lausanne-Flon";
    private static final int INITIAL_DEPARTURE_TIME = SecondsPastMidnight.fromHMS(6, 8, 0);
    private static final Date INITIAL_DATE = new Date(1, Month.OCTOBER, 2013);
    private static final int WALKING_TIME = 5 * 60;
    private static final double WALKING_SPEED = 1.25;

    private final TiledMapComponent tiledMapComponent;
    private final Set<Stop> stops;
    private final TimeTable table;
    private final TimeTableReader reader;
    private Point mouseStartPosition;
    private Point viewPosition;

    private Date actualDate=INITIAL_DATE;
    private Set<Service> services;
    private Graph graph;
    private int actualTime=INITIAL_DEPARTURE_TIME;
    private FastestPathTree path;
    private Stop startingStop;
    private IsochroneTileProvider isoTP;
    private final ColorTable colors;
    private final ArrayList<Color> colorsList;
    private TransparentTileProvider transTP;

    public IsochroneTL() throws IOException {
        TileProvider bgTileProvider = new CachedTileProvider(new OSMTileProvider(OSM_TILE_URL),100);
        tiledMapComponent = new TiledMapComponent(INITIAL_ZOOM);

        reader=new TimeTableReader("/time-table/");
        table=reader.readTimeTable();
        services=table.servicesForDate(INITIAL_DATE);
        stops=table.stops();

        for (Iterator<Stop> iterator = stops.iterator(); iterator.hasNext();) {
            Stop stop = (Stop) iterator.next();

            if(stop.name().equals(INITIAL_STARTING_STOP_NAME)){
                startingStop=stop;
            }
        }


        graph=reader.readGraphForServices(stops, services, WALKING_TIME, WALKING_SPEED);

        path=graph.fastestPath(startingStop, INITIAL_DEPARTURE_TIME);


        colorsList=new ArrayList<Color>();

        colorsList.add(new Color(255,0,0));
        colorsList.add(new Color(255, 128, 0));
        colorsList.add(new Color(255, 255, 0));
        colorsList.add(new Color(128, 255, 0));
        colorsList.add(new Color(0, 255, 0));
        colorsList.add(new Color(0, 128, 128));
        colorsList.add(new Color(0, 0, 255));
        colorsList.add(new Color(0, 0, 128));
        colorsList.add(new Color(0, 0, 0));

        colors=new ColorTable(SecondsPastMidnight.fromHMS(0, 5, 0), colorsList);

        isoTP=new IsochroneTileProvider(path, colors, WALKING_SPEED);

        transTP=new TransparentTileProvider(0.5, isoTP);

        tiledMapComponent.addTileProvider(bgTileProvider);
        tiledMapComponent.addTileProvider(transTP);
    }

    private JComponent createCenterPanel() {
        final JViewport viewPort = new JViewport();
        viewPort.setView(tiledMapComponent);
        PointOSM startingPosOSM = INITIAL_POSITION.toOSM(tiledMapComponent.zoom());
        viewPort.setViewPosition(new Point(startingPosOSM.roundedX(), startingPosOSM.roundedY()));

        final JPanel copyrightPanel = createCopyrightPanel();

        final JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(400, 300));

        layeredPane.add(viewPort, new Integer(0));
        layeredPane.add(copyrightPanel, new Integer(1));

        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Rectangle newBounds = layeredPane.getBounds();
                viewPort.setBounds(newBounds);
                copyrightPanel.setBounds(newBounds);

                viewPort.revalidate();
                copyrightPanel.revalidate();
            }
        });


        layeredPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e){
                mouseStartPosition=e.getLocationOnScreen();
                viewPosition=viewPort.getViewPosition();
            }
        });

        layeredPane.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                int x=(int) Math.round(e.getLocationOnScreen().getX());
                int y=(int) Math.round(e.getLocationOnScreen().getY());

                int windowX=(int) Math.round(viewPosition.getX());
                int windowY=(int) Math.round(viewPosition.getY());

                int mouseX=(int) Math.round(mouseStartPosition.getX());
                int mouseY=(int) Math.round(mouseStartPosition.getY());

                Point point=new Point(windowX-x+mouseX, windowY-y+mouseY);
                viewPort.setViewPosition(point);
            }
        });


        layeredPane.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation=e.getWheelRotation();
                mouseStartPosition=e.getPoint();
                Point view=viewPort.getViewPosition().getLocation();
                int newZoom=tiledMapComponent.zoom()-rotation;
                PointOSM point=new PointOSM(tiledMapComponent.zoom(), view.getX()+mouseStartPosition.getX(), view.getY()+mouseStartPosition.getY());

                if(newZoom>19)
                    newZoom=19;
                if(newZoom<10)
                    newZoom=10;

                point=point.atZoom(newZoom);
                tiledMapComponent.setZoom(newZoom);
                Point pointView=new Point((int)Math.round(point.roundedX()-mouseStartPosition.getX()), (int) Math.round(point.roundedY()-mouseStartPosition.getY()));
                viewPort.setViewPosition(pointView);

            }

        });



        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(layeredPane, BorderLayout.CENTER);
        return centerPanel;
    }

    private JPanel createCopyrightPanel() {
        Icon tlIcon = new ImageIcon(getClass().getResource("/images/tl-logo.png"));
        String copyrightText = "Données horaires 2013. Source : Transports publics de la région lausannoise / Carte : © contributeurs d'OpenStreetMap";
        JLabel copyrightLabel = new JLabel(copyrightText, tlIcon, SwingConstants.CENTER);
        copyrightLabel.setOpaque(true);
        copyrightLabel.setForeground(new Color(1f, 1f, 1f, 0.6f));
        copyrightLabel.setBackground(new Color(0f, 0f, 0f, 0.4f));
        copyrightLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 5, 0));

        JPanel copyrightPanel = new JPanel(new BorderLayout());
        copyrightPanel.add(copyrightLabel, BorderLayout.PAGE_END);
        copyrightPanel.setOpaque(false);
        return copyrightPanel;
    }

    private void start() {
        JFrame frame = new JFrame("Isochrone TL");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(createCenterPanel(), BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new IsochroneTL().start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setDate(Date date) throws IOException{
        if(!date.equals(actualDate)){
            actualDate=date;
            updateServices();
        }
    }

    private void updateServices() throws IOException{
        Set<Service> servicesTemp=table.servicesForDate(actualDate);

        if(!servicesTemp.equals(services)){
            services=servicesTemp;
            updateGraph();
        }
    }

    private void updateGraph() throws IOException{
        graph=reader.readGraphForServices(stops, services, WALKING_TIME, WALKING_SPEED);
    }

    private void setTime(int time){
        if(time!=actualTime){
            actualTime=time;
            updatePath();
        }
    }

    private void updatePath(){
        path=graph.fastestPath(startingStop, actualTime);
        updateIsoMap();
    }

    private void updateIsoMap(){
        isoTP=new IsochroneTileProvider(path, colors, WALKING_SPEED);
        transTP=new TransparentTileProvider(0.5, isoTP);
    }

    private void setStop(String stopName){
        if(!stopName.equals(startingStop.name())){
            for (Iterator<Stop> iterator = stops.iterator(); iterator.hasNext();) {
                Stop stop = (Stop) iterator.next();

                if(stop.name().equals(stopName)){
                    startingStop=stop;
                    updatePath();
                }
            }

        }
    }
}
