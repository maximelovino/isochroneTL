package ch.epfl.isochrone.timetable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

import ch.epfl.isochrone.geo.PointWGS84;
import ch.epfl.isochrone.timetable.FastestPathTree.Builder;

public class TestFastestPathTree {
    // Le "test" suivant n'en est pas un à proprement parler, raison pour
    // laquelle il est ignoré (annotation @Ignore). Son seul but est de garantir
    // que les noms des classes et méthodes sont corrects.
    @Test
    @Ignore
    public void namesAreOk() {
        Stop stop = null;
        Map<Stop, Integer> arrivalTimes = null;
        Map<Stop, Stop> predecessors = null;
        FastestPathTree f = new FastestPathTree(stop, arrivalTimes, predecessors);
        Stop s = f.startingStop();
        int i = f.startingTime();
        Set<Stop> ss = f.stops();
        i = f.arrivalTime(stop);
        List<Stop> p = f.pathTo(stop);
        System.out.println("" + s + i + ss + p);

        FastestPathTree.Builder fb = new FastestPathTree.Builder(stop, 0);
        fb.setArrivalTime(stop, 0, stop);
        i = fb.arrivalTime(stop);
        f = fb.build();
    }

    // A compléter avec de véritables méthodes de test...
    @Test
    public void arrivalTime(){
        Stop s1=new Stop("test 1",new PointWGS84(0.5, 1));
        Stop s2=new Stop("test 2", new PointWGS84(0.501, 1));
        Stop s3=new Stop("test 3", new PointWGS84(0.499, 1));
        int departureTime=SecondsPastMidnight.fromHMS(9, 0, 0);
        FastestPathTree.Builder pathBuilder=new Builder(s1,departureTime );
        pathBuilder.setArrivalTime(s2, SecondsPastMidnight.fromHMS(9, 10, 0), s1);
        pathBuilder.setArrivalTime(s3, SecondsPastMidnight.fromHMS(9, 50, 0), s2);
        assertEquals(SecondsPastMidnight.fromHMS(9, 50, 0), pathBuilder.arrivalTime(s3));
        FastestPathTree pathTree=pathBuilder.build();
        assertEquals(SecondsPastMidnight.fromHMS(9, 10, 0), pathTree.arrivalTime(s2));
        List<Stop> path=pathTree.pathTo(s3);
        assertEquals("[test 1, test 2, test 3]", path.toString());
        assertEquals(departureTime, pathBuilder.arrivalTime(s1));
        assertEquals(departureTime, pathTree.arrivalTime(s1));
        assertEquals(departureTime, pathTree.startingTime());
    }
}
