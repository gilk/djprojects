/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.api;

import java.util.Iterator;

/**
 *
 * @author djabry
 */
public interface DJMatrixIterator extends Iterator<Double>{
    
    int getIndex();
    int getRow();
    int getColumn();
    void set(double val);
    boolean isAllElements();
    
    
}
