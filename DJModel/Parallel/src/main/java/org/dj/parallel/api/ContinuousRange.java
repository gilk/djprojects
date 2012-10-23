/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

/**
 *
 * @author djabry
 */
public interface ContinuousRange {
    
    double getStart();
    double getEnd();
    
    boolean intersects(ContinuousRange r);
    boolean contains(ContinuousRange r);
    
    void setStart(double start);
    void setEnd(double end);
    
    double getSize();
    
    ContinuousRange duplicate();
}
