/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.io.api;

import java.io.DataInput;
import java.io.IOException;

/**
 *
 * @author djabry
 */
public interface EndianDataInputStream extends DataInput{
    
    void close() throws IOException;
    boolean isBigEndian();
    
    
}
