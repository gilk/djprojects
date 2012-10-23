/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.fileutilities.api;

import org.dj.remote.api.HostInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author djabry
 */
public interface FileUtilities {
    
    void copyFile(DJFile source, DJFile destination) throws IOException;
    //void copyFile(FileInputStream iS, DJFile destination);
    void copyFile(InputStream iS, DJFile destination) throws IOException;
    
    DJFile fromFile(File file,HostInfo hI);
    DJFile fromFile(File file);
    File getTempDir();
    
    void makeSymbolicLink(DJFile source,DJFile destination) throws IOException;
    void makeSymbolicLink(File source, File destination) throws IOException;
    
    void rename(DJFile file, DJFile newFile);
    void mkdir(DJFile file);
    
    boolean exists(DJFile file);
    
    boolean isBeingTransferred(DJFile file);
    void waitForTransferToFinish(DJFile file);
    
    void delete(DJFile file);

    
}
