package ch.epfl.isochrone.timetable;

import java.util.Set;
import java.util.HashSet;

public final class Service {
    
    private final String name;
    private final Date startingDate;
    private final Date endingDate;
    private final Set<Date.DayOfWeek> operatingDays;
    private final Set<Date> excludedDates;
    private final Set<Date> includedDates;
    
    public Service(String name, Date startingDate, Date endingDate, Set<Date.DayOfWeek> operatingDays, Set<Date> excludedDates, Set<Date> includedDates){
        
        this.name=name;
        this.startingDate=startingDate;
        this.endingDate=endingDate;
        this.operatingDays= new HashSet<Date.DayOfWeek>(operatingDays);
        this.excludedDates=new HashSet<Date>(excludedDates);
        this.includedDates=new HashSet<Date>(includedDates);
        
    }

    public String name(){
        return this.name;
    }

    public boolean isOperatingOn(Date date){
        boolean operating=false;
        
        if(date.compareTo(this.startingDate)!=-1 && date.compareTo(this.endingDate)!=1 && operatingDays.contains(date.dayOfWeek())){
            operating=true;
        }
        
        if(excludedDates.contains(date)){
            operating=false;
        }
        
        if(includedDates.contains(date)){
            operating=true;
        }
        
//        for(Iterator<Date> it=excludedDates.iterator();it.hasNext();){
//            Date d=it.next();
//            if(d.equals(date)){
//                operating=false;
//            }
//        }
//        
//        for(Iterator<Date> it=includedDates.iterator();it.hasNext();){
//            Date d=it.next();
//            if(d.equals(date)){
//                operating=true;
//            }
//        }
        
        return operating;
    }

    public String toString(){
        return name();
    }

    public static class Builder{
        private final String name;
        private final Date startingDate;
        private final Date endingDate;
        private final Set<Date.DayOfWeek> operatingDays=new HashSet<Date.DayOfWeek>();;
        private final Set<Date> excludedDates=new HashSet<Date>();
        private final Set<Date> includedDates=new HashSet<Date>();


        public Builder(String name, Date startingDate, Date endingDate) throws IllegalArgumentException{
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

        public Builder addExcludedDate(Date date) throws IllegalArgumentException{
            if(date.compareTo(startingDate)==-1||date.compareTo(endingDate)==1||includedDates.contains(date)){
                throw new IllegalArgumentException();
            }
            
            excludedDates.add(date);
            return this;
        }

        public Builder addIncludedDate(Date date) throws IllegalArgumentException{
            if(date.compareTo(startingDate)==-1||date.compareTo(endingDate)==1||excludedDates.contains(date)){
                throw new IllegalArgumentException();
            }      
            
            includedDates.add(date);
            return this;
        }

        public Service build(){
            return new Service(name, startingDate, endingDate, operatingDays, excludedDates, includedDates);
        }
    }

}
