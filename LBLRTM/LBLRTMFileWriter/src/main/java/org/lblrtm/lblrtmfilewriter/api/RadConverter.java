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
public class RadConverter implements VectorConverterFunction{
    private final DJMatrix wn;
    public RadConverter(DJMatrix wn){
        
        this.wn=wn;
        
    }
    
    public double getWavenumberFor(int i){
        
        return this.wn.getValueAt(i, 1);
    }

    @Override
    public double convertValueAt(int i, double val) {
         return Converter.convertToR(val, getWavenumberFor(i));
    }
    
   
    
}
