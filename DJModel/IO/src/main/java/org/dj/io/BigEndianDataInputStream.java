/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.io;

import java.io.DataInputStream;
import java.io.InputStream;
import org.dj.io.api.EndianDataInputStream;

/**
 *
 * @author djabry
 */
public class BigEndianDataInputStream extends DataInputStream implements EndianDataInputStream{
    
    public BigEndianDataInputStream(InputStream iS){
        
        super(iS);
    }

    @Override
    public boolean isBigEndian() {
       return true;
    }
    
}
