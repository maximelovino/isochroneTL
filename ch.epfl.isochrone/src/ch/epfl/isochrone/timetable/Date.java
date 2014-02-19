package ch.epfl.isochrone.timetable;

import static ch.epfl.isochrone.math.Math.*;

public final class Date {

    private final int day;
    private final Month month;
    private final int year;
    
    public Date (int day, Month month, int year) throws IllegalArgumentException{
        if(day<1||day>daysInMonth(month,year)){
            throw new IllegalArgumentException();
        }
        
        this.day=day;
        this.month=month;
        this.year=year;
        
    }
    
    public Date (int day, int month, int year){
        this(day, intToMonth(month), year);
    }
    
    public Date(java.util.Date date){        
        this(date.getDate(),intToMonth(date.getMonth()+1),date.getYear()+1900);
    }
    
    public int day(){
       return day; 
    }
    
    public Month month(){
        return month;
    }
    
    public int intMonth(){
        return monthToInt(month);       
    }
    
    public int year(){
        return year;
    }
    
    public DayOfWeek dayOfWeek(){
        int k=modF(dateToFixed(day,month,year),7);
        
        DayOfWeek dayWeek=DayOfWeek.MONDAY;
        
//      Sunday at 0
        switch(k){
        case 0:
            dayWeek=DayOfWeek.SUNDAY;
            break;
        case 1:
            dayWeek=DayOfWeek.MONDAY;
            break;
        case 2:
            dayWeek=DayOfWeek.TUESDAY;
            break;
        case 3:
            dayWeek=DayOfWeek.WEDNESDAY;
            break;
        case 4:
            dayWeek=DayOfWeek.THURSDAY;
            break;
        case 5:
            dayWeek=DayOfWeek.FRIDAY;
            break;
        case 6:
            dayWeek=DayOfWeek.SATURDAY;
            break;
        }
        
        return dayWeek;
    }
    
    public Date relative(int daysDiff){
        return fixedToDate(this.fixed()+daysDiff);
    }
    
    public java.util.Date toJavaDate(){
        return new java.util.Date(this.year()-1900,this.intMonth()-1,this.day);
    }
    
    public String toString(){
        return String.format("%04d-%02d-%02d",this.year(),this.intMonth(),this.day());
    }
    
    public boolean equals(Object that){
        if (that instanceof Date){
            if(this.compareTo((Date)that)==0){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    public int hashCode(){
        return this.fixed();
    }
    
    public int compareTo(Date that){
        if(this.fixed()<that.fixed()){
            return -1;
        }else if(this.fixed()==that.fixed()){
            return 0;
        }else{
            return 1;
        }
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
        return ((modF(y,4)==0 && modF(y,100)!=0)||modF(y,400)==0);
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
        int y0=y-1;
        int c;
        if(monthToInt(m)<=2){
            c=0;
        }else if(monthToInt(m)>2 && isLeapYear(y)){
            c=-1;
        }else{
            c=-2;
        }
        
        int g=365*y0+divF(y0,4)-divF(y0,100)+divF(y0,400)+divF((367*monthToInt(m)-362),12)+c+d;
        
        return g;
    }
    
    private static Date fixedToDate(int n){
        
        int d0=n-1;
        int n400=divF(d0,146097);
        int d1=modF(d0,146097);
        int n100=divF(d1,36524);
        int d2=modF(d1,36524);
        int n4=divF(d2,1461);
        int d3=modF(d2,1461);
        int n1=divF(d3,365);
        int y0=400*n400+100*n100+4*n4+n1;
        
        int y;
        
        if(n100==4||n1==4){
            y=y0;
        }else{
            y=y0+1;
        }
        
        int p=n-dateToFixed(1,intToMonth(1),y);
        int c;
        
        if(n<dateToFixed(1,intToMonth(3),y)){
            c=0;
        }else if(n>=dateToFixed(1,intToMonth(3),y) && isLeapYear(y)){
            c=1;
        }else{
            c=2;
        }
        
        int m=divF((12*(p+c)+373),367);
        
        int d=n-dateToFixed(1,intToMonth(m),y)+1;
        
        return new Date(d,m,y);     
        
    }
    
    private int fixed(){
        return dateToFixed(this.day(),this.month(),this.year());
    }
    
    
    public enum DayOfWeek{
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
    
    public enum Month{
        JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
    }
}
