/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task;

/**
 *
 * @author djabry
 */
public class Planck {
    
    private static final double h = 6.626068e-34; //Planck's constant (J-s)
    private static final double c = 2.99792458e8;  //speed of light (m/s)
    private static final double k = 8.314/6.022e23; //Boltzmann's constant (J/K)
    private static final double alpha1 = 2*h*Math.pow(c,2);
    private static final double alpha2 = h*c/k;
    
    public static double getValue(double wn, double temp){
        
        //wn in cm^-1
        //temp in K
        //output value in w/(m^2 cm^-1 sr)

        double v = 1e2*wn; //convert to m
        double radiance = alpha1*Math.pow(v,3)/(Math.exp(alpha2*v/temp)-1);
        return radiance;

    }
    
}
