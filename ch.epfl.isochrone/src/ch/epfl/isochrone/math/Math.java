package ch.epfl.isochrone.math;

import static java.lang.Math.*;


/**
 * @author Maxime Lovino (236726)
 * @author Julie Djeffal (193164)
 *
 */
public final class Math {
    
    private Math(){
        
    }

    /**
     * @param x
     *      Le nombre dont on calcule l'arcsinh
     * @return L'arcsinh d'un nombre
     */
    public static double asinh(double x){
        double y=log(x+sqrt(1+pow(x, 2)));
        return y;
    }
    
    /**
     * @param x
     *      Le nombre dont on calcule l'haversin
     * @return L'haversin d'un nombre
     */
    public static double haversin(double x){
        double y= pow(sin(x/2), 2);
        
        return y;
    }
    
    /**
     * @param n
     *      Le dividende
     * @param d
     *      Le diviseur
     * @return Le quotient de la division par défaut
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
     *      Le dividende
     * @param d
     *      Le diviseur
     * @return Le reste de la division par défaut
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
