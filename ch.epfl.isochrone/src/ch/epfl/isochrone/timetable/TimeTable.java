package ch.epfl.isochrone.timetable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**A timetable
 * 
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class TimeTable {
    private final Set<Stop> stops;
    private final Collection<Service> services;

    /**
     * @param stops
     *      The set of the stops of a transit line
     * @param services
     *      The set of the services of a transit line
     */
    public TimeTable(Set<Stop> stops, Collection<Service> services){
        this.stops=new HashSet<Stop>(stops);
        this.services=new HashSet<Service>(services);
    }
    
    /**
     * @return The set of the stops of the service (immutable)
     */
    public Set<Stop> stops(){
        return Collections.unmodifiableSet(stops);
    }
    
    /**
     * @param date
     *      The date that we want to know the services that operates on
     * @return
     *      The set of services that operate on that date for the timetable (immutable)
     */
    public Set<Service> servicesForDate(Date date){
        Set<Service> servicesDate=new HashSet<Service>();
        for(Iterator<Service> it=services.iterator();it.hasNext();){
            Service s=it.next();
            if(s.isOperatingOn(date)){
                servicesDate.add(s);
            }
        }
        
        return Collections.unmodifiableSet(servicesDate);
    }
    
    /**A static nested Builder class for TimeTable, uses default empty constructor
     * 
     * @author Maxime Lovino (236726)
     * @author Julie Djeffal (193164)
     * 
     *
     */
    public static class Builder{
        
        private final Set<Stop> stops=new HashSet<Stop>();
        private final Collection<Service> services=new HashSet<Service>();
        
        /**
         * @param newStop
         *      The stop that we want to add
         * @return
         *      The timetable in construction (this) so that we can chain the methods calls
         */
        public Builder addStop(Stop newStop){
            stops.add(newStop);
            return this;
        }
        
        /**
         * @param newService
         *      The service that we want to add 
         * @return
         *      The timetable in construction (this) so that we can chain the methods calls
         */
        public Builder addService(Service newService){
            services.add(newService);
            return this;
        }
        
        /**
         * @return A timetable built from the timetable in construction
         */
        public TimeTable build(){
            return new TimeTable(stops, services);
        }
    }
}
