package ch.epfl.isochrone.timetable;

import java.util.Collections;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import ch.epfl.isochrone.geo.PointWGS84;
import ch.epfl.isochrone.timetable.Date.DayOfWeek;
import ch.epfl.isochrone.timetable.Date.Month;

public class TestTimeTable {
    // Le "test" suivant n'en est pas un à proprement parler, raison pour
    // laquelle il est ignoré (annotation @Ignore). Son seul but est de garantir
    // que les noms des classes et méthodes sont corrects.
    @Test
    @Ignore
    public void namesAreOk() {
        TimeTable t = new TimeTable(Collections.<Stop> emptySet(),
                Collections.<Service> emptySet());
        t.stops();
        t.servicesForDate(new Date(1, Month.JANUARY, 2000));

        TimeTable.Builder b = new TimeTable.Builder();
        b.addStop(new Stop("s", new PointWGS84(0, 0)));
        Date d = new Date(1, Month.APRIL, 2000);
        b.addService(new Service("s", d, d, Collections.<DayOfWeek> emptySet(),
                Collections.<Date> emptySet(), Collections.<Date> emptySet()));
        b.build();
    }

    // A compléter avec de véritables méthodes de test...
    @Test (expected= java.lang.UnsupportedOperationException.class)
    public void testImmuabilite(){
        TimeTable.Builder b= new TimeTable.Builder();
        b.addStop(new Stop("s", new PointWGS84(1,1)));
        
        Service.Builder s= new Service.Builder("test", new Date(1, Month.JANUARY, 2014), new Date(31, Month.DECEMBER, 2014));
        s.addOperatingDay(DayOfWeek.WEDNESDAY);
        Service testService=s.build();
        b.addService(testService);
        
        TimeTable testTable=b.build();
        Set<Stop> stops=testTable.stops();
        stops.clear();
    }
}
