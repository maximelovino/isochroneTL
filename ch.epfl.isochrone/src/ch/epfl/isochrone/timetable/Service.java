package ch.epfl.isochrone.timetable;

import java.util.Set;
import java.util.HashSet;

/**A transit service
 * 
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class Service {

    private final String name;
    private final Date startingDate;
    private final Date endingDate;
    private final Set<Date.DayOfWeek> operatingDays;
    private final Set<Date> excludedDates;
    private final Set<Date> includedDates;

    /**The main constructor for a Service
     * 
     * @param name
     *      The name of the service
     * @param startingDate
     *      The starting date of a service
     * @param endingDate
     *      The ending date of a service
     * @param operatingDays
     *      The days in which the system operates
     * @param excludedDates
     *      The days in which the system exceptionally doesn't operate (holidays for example)
     * @param includedDates
     *      The days in which the system exceptionally operates
     */
    public Service(String name, Date startingDate, Date endingDate, Set<Date.DayOfWeek> operatingDays, Set<Date> excludedDates, Set<Date> includedDates){

        this.name=name;
        this.startingDate=startingDate;
        this.endingDate=endingDate;
        this.operatingDays= new HashSet<Date.DayOfWeek>(operatingDays);
        this.excludedDates=new HashSet<Date>(excludedDates);
        this.includedDates=new HashSet<Date>(includedDates);

    }

    /**
     * @return The name of a service
     */
    public String name(){
        return this.name;
    }

    /**
     * @param date
     *      The date on which we want to know if the system operates
     * @return
     *      True if the Service operates on that date, false otherwise
     */
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

        return operating;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return name();
    }

    /**A static nested Builder class for the class Service
     * 
     * @author Maxime Lovino (236726)
     * @author Julie Djeffal (193164)
     *
     */
    public static class Builder{
        private final String name;
        private final Date startingDate;
        private final Date endingDate;
        private final Set<Date.DayOfWeek> operatingDays;
        private final Set<Date> excludedDates;
        private final Set<Date> includedDates;


        /**
         * @param name
         *      The name of a service
         * @param startingDate
         *      The starting date of a service
         * @param endingDate
         *      The ending date of a service
         * @throws IllegalArgumentException
         *      If the starting date is posterior to the ending date
         */
        public Builder(String name, Date startingDate, Date endingDate) throws IllegalArgumentException{
            if(startingDate.compareTo(endingDate)==1){
                throw new IllegalArgumentException();
            }

            this.name=name;
            this.startingDate=startingDate;
            this.endingDate=endingDate;
            this.operatingDays=new HashSet<Date.DayOfWeek>();
            this.excludedDates=new HashSet<Date>();
            this.includedDates=new HashSet<Date>();
        }

        /**
         * @return The name of the service in construction
         */
        public String name(){
            return this.name;
        }

        /**Add an operating day to the service
         * @param day
         *      The day that we want the Service to operate on
         * @return
         *      The service in construction (this) so that we can chain the methods calls
         */
        public Builder addOperatingDay(Date.DayOfWeek day){
            operatingDays.add(day);
            return this;
        }

        /**
         * @param date
         *      The date that we want to exclude from the operating dates
         * @return
         *      The service in construction (this) so that we can chain the methods calls 
         * @throws IllegalArgumentException
         *      If the date is not included in the service range or if it is already in the includedDates
         */
        public Builder addExcludedDate(Date date) throws IllegalArgumentException{
            if(date.compareTo(startingDate)==-1||date.compareTo(endingDate)==1||includedDates.contains(date)){
                throw new IllegalArgumentException();
            }

            excludedDates.add(date);
            return this;
        }

        /**
         * @param date
         *      The date that we want to include in the operating dates
         * @return
         *      The service in construction (this) so that we can chain the methods calls 
         * @throws IllegalArgumentException
         *      If the date is not included in the service range or if it is already in the excludedDates
         */
        public Builder addIncludedDate(Date date) throws IllegalArgumentException{
            if(date.compareTo(startingDate)==-1||date.compareTo(endingDate)==1||excludedDates.contains(date)){
                throw new IllegalArgumentException();
            }      

            includedDates.add(date);
            return this;
        }

        /**
         * @return A service built from the service in construction
         */
        public Service build(){
            return new Service(name, startingDate, endingDate, operatingDays, excludedDates, includedDates);
        }
    }

}