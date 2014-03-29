package ch.epfl.isochrone.timetable;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class TestTimeTableReader {
    // Le "test" suivant n'en est pas un à proprement parler, raison pour
    // laquelle il est ignoré (annotation @Ignore). Son seul but est de garantir
    // que les noms des classes et méthodes sont corrects.
    @Test
    @Ignore
    public void namesAreOk() throws IOException {
        TimeTableReader r = new TimeTableReader("");
        TimeTable t = r.readTimeTable();
        Graph g = r.readGraphForServices(t.stops(), Collections.<Service>emptySet(), 0, 0d);
        System.out.println(g); // Evite l'avertissement que g n'est pas utilisé
    }
    
    @Test
    public void testStops() throws IOException{
        TimeTableReader reader=new TimeTableReader("/time-table/");
        TimeTable table=reader.readTimeTable();
        Set<Stop> stops=table.stops();
        
        Stop firstStop=(Stop)stops.toArray()[0];
        assertEquals("1er Août",firstStop.name());
        assertEquals(Math.toRadians(46.5367366879), firstStop.position().latitude());
        assertEquals(Math.toRadians(6.58201906962), firstStop.position().longitude());
    }
}
