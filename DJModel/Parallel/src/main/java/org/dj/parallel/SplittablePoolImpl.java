/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import java.util.Collection;
import java.util.List;
import javolution.util.FastList;
import org.dj.parallel.api.Splittable;

/**
 *
 * @author djabry
 */
public class SplittablePoolImpl implements SplittablePool{
    
    private final List<Splittable> sptbls= new FastList<Splittable>();;


    @Override
    public void addSplittable(Splittable splittable) {
       sptbls.add(splittable);
    }

    @Override
    public void addAllSplittables(Collection<Splittable> splittables) {
        this.sptbls.addAll(splittables);
    }

    @Override
    public void removeSplittable(Splittable splittable) {
        sptbls.remove(splittable);
    }

    @Override
    public List<Splittable> getSplittables() {
       return new FastList<Splittable>(sptbls);
    }

    @Override
    public Double getTotalSize() {
        Double size = 0.0;
        for(Splittable splittable:sptbls){
            size+=splittable.getSize();
        }
        
        return size;
    }
    
}
