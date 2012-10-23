/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import java.util.List;
import org.dj.executor.api.DJRunnable;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public interface Task<T extends HostInfo> extends HostInfo.Provider<T>, DJRunnable{

    //List<Task> getDependencies();
    void setHostInfo(T hI );
    
    
    
}
