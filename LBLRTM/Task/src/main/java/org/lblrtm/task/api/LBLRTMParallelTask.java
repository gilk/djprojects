/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.api;

import java.io.Closeable;
import java.io.File;
import java.util.Set;
import org.dj.parallel.api.ContinuousRange;
import org.dj.parallel.api.GParallelTask;
import org.lblrtm.host.api.LBLRTMHostInfo;

/**
 *
 * @author djabry
 */
public interface LBLRTMParallelTask extends GParallelTask<LBLRTMTask,LBLRTMHostInfo,TaskResult>, Closeable{
    

    LBLRTMParallelTask duplicate();
    LBLRTMParallelTask duplicate(boolean linkedProfile);
    Set<File> getOutputFiles();
    Set<ContinuousRange> getWavelengthRanges();
    public static final String RESULT_FILE_NAME="Result.nc";
    
    

    
}


