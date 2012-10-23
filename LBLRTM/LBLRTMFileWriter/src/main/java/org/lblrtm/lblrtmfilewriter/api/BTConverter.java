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
public class BTConverter implements VectorConverterFunction{
    
    private final DJMatrix wavenumber;
    
    public BTConverter(DJMatrix wavenumber){
        
        this.wavenumber=wavenumber;

    }
    
    public double getWavenumberFor(int i){
        
        return wavenumber.getValueAt(i, 1);
    }

    @Override
    public double convertValueAt(int i, double val) {
        return Converter.convertToBT(val, getWavenumberFor(i));
    }
    
}
