/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import java.io.File;
import java.util.Set;


/**
 *
 * @author djabry
 */
public interface SplittableTask extends Task, Splittable{
    
    @Override
    SplittableTask duplicate();
    //void mergeResults(SplittableTask task);
    Set<File> getOutputFiles();
    void cleanup();
    
}
