/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.api;

import java.io.Closeable;
import java.io.File;
import java.util.Set;
import org.dj.parallel.api.ContinuousRange;
import org.dj.parallel.api.GSplittableTask;
import org.dj.parallel.api.SplittableTask;
import org.lblrtm.host.api.LBLRTMHostInfo;
import org.lblrtm.lblrtmfilewriter.api.DJTAPE5;


/**
 *
 * @author djabry
 */
public interface LBLRTMTask extends GSplittableTask<LBLRTMHostInfo>, Closeable{
    
    void setConvertFiles(boolean tf);
    boolean isConvertFiles();
    //Set<File> getOutputFiles();
    DJTAPE5 getTAPE5();
    void setLocalDir(File dir);
    File getLocalDir();
    LBLRTMTask duplicate(boolean linkedProfile);
    
    public static final String LOCK_FILE_NAME = "lock";
    
    
}
