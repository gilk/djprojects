/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import java.util.List;

/**
 *
 * @author djabry
 */
public interface Bins {
    
    List<Bin> getBins();
    void putSplittables(SplittablePool blocks);
    double getTolerance();
    void setTolerance(double tol);
    
    
}
