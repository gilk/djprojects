/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.executor.api;

import java.util.List;
import org.dj.domainmodel.api.DJNodeObject;
import org.netbeans.api.progress.ProgressHandle;

/**
 *
 * @author djabry
 */
public interface DJRunnable extends Runnable, DJNodeObject{
    
    
    //public static final String PROP_COMPLETED = "completed";
    
    
    public static final String PROP_RUNNING = "running";
    public static final String PROP_COMPLETE = "complete";
    
    
    
    boolean isComplete();
    boolean isRunning();
    void updateStatus();
    boolean cancel();
    List<DJRunnable> getDependencies();
    String getDisplayName();
    String getMessage();
   

    
}
