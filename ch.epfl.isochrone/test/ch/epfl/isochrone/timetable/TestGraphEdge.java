package ch.epfl.isochrone.timetable;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Ignore;
import org.junit.Test;

import ch.epfl.isochrone.geo.PointWGS84;

public class TestGraphEdge {
    // Le "test" suivant n'en est pas un à proprement parler, raison pour
    // laquelle il est ignoré (annotation @Ignore). Son seul but est de garantir
    // que les noms des classes et méthodes sont corrects.
    @Test
    public void namesAreOk() {
        int i1 = GraphEdge.packTrip(0, 0);
        i1 = GraphEdge.unpackTripDepartureTime(0);
        i1 = GraphEdge.unpackTripDuration(0);
        i1 = GraphEdge.unpackTripArrivalTime(0) + i1;
        Stop s = null;
        GraphEdge e = new GraphEdge(s, 0, Collections.<Integer>emptySet());
        s = e.destination();
        i1 = e.earliestArrivalTime(0);

        GraphEdge.Builder b = new GraphEdge.Builder(s);
        b.setWalkingTime(0);
        b.addTrip(0, 0);
        e = b.build();
    }

    // A compléter avec de véritables méthodes de test...
    
    @Test(expected=IllegalArgumentException.class)
    public void setIncorrectWalkingTime(){
        GraphEdge.Builder b=new GraphEdge.Builder(new Stop("test",new PointWGS84(1, 1)));
        b.setWalkingTime(-2);       
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void invalidTrip(){
        GraphEdge.Builder b=new GraphEdge.Builder(new Stop("test",new PointWGS84(1, 1)));
        b.addTrip(-1, 2);
    }
    
    @Test
    public void arrivalTime(){
        GraphEdge.Builder b=new GraphEdge.Builder(new Stop("test",new PointWGS84(1, 1)));
        b.setWalkingTime(-1);
        b.addTrip(SecondsPastMidnight.fromHMS(9, 10, 0),SecondsPastMidnight.fromHMS(9, 30, 0));
        GraphEdge.Builder c=new GraphEdge.Builder(new Stop("test2",new PointWGS84(1, 1)));
        c.addTrip(SecondsPastMidnight.fromHMS(9, 10, 0),SecondsPastMidnight.fromHMS(9, 30, 0));
        c.addTrip(SecondsPastMidnight.fromHMS(9, 30, 0),SecondsPastMidnight.fromHMS(9, 50, 0));
        GraphEdge g=b.build();
        
        int w=g.earliestArrivalTime(SecondsPastMidnight.fromHMS(9, 40, 0));
        assertEquals(SecondsPastMidnight.INFINITE,w);
        c.setWalkingTime(SecondsPastMidnight.fromHMS(0, 25, 0));
        GraphEdge h=c.build();
        int x=h.earliestArrivalTime(SecondsPastMidnight.fromHMS(9, 10, 0));
        assertEquals(SecondsPastMidnight.fromHMS(9, 30, 0),x);
        int y=h.earliestArrivalTime(SecondsPastMidnight.fromHMS(9, 30, 0));
        assertEquals(SecondsPastMidnight.fromHMS(9, 50, 0),y);
        int z=h.earliestArrivalTime(SecondsPastMidnight.fromHMS(9, 12, 0));
        assertEquals(SecondsPastMidnight.fromHMS(9, 37, 0),z);
    }

}
