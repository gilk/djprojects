/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.executor.api;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public interface Executor {
    

    
    public static final int NUM_THREADS =10;
    
    AssignedCommand createCommand(String command, String[] envP, File dir);
    AssignedCommand createCommand(String command, String[] envP, File dir,HostInfo hI);
    AssignedCommand createCommand(String command, String[] envP, File dir, HostInfo hI, String finishString);
    
    void execute(Runnable runnable);
    
    void execute(List<Runnable> runnables);
    
    InputStream executeCommand(AssignedCommand command);



    
}
