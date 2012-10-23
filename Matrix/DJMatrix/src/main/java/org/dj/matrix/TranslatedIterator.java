/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix;

import java.util.Iterator;

/**
 *
 * @author djabry
 */
public abstract class TranslatedIterator implements Iterator<Integer>{
    
    private final Iterator<Integer> childIterator;
    
    public TranslatedIterator(Iterator<Integer> childIterator){
        
        this.childIterator=childIterator;
    }
    
    public abstract int translateIndex(int index);

    @Override
    public boolean hasNext() {
        return childIterator.hasNext();
    }

    @Override
    public Integer next() {
        return this.translateIndex(childIterator.next());
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
