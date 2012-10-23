/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import java.util.Collection;
import java.util.List;
import org.dj.parallel.api.Splittable;

/**
 *
 * @author djabry
 */
public interface SplittablePool {
    
    void addSplittable(Splittable block);
    void addAllSplittables(Collection<Splittable> blocks);
    void removeSplittable(Splittable block);
    List<Splittable> getSplittables();
    Double getTotalSize();
    
    
    
}
