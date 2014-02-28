package ch.epfl.isochrone.timetable;

import java.util.Collections;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import ch.epfl.isochrone.timetable.Date.DayOfWeek;
import ch.epfl.isochrone.timetable.Date.Month;

public class TestService {
    // Le "test" suivant n'en est pas un à proprement parler, raison pour
    // laquelle il est ignoré (annotation @Ignore). Son seul but est de garantir
    // que les noms des classes et méthodes sont corrects.
    @Test
    @Ignore
    public void namesAreOk() {
        Date d = new Date(1, Month.JANUARY, 2000);
        Service s = new Service("s",
                d, d,
                Collections.<Date.DayOfWeek> emptySet(),
                Collections.<Date> emptySet(),
                Collections.<Date> emptySet());
        s.name();
        s.isOperatingOn(d);

        Service.Builder sb = new Service.Builder("s", d, d);
        sb.name();
        sb.addOperatingDay(DayOfWeek.MONDAY);
        sb.addExcludedDate(d);
        sb.addIncludedDate(d);
        sb.build();
    }


    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testConstructeurStartingDateAfterEndingDate () {
        Date debut = new Date (23, Month.JANUARY, 2014); 
        Date fin = new Date (10, Month.JANUARY, 2014);
        new Service.Builder("Erreur Date", debut, fin);
    }
    
    @Test
    public void excludedIncludedDate() {
        Date debut = new Date (01, Month.JANUARY, 2014); 
        Date fin = new Date (31, Month.DECEMBER, 2014);
        Service.Builder sb = new Service.Builder("s", debut, fin);
        
        sb.addIncludedDate(new Date(02, Month.APRIL, 2014));
        
        try {
            sb.addExcludedDate(new Date(02, Month.APRIL, 2014));
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}
