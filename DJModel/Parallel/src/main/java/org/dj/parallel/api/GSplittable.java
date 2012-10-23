/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import com.google.common.collect.Range;

/**
 *
 * @author djabry
 */
public interface GSplittable<G extends GSplittable> {
    
    Range<Integer> getRange();
    void setRange(Range<Integer> r);
    G duplicate();
    
}
