/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author djabry
 */
public class RangeIterator implements Iterable<Integer>{
    
        private final int start;
        private final int stop;

        public RangeIterator(int start, int stop) {

            this.start = start-1;
            this.stop = stop;
        }

        @Override
        public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {

                private Integer counter = start;

                @Override
                public boolean hasNext() {
                    return (counter != stop);
                }

                @Override
                public Integer next() {
                    if (counter == stop) {
                        throw new NoSuchElementException();
                    }
                    return ++counter;
                }

                @Override
                public void remove() {
                }
            };
        }
    
}
