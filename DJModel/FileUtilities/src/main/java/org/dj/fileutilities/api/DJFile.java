/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.fileutilities.api;

import java.io.File;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public interface DJFile extends HostInfo.Provider {
    
    File getFile();
    String getPath();
    
    
}
