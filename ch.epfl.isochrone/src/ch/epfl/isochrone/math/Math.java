package ch.epfl.isochrone.math;

import static java.lang.Math.*;


/**
 * Math utilities
 * 
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class Math {
    
    /**
     * Empty constructor so that the class can't be instantiate
     */
    private Math(){
        
    }

    /**
     * @param x
     *      The number that we want to calculate the arcsinh of
     * @return arcsinh(x)
     */
    public static double asinh(double x){
        double y=log(x+sqrt(1+pow(x, 2)));
        return y;
    }
    
    /**
     * @param x
     *      The number that we want to calculate the haversin of
     * @return haversin(x)
     */
    public static double haversin(double x){
        double y= pow(sin(x/2), 2);
        
        return y;
    }
    
    /**
     * @param n
     *      The dividend
     * @param d
     *      The divisor
     * @return The quotient of the division by default
     */
    public static int divF(int n, int d){
        int i;
        
        if (Integer.signum(n%d)==-Integer.signum(d)){
            i=1;
        }else{
            i=0;
        }
        
        int quotient=n/d-i;
        
        return quotient;
    }
    
    /**
     * @param n
     *      The dividend
     * @param d
     *      The divisor
     * @return The rest of the division by default
     */
    public static int modF(int n, int d){
        int i;
        
        if (Integer.signum(n%d)==-Integer.signum(d)){
            i=1;
        }else{
            i=0;
        }
        
        int reste=(n%d)+(i*d);
        
        return reste;
    }
    
}
