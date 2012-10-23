/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilereader.api;

import java.io.File;
import ucar.nc2.NetcdfFile;


/**
 *
 * @author djabry
 */
public interface LBLRTMFileReader {
    
    NetcdfFile readFile(File file);
    boolean canRead(File file);
    void convertFile(File file, File outputFile);
    Runnable getFileConverter(File in, File out);
    
    
}
