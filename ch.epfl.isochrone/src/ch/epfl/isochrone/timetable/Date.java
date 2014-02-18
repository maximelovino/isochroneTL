package ch.epfl.isochrone.timetable;

public final class Date {

    private final int day;
    private final Month month;
    private final int year;
    private final int date;
    
    public Date (int day, Month month, int year) throws IllegalArgumentException{
        
    }
    
    public Date (int day, int month, int year) throws IllegalArgumentException{
        
    }
    
    public Date(java.util.Date date){
        
    }
    
    public int day(){
       return day; 
    }
    
    public Month month(){
        return month;
    }
    
    public int intMonth(){
            
       
    }
    
    public int year(){
        
    }
    
    public DayOfWeek dayOfWeek(){
        
    }
    
    public Date relative(int daysDiff){
        
    }
    
    public java.util.Date toJavaDate(){
        
    }
    
    public String toString(){
        
    }
    
    public boolean equals(Object that){
        
    }
    
    public int hashCode(){
        
    }
    
    public int compareTo(Date that){
        
    }
    
    private static Month inToMonth(int m){
        
    }
    
    private static int monthToInt(Month m){
        
        int i=0;
        switch(m){
        case JANUARY:
            i=1;
            break;
        case FEBRUARY:
            i=2;
            break;
        case MARCH:
            i=3;
            break;
        case APRIL:
            i=4;
            break;
        case MAY:
            i=5;
            break;
        case JUNE:
            i=6;
            break;
        case JULY:
            i=7;
            break;
        case AUGUST:
            i=8;
            break;
        case SEPTEMBER:
            i=9;
            break;
        case OCTOBER:
            i=10;
            break;
        case NOVEMBER:
            i=11;
            break;
        case DECEMBER:
            i=12;
            break;
        }
        
        return i;
        
    }
    
    private static boolean isLeapYear(int y){
        
    }
    
    private static int daysInMonth(Month m, int y){
        
    }
    
    private static int dateToFixed(int d, Month m, int y){
        
    }
    
    private static Date fixedToDate(int n){
        
    }
    
    private int fixed(){
        
    }
    
    
    public enum DayOfWeek{
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
    
    public enum Month{
        JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
    }
}
