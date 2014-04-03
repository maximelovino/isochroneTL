package ch.epfl.isochrone.timetable;

import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import ch.epfl.isochrone.geo.PointWGS84;

public class TestGraph {
    // Le "test" suivant n'en est pas un à proprement parler, raison pour
    // laquelle il est ignoré (annotation @Ignore). Son seul but est de garantir
    // que les noms des classes et méthodes sont corrects.
    @Test
    @Ignore
    public void namesAreOk() {
        // Graph n'a aucune méthode publique à ce stade...

        Set<Stop> stops = null;
        Stop stop = null;
        Graph.Builder gb = new Graph.Builder(stops);
        gb.addTripEdge(stop, stop, 0, 0);
        gb.addAllWalkEdges(0, 0);
        gb.build();
    }
    
    @Test
    public void testFastestPath(){
        Stop s1=new Stop("test 1",new PointWGS84(0.5, 1));
        Stop s2=new Stop("test 2", new PointWGS84(0.501, 1));
        Stop s3=new Stop("test 3", new PointWGS84(0.499, 1));
        Set<Stop> stops= new HashSet<Stop>();
        stops.add(s1);
        stops.add(s2);
        stops.add(s3);
        Graph.Builder buildingGraph=new Graph.Builder(stops);
        buildingGraph.addAllWalkEdges(SecondsPastMidnight.fromHMS(0, 30, 0), 30);
        buildingGraph.addTripEdge(s1, s2, SecondsPastMidnight.fromHMS(9, 0, 0), SecondsPastMidnight.fromHMS(9, 25, 0));
        buildingGraph.addTripEdge(s1, s2, SecondsPastMidnight.fromHMS(9, 25, 0), SecondsPastMidnight.fromHMS(9, 50, 0));
        Graph graph=buildingGraph.build();
        FastestPathTree path=graph.fastestPath(s1, SecondsPastMidnight.fromHMS(9,25, 0));
        int arrivalTime=path.arrivalTime(s2);
        assertEquals(SecondsPastMidnight.fromHMS(9, 25, 0), arrivalTime);        
    }
}
