package ch.epfl.isochrone.timetable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class TimeTable {
    private final Set<Stop> stops;
    private final Collection<Service> services;

    public TimeTable(Set<Stop> stops, Collection<Service> services){
        this.stops=new HashSet<Stop>(stops);
        this.services=new HashSet<Service>(services);
    }
    
    public Set<Stop> stops(){
        return Collections.unmodifiableSet(stops);
    }
    
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
    
    public static class Builder{
        
        private final Set<Stop> stops=new HashSet<Stop>();
        private final Collection<Service> services=new HashSet<Service>();
        
        public Builder addStop(Stop newStop){
            stops.add(newStop);
            return this;
        }
        
        public Builder addService(Service newService){
            services.add(newService);
            return this;
        }
        
        public TimeTable build(){
            return new TimeTable(stops, services);
        }
    }
}
