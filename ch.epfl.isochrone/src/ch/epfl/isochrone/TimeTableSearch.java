package ch.epfl.isochrone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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

    public static void main(String[] args) throws IOException {
        String stopName=args[0];
        Stop startingStop=null;
        String[] dateTxt=args[1].split("-");
        Date date=new Date(Integer.parseInt(dateTxt[2]), Integer.parseInt(dateTxt[1]), Integer.parseInt(dateTxt[0]));
        String[] timeTxt=args[2].split(":");
        int time=SecondsPastMidnight.fromHMS(Integer.parseInt(timeTxt[0]), Integer.parseInt(timeTxt[1]), Integer.parseInt(timeTxt[2]));

        TimeTableReader reader=new TimeTableReader("/time-table/");
        TimeTable table=reader.readTimeTable();
        Set<Service> services=table.servicesForDate(date);
        Set<Stop> stops=table.stops();


        for (Iterator<Stop> iterator = stops.iterator(); iterator.hasNext();) {
            Stop stop = (Stop) iterator.next();

            if(stop.name().equals(stopName)){
                startingStop=stop;
            }

        }

        Graph graph=reader.readGraphForServices(stops, services, SecondsPastMidnight.fromHMS(0, 5, 0), 1.25);

        FastestPathTree fastestPathTree=graph.fastestPath(startingStop, time);
        
        List<Stop> stopsList=new ArrayList<Stop>(fastestPathTree.stops());
        Collections.sort(stopsList, new Comparator<Stop>() {

            @Override
            public int compare(Stop o1, Stop o2) {
                return o1.name().compareTo(o2.name());
            }
        });


        for (Stop stop:stopsList) {
            System.out.print(stop+" : ");
            
            System.out.print(SecondsPastMidnight.toString(fastestPathTree.arrivalTime(stop)));
            System.out.println();
            System.out.print("via: ");
            List<Stop> path=fastestPathTree.pathTo(stop);
            System.out.println(path);
            
        }

    }

}
