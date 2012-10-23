/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.api;

import java.io.Serializable;
import java.util.Iterator;
import org.dj.index.api.IntegerIndex;

/**
 *
 * @author djabry
 */
public interface IndexPool extends Serializable, IntegerIndex {
    
    void put(int row, int col);
    void remove(int row, int col);
    
}
