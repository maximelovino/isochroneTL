package ch.epfl.isochrone.timetable;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import ch.epfl.isochrone.geo.PointWGS84;

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
        assertEquals(459,stops.toArray().length);
    }

    @Test
    public void testServices() throws IOException{
        TimeTableReader reader=new TimeTableReader("/time-table/");
        TimeTable table=reader.readTimeTable();
        Date date=new Date(28, 01, 2014);
        Set<Service> services=table.servicesForDate(date);
        Set<Stop> stops=table.stops();
        Graph graph=reader.readGraphForServices(stops, services, SecondsPastMidnight.fromHMS(0, 5, 0), 1.25);
        Stop stop=(Stop)stops.toArray()[0];
        FastestPathTree path=graph.fastestPath(stop, SecondsPastMidnight.fromHMS(9, 0, 0));
        assertEquals(3,services.toArray().length);
    }    
}
