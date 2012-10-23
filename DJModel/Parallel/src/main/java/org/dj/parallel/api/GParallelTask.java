/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import com.google.common.collect.Range;
import java.util.Set;
import org.dj.executor.api.DJRunnable;
import org.dj.index.api.IntegerIndex;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public interface GParallelTask<T extends GSplittableTask<H>, H extends HostInfo,R extends Result<R>> extends DJRunnable{
    
    public static final String PROP_HOSTS  ="hosts";
    public static final String PROP_OPTIMISING_RATIO = "optimisingRatio";
    public static final String PROP_RANGES = "ranges";
    
    Set<H> getHosts();
    T getTask();
    
    
    IntegerIndex getRanges();
    //Range<Integer> getTaskRanges();
    //Set<GCompletedTask<T,H,R>> getCompletedTasks();
    //double getOptimisingRatio();
    GCompletedTask<T,H,R> getCompletedTask();
    void setRanges(IntegerIndex ranges);
    //void setRangesFromIndex(IntegerIndex ranges);
    
}
