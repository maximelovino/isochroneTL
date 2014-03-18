package ch.epfl.isochrone.timetable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.isochrone.geo.PointWGS84;
import ch.epfl.isochrone.timetable.Date.DayOfWeek;
import ch.epfl.isochrone.timetable.Service.Builder;

public final class TimeTableReader {
    private final String baseResourceName;
    public TimeTableReader(String baseResourceName){
        this.baseResourceName=baseResourceName;
    }

    public TimeTable readTimeTable(){

    }

    public Graph readGraphForServices(){

    }

    private Set<Stop> readStops() throws IOException{
        Set<Stop> fileStops=new HashSet<Stop>();
        InputStream stopsStream = getClass().getResourceAsStream(baseResourceName+"stops.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stopsStream, StandardCharsets.UTF_8));

        String line;
        while((line=reader.readLine())!=null){
            String[] aStop=line.split(";");
            double latitude=Double.parseDouble(aStop[1]);
            double longitude=Double.parseDouble(aStop[2]);
            fileStops.add(new Stop(aStop[0], new PointWGS84(longitude, latitude)));
        }

        reader.close();       
        return fileStops;
    }

    private Set<Service.Builder> readServices() throws IOException{
        Set<Service.Builder> fileServicesBuilders=new HashSet<Service.Builder>();
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
        reader.close();
        return fileServicesBuilders;
    }

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
