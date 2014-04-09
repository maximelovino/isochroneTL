package ch.epfl.isochrone.timetable;

import static ch.epfl.isochrone.math.Math.*;

/**
 * Seconds past midnight for dealing with time
 * 
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class SecondsPastMidnight {
    public static final int INFINITE=200000;
    
    /**
     * Empty constructor so that the class can't be instantiate
     */
    private SecondsPastMidnight(){
        
    }
    
    /**
     * @param hours
     *      The hours value in a time
     * @param minutes
     *      The minutes value in a time
     * @param seconds
     *      The seconds value in a time
     * @return The number of seconds passed since midnight
     * @throws IllegalArgumentException
     *      If hours are not in [0,30[ or minutes or seconds are not in [0,60[
     */
    public static int fromHMS(int hours, int minutes, int seconds) throws IllegalArgumentException{
        if(hours<0||hours>=30||minutes<0||minutes>=60||seconds<0||seconds>=60){
            throw new IllegalArgumentException("the time is invalid");
        }
        
        int s=hours*3600+minutes*60+seconds;
        return s;
    }
    
    /**
     * @param date
     *      A date of the form java.util.Date
     * @return The number of seconds passed since midnight
     */
    @SuppressWarnings("deprecation")
    public static int fromJavaDate(java.util.Date date){
        return fromHMS(date.getHours(),date.getMinutes(),date.getSeconds());
    }
    
    /**
     * @param spm
     *      The number of seconds passed since midnight of a time
     * @return The number of hours in that time
     * @throws IllegalArgumentException
     *      If the time exceeds 29:59:59
     */
    public static int hours(int spm) throws IllegalArgumentException{
        if(spm<0||spm>fromHMS(29,59,59)){
            throw new IllegalArgumentException("the time is invalid");
        }
        
        return divF(spm,3600);
    }
    
    /**
     * @param spm
     *      The number of seconds passed since midnight of a time
     * @return The number of minutes in that time
     * @throws IllegalArgumentException
     *      If the time exceeds 29:59:59
     */
    public static int minutes(int spm){
        spm=spm-(hours(spm)*3600);
        
        return divF(spm,60);
    }
    
    /**
     * @param spm
     *      The number of seconds passed since midnight of a time
     * @return The number of seconds in that time
     * @throws IllegalArgumentException
     *      If the time exceeds 29:59:59
     */
    public static int seconds(int spm){
        spm=spm-(hours(spm)*3600)-(minutes(spm)*60);
        
        return spm;
    }
    
    /**
     * @param spm
     *      A time in seconds past midnight format
     * @return A string of the time in the format hh:mm:ss
     */
    public static String toString(int spm){
        return String.format("%02d:%02d:%02d", hours(spm),minutes(spm),seconds(spm));
    }
}
