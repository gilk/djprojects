/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.index.api;

import com.google.common.collect.Range;
import java.util.Iterator;

/**
 *
 * @author djabry
 */
public interface IntegerIndex extends Index<Integer>{
    

    int getCardinality();
    int size();
    int getLowerLimit();
    int getUpperLimit();
    Iterator<Range<Integer>> allRanges();
    void putRange(int start, int end) throws IllegalRangeException;
    void putIndex(IntegerIndex ix);
    void removeRange(int start, int end) throws IllegalRangeException;
    IntegerIndex copy();
    
    
    
    
}
