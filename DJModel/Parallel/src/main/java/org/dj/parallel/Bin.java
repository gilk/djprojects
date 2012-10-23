/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import java.util.List;
import java.util.Set;
import org.dj.parallel.api.Splittable;

/**
 *
 * @author djabry
 */
public interface Bin {
    
    List<? extends Splittable> getSplittables();
    void putSplittable(Splittable Splittable);
    Double getCapacity();
    Double getRemainingCapacity();
}
