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
    
    private static Month intToMonth(int m) throws IllegalArgumentException{
        
        if(m<1||m>12){
            throw new IllegalArgumentException();
        }
        
        Month month=Month.JANUARY;
        
        switch(m){
        case 1:
            break;
        case 2:
            month= Month.FEBRUARY;
            break;
        case 3:
            month= Month.MARCH;
            break;
        case 4:
            month=Month.APRIL;
            break;
        case 5:
            month=Month.MAY;
            break;
        case 6:
            month=Month.JUNE;
            break;
        case 7:
            month=Month.JULY;
            break;
        case 8:
            month=Month.AUGUST;
            break;
        case 9:
            month=Month.SEPTEMBER;
            break;
        case 10:
            month=Month.OCTOBER;
            break;
        case 11:
            month=Month.NOVEMBER;
            break;
        case 12:
            month=Month.DECEMBER;
            break;            
        }
        
        return month;
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
        return ((y%4==0 && y%100!=0)||y%400==0);
    }
    
    private static int daysInMonth(Month m, int y){
        
        int days=0;
        switch(m){
        case JANUARY: case MARCH: case MAY: case JULY: case AUGUST: case OCTOBER: case DECEMBER:
            days= 31;
            break;
        case APRIL: case JUNE: case SEPTEMBER: case NOVEMBER:
            days= 30;
            break;
        case FEBRUARY:
            if(isLeapYear(y)){
                days= 29;
            }else{
                days=28;
            }
            break;            
        }
        
        return days;
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
