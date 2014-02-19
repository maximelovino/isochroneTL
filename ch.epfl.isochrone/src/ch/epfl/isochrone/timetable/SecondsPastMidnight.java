package ch.epfl.isochrone.timetable;

import static ch.epfl.isochrone.math.Math.*;

public final class SecondsPastMidnight {
    public static final int INFINITE=200000;
    
    private SecondsPastMidnight(){
        
    }
    
    public static int fromHMS(int hours, int minutes, int seconds) throws IllegalArgumentException{
        if(hours<0||hours>=30||minutes<0||minutes>=60||seconds<0||seconds>=60){
            throw new IllegalArgumentException();
        }
        
        int s=hours*3600+minutes*60+seconds;
        return s;
    }
    
    public static int fromJavaDate(java.util.Date date){
        return fromHMS(date.getHours(),date.getMinutes(),date.getSeconds());
    }
    
    public static int hours(int spm) throws IllegalArgumentException{
        if(spm<0||spm>fromHMS(29,59,59)){
            throw new IllegalArgumentException();
        }
        
        return divF(spm,3600);
    }
    
    public static int minutes(int spm){
        spm=spm-(hours(spm)*3600);
        
        return divF(spm,60);
    }
    
    public static int seconds(int spm){
        spm=spm-(hours(spm)*3600)-(minutes(spm)*60);
        
        return spm;
    }
    
    public static String toString(int spm){
        return String.format("%02d:%02d:%02d", hours(spm),minutes(spm),seconds(spm));
    }
}
