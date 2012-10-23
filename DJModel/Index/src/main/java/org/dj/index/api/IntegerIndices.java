/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.index.api;

import com.google.common.collect.Range;

/**
 *
 * @author djabry
 */
public interface IntegerIndices {
    IntegerIndex singleton(int index);
    IntegerIndex fromRange(int start, int end);
    IntegerIndex fromRange(Range<Integer> rng);
    IntegerIndex fromRanges(Iterable<Range<Integer>> rngs);
    IntegerIndex empty(int size);
    IntegerIndex empty(int start, int finish);
}
