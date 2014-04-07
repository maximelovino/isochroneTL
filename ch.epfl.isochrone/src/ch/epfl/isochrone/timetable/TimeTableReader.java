package ch.epfl.isochrone.timetable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.epfl.isochrone.geo.PointWGS84;
import ch.epfl.isochrone.timetable.Date.DayOfWeek;
import ch.epfl.isochrone.timetable.Service.Builder;

/**
 * The class that will read our files
 * 
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class TimeTableReader {
    private final String baseResourceName;
    
    /**
     * @param baseResourceName
     *      The path that contains our files
     */
    public TimeTableReader(String baseResourceName){
        this.baseResourceName=baseResourceName;
    }

    /**
     * @return A timetable based on the files
     * @throws IOException
     */
    public TimeTable readTimeTable() throws IOException{
        return new TimeTable(readStops(),readServices());
    }

    /**
     * @param stops
     *      The set of stops
     * @param services
     *      The set of services
     * @param walkingTime
     *      The maximum time a person can walk
     * @param walkingSpeed
     *      The speed at which a person walks
     * @return
     *      A graph from our files for the services and stops in parameter
     * @throws IOException
     */
    public Graph readGraphForServices(Set<Stop> stops, Set<Service> services, int walkingTime, double walkingSpeed) throws IOException{
        Graph.Builder buildingGraph=new Graph.Builder(stops);
        InputStream graphStream=getClass().getResourceAsStream(baseResourceName+"stop_times.csv");
        BufferedReader reader=new BufferedReader(new InputStreamReader(graphStream, StandardCharsets.UTF_8));
        buildingGraph.addAllWalkEdges(walkingTime, walkingSpeed);

        String line;
        while((line=reader.readLine())!=null){
            String[] aStopTime=line.split(";");
            String fromStopTxt=aStopTime[1];
            String toStopTxt=aStopTime[3];
            int departureTime=Integer.parseInt(aStopTime[2]);
            int arrivalTime=Integer.parseInt(aStopTime[4]);
            Stop fromStop=null;
            Stop toStop=null;
            for (Iterator<Service> it = services.iterator(); it.hasNext();) {
                Service service = (Service) it.next();
                if(service.name().equals(aStopTime[0])){
                    for (Iterator<Stop> iterator = stops.iterator(); iterator.hasNext();) {
                        Stop theStop = (Stop) iterator.next();
                        if(theStop.name().equals(fromStopTxt)){
                            fromStop=theStop;
                        }
                        if(theStop.name().equals(toStopTxt)){
                            toStop=theStop;
                        }
                    }
                    buildingGraph.addTripEdge(fromStop, toStop, departureTime, arrivalTime);
                }
            }
        }
        reader.close();
        return buildingGraph.build();
    }

    /**
     * @return The set of stops from the file
     * @throws IOException
     */
    private Set<Stop> readStops() throws IOException{
        Set<Stop> stopSet=new HashSet<Stop>();
        InputStream stopsStream = getClass().getResourceAsStream(baseResourceName+"stops.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stopsStream, StandardCharsets.UTF_8));

        String line;
        while((line=reader.readLine())!=null){
            String[] aStop=line.split(";");
            double latitude=Math.toRadians(Double.parseDouble(aStop[1]));
            double longitude=Math.toRadians(Double.parseDouble(aStop[2]));
            stopSet.add(new Stop(aStop[0], new PointWGS84(longitude, latitude)));
        }

        reader.close();
        stopsStream.close();
        
        
        return Collections.unmodifiableSet(stopSet);
    }

    /**
     * @return The set of services from the files
     * @throws IOException
     */
    private Set<Service> readServices() throws IOException{
        Set<Service.Builder> fileServicesBuilders=new HashSet<Service.Builder>();
        Set<Service> fileService=new HashSet<Service>();
        InputStream servicesStream=getClass().getResourceAsStream(baseResourceName+"calendar.csv");
        BufferedReader reader= new BufferedReader(new InputStreamReader(servicesStream, StandardCharsets.UTF_8));

        String line;
        while((line=reader.readLine())!=null){
            String[] aService=line.split(";");
            String name=aService[0];
            String beginDate=aService[8];
            int beginYear=Integer.parseInt(beginDate.substring(0, 4));
            int beginMonth=Integer.parseInt(beginDate.substring(4, 6));
            int beginDay=Integer.parseInt(beginDate.substring(6));
            String endingDate=aService[9];
            int endingYear=Integer.parseInt(endingDate.substring(0, 4));
            int endingMonth=Integer.parseInt(endingDate.substring(4, 6));
            int endingDay=Integer.parseInt(endingDate.substring(6));
            Service.Builder theService=new Service.Builder(name,new Date(beginDay, beginMonth, beginYear), new Date(endingDay, endingMonth, endingYear));

            for(int i=1;i<8;i++){
                if(Integer.parseInt(aService[i])==1){
                    theService.addOperatingDay(getOperatingDayFromServiceFile(i));
                }
            }
            fileServicesBuilders.add(theService);
        }
        servicesStream=getClass().getResourceAsStream(baseResourceName+"calendar_dates.csv");

        while((line=reader.readLine())!=null){
            String[] aServiceSpecial=line.split(";");
            String name=aServiceSpecial[0];
            String date=aServiceSpecial[1];
            int year=Integer.parseInt(date.substring(0, 4));
            int month=Integer.parseInt(date.substring(4, 6));
            int day=Integer.parseInt(date.substring(6));
            for (Iterator<Service.Builder> it = fileServicesBuilders.iterator(); it.hasNext();) {
                Service.Builder service = (Service.Builder) it.next();
                String serviceName=service.name();

                if(name.equals(serviceName)){
                    if(Integer.parseInt(aServiceSpecial[2])==1){
                        service.addIncludedDate(new Date(day, month, year));
                    }
                    if(Integer.parseInt(aServiceSpecial[2])==2){
                        service.addExcludedDate(new Date(day, month, year));
                    }
                }

            }
        }

        reader.close();
        servicesStream.close();

        for (Iterator<Service.Builder> it = fileServicesBuilders.iterator(); it.hasNext();) {
            Service.Builder service = (Service.Builder) it.next();
            fileService.add(service.build());
        }
        return Collections.unmodifiableSet(fileService);
    }

    /**
     * @param n
     *      The index of a day of week
     * @return
     *      The day at that index, monday=1,..., sunday=7
     */
    private static DayOfWeek getOperatingDayFromServiceFile(int n){

        switch(n){
        case 1:
            return DayOfWeek.MONDAY;
        case 2:
            return DayOfWeek.TUESDAY;
        case 3:
            return DayOfWeek.WEDNESDAY;
        case 4:
            return DayOfWeek.THURSDAY;
        case 5:
            return DayOfWeek.FRIDAY;
        case 6:
            return DayOfWeek.SATURDAY;
        case 7:
            return DayOfWeek.SUNDAY;
        default:
            throw new Error();
        }
    }
}
