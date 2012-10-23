/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import org.dj.matrix.api.DJMatrix;

/**
 *
 * @author djabry
 */
public class JacobianBTConverter implements VectorConverterFunction{
    
    private final DJMatrix rad;
    private final DJMatrix wn;
    
    
    public JacobianBTConverter(DJMatrix rad,DJMatrix wn){
        
        this.rad=rad;
        this.wn=wn;
        
        
    }

    @Override
    public double convertValueAt(int i, double val) {
        
        double w = wn.getValueAt(i, 1)*100.0;
        double r = rad.getValueAt(i, 1)*100.0;
        double a1 = Converter.ALPHA1;
        double a2 = Converter.ALPHA2;
        double outVal = (100.0*a1*a2*Math.pow(w, 4))/(((Math.pow(r, 2))+a1*r*Math.pow(w, 4))*Math.pow((Math.log(1+(a1*Math.pow(w, 2)/r))),2));
        return outVal;
    }
    
}
