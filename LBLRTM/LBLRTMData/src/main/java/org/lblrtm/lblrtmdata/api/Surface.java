/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

import org.dj.domainmodel.api.DJObject;

/**
 *
 * @author djabry
 */
public interface Surface extends DJObject{
    
    public enum SurfaceType{
        LAMBERTIAN("l"),
        SPECULAR("s");
        
        private String symbol;
        
        SurfaceType(String symbol){
            
            this.symbol=symbol;
        }
        
        public String getSymbol(){
            
            return symbol;
        }

        @Override
        public String toString() {
            
            String n = name().toLowerCase();
            return n.replaceFirst(n.substring(0,1), n.substring(0, 1).toUpperCase());
        } 
        
    }
    
    public static final String PROP_SURFACE_TYPE = "surfaceType";
    public static final String PROP_SURFACE_TEMP = "surfaceTemperature";

    
    double getSurfaceTemperature();
    void setSurfaceTemperature(double temp);
    
    double getEmissionCoefficient(int power);
    void setEmissionCoefficient(int power, double coeff);
    
    double getReflectionCoefficient(int power);
    void setReflectionCoefficient(int power,double coeff);
    
    SurfaceType getSurfaceType();
    void setSurfaceType(SurfaceType s);
    
    public Surface duplicate();
}
