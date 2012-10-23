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
public interface VectorConverterFunction {
    
    double convertValueAt(int i, double val);
}
