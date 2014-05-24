package ch.epfl.isochrone.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JViewport;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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

/**
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
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
    private final IsochroneTileProvider isoTP;
    private final ColorTable colors;
    private final ArrayList<Color> colorsList;
    private final TransparentTileProvider transTP;

    /**
     * @throws IOException
     */
    public IsochroneTL() throws IOException {
        TileProvider bgTileProvider = new CachedTileProvider(new OSMTileProvider(OSM_TILE_URL), 100);
        tiledMapComponent = new TiledMapComponent(INITIAL_ZOOM);
        reader = new TimeTableReader("/time-table/");
        table = reader.readTimeTable();
        services = table.servicesForDate(INITIAL_DATE);
        stops = table.stops();

        for (Iterator<Stop> iterator = stops.iterator(); iterator.hasNext();) {
            Stop stop = (Stop) iterator.next();

            if (stop.name().equals(INITIAL_STARTING_STOP_NAME)){
                startingStop = stop;
            }
        }
        graph = reader.readGraphForServices(stops, services, WALKING_TIME, WALKING_SPEED);
        path = graph.fastestPath(startingStop, INITIAL_DEPARTURE_TIME);
        colorsList = new ArrayList<Color>();
        colorsList.add(new Color(255, 0, 0));
        colorsList.add(new Color(255, 128, 0));
        colorsList.add(new Color(255, 255, 0));
        colorsList.add(new Color(128, 255, 0));
        colorsList.add(new Color(0, 255, 0));
        colorsList.add(new Color(0, 128, 128));
        colorsList.add(new Color(0, 0, 255));
        colorsList.add(new Color(0, 0, 128));
        colorsList.add(new Color(0, 0, 0));
        colors = new ColorTable(SecondsPastMidnight.fromHMS(0, 5, 0), colorsList);
        isoTP = new IsochroneTileProvider(path, colors, WALKING_SPEED);
        transTP = new TransparentTileProvider(0.5, isoTP);
        tiledMapComponent.addTileProvider(bgTileProvider);
        tiledMapComponent.addTileProvider(transTP);
    }

    /**
     * @return a center panel 
     */
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
                mouseStartPosition = e.getLocationOnScreen();
                viewPosition = viewPort.getViewPosition();
            }
        });

        layeredPane.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                int x = (int) Math.round(e.getLocationOnScreen().getX());
                int y = (int) Math.round(e.getLocationOnScreen().getY());

                int windowX = (int) Math.round(viewPosition.getX());
                int windowY = (int) Math.round(viewPosition.getY());

                int mouseX = (int) Math.round(mouseStartPosition.getX());
                int mouseY = (int) Math.round(mouseStartPosition.getY());

                Point point = new Point(windowX - x + mouseX, windowY - y + mouseY);
                viewPort.setViewPosition(point);
            }
        });


        layeredPane.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                mouseStartPosition = e.getPoint();
                Point view = viewPort.getViewPosition().getLocation();
                int newZoom = tiledMapComponent.zoom() - rotation;
                PointOSM point = new PointOSM(tiledMapComponent.zoom(), view.getX() + mouseStartPosition.getX(), view.getY() + mouseStartPosition.getY());

                if (newZoom > 19)
                    newZoom = 19;
                if (newZoom < 11)
                    newZoom = 11;

                point = point.atZoom(newZoom);
                tiledMapComponent.setZoom(newZoom);
                Point pointView = new Point((int) Math.round(point.roundedX() - mouseStartPosition.getX()), (int) Math.round(point.roundedY() - mouseStartPosition.getY()));
                viewPort.setViewPosition(pointView);

            }

        });



        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(layeredPane, BorderLayout.CENTER);
        return centerPanel;
    }

    /**
     * @return a Copyright panel
     */
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
        frame.getContentPane().add(createHeaderPanel(), BorderLayout.PAGE_START);

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

    /**
     * @throws IOException
     */
    private void updateServices() throws IOException {
        Set<Service> servicesTemp = table.servicesForDate(actualDate);

        if (!servicesTemp.equals(services)) {
            services = servicesTemp;
            updateGraph();
        }
    }

    /**
     * @throws IOException
     */
    private void updateGraph() throws IOException {
        graph = reader.readGraphForServices(stops, services, WALKING_TIME, WALKING_SPEED);
        updatePath();
    }

    private void updatePath() {
        path = graph.fastestPath(startingStop, actualTime);
        updateIsoMap();
    }

    private void updateIsoMap() {
        isoTP.setPath(path);
        tiledMapComponent.repaint();
    }

    /**
     * @param newStop 
     * 		A new starting stop
     */
    private void setStop(Stop newStop) {
        if ( !newStop.equals(startingStop)) {
            startingStop = newStop;
            updatePath();
        }
    }

    private void setDateAndTime(java.util.Date date){
        int timeTemp=SecondsPastMidnight.fromJavaDate(date);
        Date dateTemp=new Date(date);

        if(timeTemp<SecondsPastMidnight.fromHMS(4, 0, 0)){
            timeTemp+=SecondsPastMidnight.fromHMS(24, 0, 0);
            dateTemp=dateTemp.relative(-1);
        }

        if (timeTemp != actualTime) {
            actualTime = timeTemp;
            updatePath();
        }

        if (!dateTemp.equals(actualDate)) {
            actualDate = dateTemp;
            try {
                updateServices();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return a Header Panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout());

        JLabel departure = new JLabel("Départ:");
        JLabel timeLabel=new JLabel("Date et heure");
        JSeparator divider = new JSeparator();
        Vector<Stop> stopsVector = new Vector<>(stops);
        Collections.sort(stopsVector, new Comparator<Stop>() {

            @Override
            public int compare(Stop o1, Stop o2) {
                return o1.name().compareTo(o2.name());
            }
        });
        JComboBox<Stop> dropdown = new JComboBox<>(stopsVector);

        dropdown.setSelectedItem(startingStop);

        dropdown.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox dropdown = (JComboBox) e.getSource();
                setStop((Stop)dropdown.getSelectedItem());

            }
        });

        SpinnerDateModel dateSpinner = new SpinnerDateModel();

        dateSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                SpinnerDateModel spinner = (SpinnerDateModel) e.getSource();
                setDateAndTime(spinner.getDate());

            }
        });

        java.util.Date javaDate=actualDate.toJavaDate();
        javaDate.setHours(SecondsPastMidnight.hours(actualTime));
        javaDate.setMinutes(SecondsPastMidnight.minutes(actualTime));
        javaDate.setSeconds(SecondsPastMidnight.seconds(actualTime));
        dateSpinner.setValue(javaDate);


        JSpinner dateSelector = new JSpinner(dateSpinner);

        headerPanel.add(departure);
        headerPanel.add(dropdown);
        headerPanel.add(divider);
        headerPanel.add(timeLabel);
        headerPanel.add(dateSelector);

        return headerPanel;

    }
}

