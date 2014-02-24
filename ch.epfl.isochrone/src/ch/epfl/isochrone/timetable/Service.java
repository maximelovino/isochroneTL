package ch.epfl.isochrone.timetable;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

public final class Service {


    public Service(String name, Date startingDate, Date endingDate, Set<Date.DayOfWeek> operatingDays, Set<Date> excludedDates, Set<Date> includedDates){
        Builder b=new Builder(name, startingDate, endingDate);

        for(Iterator<Date.DayOfWeek> it=operatingDays.iterator();it.hasNext();){
            Date.DayOfWeek d=it.next();
            b.addOperatingDay(d);
        }

        for(Iterator<Date> it=excludedDates.iterator();it.hasNext();){
            Date d=it.next();
            b.addExcludedDate(d);
        }

        for(Iterator<Date> it=includedDates.iterator();it.hasNext();){
            Date d=it.next();
            b.addIncludedDate(d);
        }
        
        b.build();
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
        private final Set<Date.DayOfWeek> operatingDays=new HashSet<Date.DayOfWeek>();;
        private final Set<Date> excludedDates=new HashSet<Date>();
        private final Set<Date> includedDates=new HashSet<Date>();


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
            operatingDays.add(day);
            return this;
        }

        public Builder addExcludedDate(Date date){
            excludedDates.add(date);
            return this;
        }

        public Builder addIncludedDate(Date date){
            includedDates.add(date);
            return this;
        }

        public Service build(){
            return new Service(name, startingDate, endingDate, operatingDays, excludedDates, includedDates);
        }
    }

}
