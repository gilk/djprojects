/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;



import java.io.File;
import java.util.Set;




/**
 *
 * @author djabry
 */
public interface Task {
    
    Set<LHostInfo> getHostInfo();
    WorkingDir getWorkingDir();
    TAPE5 getTAPE5();
    WNCollection getWNRanges();
    Set<LBLRTMOutputFile> getOutputFiles();
    Set<String> getFilesToScan(File TAPE6);
    
    

}
