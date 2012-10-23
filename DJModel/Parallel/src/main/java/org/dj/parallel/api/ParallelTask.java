/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.dj.executor.api.DJRunnable;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public interface ParallelTask extends DJRunnable{
    
    public static final String PROP_HOSTS  ="hosts";
    public static final String PROP_OPTIMISING_RATIO = "optimisingRatio";
    public static final String PROP_RANGES = "ranges";
    
    Set<? extends HostInfo> getHosts();
    SplittableTask getTask();
    Set<ContinuousRange> getRanges();
    Map<ContinuousRange,Set<SplittableTask>> getCompletedTasks();
    double getOptimisingRatio();
    void setRanges(Set<ContinuousRange> ranges);
    
    
    
    
    
}
