/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import java.io.File;
import java.util.Set;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public interface GSplittableTask<H extends HostInfo> extends GSplittable<GSplittableTask<H>>, Task<H>{
    
    @Override
    GSplittableTask<H> duplicate();
    GSplittableTask<H> duplicate(boolean linkData);
    //void mergeResults(GSplittableTask<H> task);
    Set<File> getOutputFiles();
    void cleanup();
    
    
    
}
