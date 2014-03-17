package ch.epfl.isochrone.timetable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.isochrone.geo.PointWGS84;

public final class TimeTableReader {
    public TimeTableReader(String baseResourceName){
        
    }
    
    public TimeTable readTimeTable(){
        
    }
    
    public Graph readGraphForServices(){
        
    }
    
    private Set<Stop> readStops() throws IOException{
        Set<Stop> fileStops=new HashSet<Stop>();
        InputStream stopsStream = getClass().getResourceAsStream("/time-table/stops.csv");
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
}
