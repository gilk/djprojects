/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

import org.dj.domainmodel.api.DJNodeObjectAbstr;

/**
 *
 * @author djabry
 */
public class SurfaceAbstr extends DJNodeObjectAbstr implements Surface{
    
    public SurfaceAbstr(){
        
        super();
        
        this.setPropertyValue(PROP_SURFACE_TEMP, 300.0);
        this.setPropertyValue(PROP_SURFACE_TYPE, SurfaceType.SPECULAR);
        
        for(int i = 0;i<3;i++){
            
            this.setPropertyValue(generateEmissionCoeffName(i), 0.0);
            this.setPropertyValue(generateReflectionCoeffName(i), 0.0);
            
        }
        
        this.setEmissionCoefficient(0, 1.0);
        
    }
    
    
    public static String generateEmissionCoeffName(int i){
        
        return "emissionCoefficient"+i;
    }
    
    public static String generateReflectionCoeffName(int i ){
        
        return "reflectionCoefficient"+i;
    }

    @Override
    public double getSurfaceTemperature() {
        
        return (Double)this.getPropertyValue(PROP_SURFACE_TEMP);
    }

    @Override
    public void setSurfaceTemperature(double temp) {
        this.setPropertyValue(PROP_SURFACE_TEMP, temp);
    }

    @Override
    public double getEmissionCoefficient(int power) {
       return (Double)this.getPropertyValue(generateEmissionCoeffName(power));
    }

    @Override
    public void setEmissionCoefficient(int power, double coeff) {
        this.setPropertyValue(generateEmissionCoeffName(power), coeff);
    }

    @Override
    public double getReflectionCoefficient(int power) {
        return (Double)this.getPropertyValue(generateReflectionCoeffName(power));
    }

    @Override
    public void setReflectionCoefficient(int power, double coeff) {
        this.setPropertyValue(generateReflectionCoeffName(power), coeff);
    }

    @Override
    public SurfaceType getSurfaceType() {
        return (SurfaceType)this.getPropertyValue(PROP_SURFACE_TYPE);
    }

    @Override
    public void setSurfaceType(SurfaceType s) {
        this.setPropertyValue(PROP_SURFACE_TYPE, s);
    }

    @Override
    public Surface duplicate() {
        Surface s = new SurfaceAbstr();
        
        for(int i = 0;i<3;i++){
            s.setEmissionCoefficient(i, this.getEmissionCoefficient(i));
            s.setReflectionCoefficient(i, this.getReflectionCoefficient(i));
        }
        
        s.setSurfaceTemperature(this.getSurfaceTemperature());
        s.setSurfaceType(this.getSurfaceType());
        
        return s;
        
    }
    
}
