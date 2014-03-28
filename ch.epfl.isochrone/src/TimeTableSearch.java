import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import ch.epfl.isochrone.timetable.Date;
import ch.epfl.isochrone.timetable.FastestPathTree;
import ch.epfl.isochrone.timetable.Graph;
import ch.epfl.isochrone.timetable.SecondsPastMidnight;
import ch.epfl.isochrone.timetable.Service;
import ch.epfl.isochrone.timetable.Stop;
import ch.epfl.isochrone.timetable.TimeTable;
import ch.epfl.isochrone.timetable.TimeTableReader;


public class TimeTableSearch {

    public static void main(String[] args) {
        String stopName=args[0];
        Stop startingStop=null;
        String[] dateTxt=args[1].split("-");
        Date date=new Date(Integer.parseInt(dateTxt[2]), Integer.parseInt(dateTxt[1]), Integer.parseInt(dateTxt[0]));
        String[] timeTxt=args[2].split(":");
        int time=SecondsPastMidnight.fromHMS(Integer.parseInt(timeTxt[0]), Integer.parseInt(timeTxt[1]), Integer.parseInt(timeTxt[2]));
        
        TimeTableReader reader=new TimeTableReader("/time-table/");
        TimeTable table=null;
        try {
            table = reader.readTimeTable();
        } catch (IOException e) {
            // TODO Auto-generated catch block
           System.out.println("erreur de lecture de l'horaire");
        }
        Set<Service> services=table.servicesForDate(date);
        Set<Stop> stops=table.stops();
        Graph graph=null;
        try {
            graph=reader.readGraphForServices(stops, services, SecondsPastMidnight.fromHMS(0, 5, 0), 1.25);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("erreur de lecture de l'horaire");
        }
        
        for (Iterator iterator = stops.iterator(); iterator.hasNext();) {
            Stop stop = (Stop) iterator.next();
            
            if(stop.name().equals(stopName)){
                startingStop=stop;
            }
            
        }
        
        FastestPathTree fastestPathTree=graph.fastestPath(startingStop, time);
        
    }

}
