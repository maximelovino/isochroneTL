package ch.epfl.isochrone.timetable;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import ch.epfl.isochrone.geo.PointWGS84;

public class TestStop {
    // Le "test" suivant n'en est pas un à proprement parler, raison pour
    // laquelle il est ignoré (annotation @Ignore). Son seul but est de garantir
    // que les noms des classes et méthodes sont corrects.
    @Test
    @Ignore
    public void namesAreOk() {
        Stop s = new Stop("invalid", new PointWGS84(6.57, 46.52));
        s.name();
        s.position();
    }

    // A compléter avec de véritables méthodes de test...
    
    @Test
    public void testNameGetter(){
        Stop test = new Stop("test", new PointWGS84(1, 1));
        String s=test.name();
        boolean x=s.equals("test");
        assertTrue(x);
    }
    
    @Test(expected=java.lang.IllegalArgumentException.class)
    public void invalidPosition(){
        Stop s = new Stop("invalid", new PointWGS84(6.57, 46.52));                
    }
}
