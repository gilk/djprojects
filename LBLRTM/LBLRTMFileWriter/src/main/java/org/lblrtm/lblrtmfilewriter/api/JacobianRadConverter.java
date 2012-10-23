/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import org.dj.matrix.api.DJMatrix;
import org.dj.matrix.api.DJMatrixIterator;

/**
 *
 * @author djabry
 */
public class JacobianRadConverter extends JacobianBTConverter{
    
    static DJMatrix convertBTToRad(DJMatrix bt,DJMatrix wn){
        
        DJMatrix rad = bt.copy();
        
        DJMatrixIterator iterator = bt.iterator(true);
        DJMatrixIterator iterator1 = wn.iterator(true);
        
        while(iterator.hasNext()&&iterator1.hasNext()){
            
            double t = iterator.next();
            double w = iterator1.next();
            
            rad.setValueAt(iterator.getRow(), iterator.getColumn(), Converter.convertToR(t, w));
            
        }
        
        return rad;
        
    }
    
    public JacobianRadConverter(DJMatrix bt, DJMatrix wn){
        
        super(convertBTToRad(bt,wn),wn);

    }

    @Override
    public double convertValueAt(int i, double val) {
        return 1/super.convertValueAt(i, val);
    }
    
    
    
}
