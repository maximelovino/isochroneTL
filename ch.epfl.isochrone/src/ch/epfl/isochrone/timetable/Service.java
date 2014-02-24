package ch.epfl.isochrone.timetable;

import java.util.Set;

public final class Service {
       

    public Service(String name, Date startingDate, Date endingDate, Set<Date.DayOfWeek> operatingDays, Set<Date> excludedDates, Set<Date> includedDates){
        
    }
    
    public String name(){
        
    }
    
    public boolean isOperatingOn(Date date){
        
    }
    
    public String toString(){
        
    }
    
    public static class Builder{
        private final String name;
        private final Date startingDate;
        private final Date endingDate;
        
        public Builder(String name, Date startingDate, Date endingDate){
            if(startingDate.compareTo(endingDate)==1){
                throw new IllegalArgumentException();
            }
            
            this.name=name;
            this.startingDate=startingDate;
            this.endingDate=endingDate;
        }
        
        public String name(){
            return this.name;
        }
        
        public Builder addOperatingDay(Date.DayOfWeek day){
            
        }
        
        public Builder addExcludedDate(Date date){
            
        }
        
        public Builder addIncludedDate(Date date){
            
        }
        
        public Service build(){
            
        }
    }
    
}
