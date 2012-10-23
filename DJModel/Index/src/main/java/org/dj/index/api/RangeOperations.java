/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.index.api;

import com.google.common.collect.Range;
import java.util.Set;

/**
 *
 * @author djabry
 */
public interface RangeOperations {
    
    <C extends Comparable> C lengthOf(Range<C> r);
    <C extends Comparable> C lengthOf(Iterable<Range<C>> ranges);
    <C extends Comparable> C maxValue(Iterable<Range<C>> ranges);
    <C extends Comparable> C minValue(Iterable<Range<C>> ranges);
    <C extends Comparable> Range<C> duplicate(Range<C> r);
    
    
}
