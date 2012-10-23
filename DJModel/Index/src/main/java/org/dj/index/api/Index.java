/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.index.api;

import java.util.Iterator;

/**
 *
 * @author djabry
 */
public interface Index<C extends Comparable> {
    
    boolean contains(C obj);
    void put(C obj);
    void remove(C obj);
    Iterator<C> allItems();
    C getMaxValue();
    C getMinValue();
    
    
    
    
    
}
