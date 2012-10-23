/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.io.api;

import java.io.DataInput;
import java.io.File;

/**
 *
 * @author djabry
 */
public interface EndianDataInputFactory {
    
    EndianDataInputStream getDataInput(File file, boolean bigEndian);
    
}
