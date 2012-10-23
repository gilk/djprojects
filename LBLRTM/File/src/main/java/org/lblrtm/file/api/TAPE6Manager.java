/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.file.api;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author djabry
 */
public interface TAPE6Manager {
    
    TAPE6 getTAPE6ForFile(File f) throws IOException;
    void closeAll();
    void closeFile(File f);
    
    
}
