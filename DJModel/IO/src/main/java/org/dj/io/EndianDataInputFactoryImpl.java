/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.io;

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dj.io.api.EndianDataInputFactory;
import org.dj.io.api.EndianDataInputStream;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider (service = EndianDataInputFactory.class)
public class EndianDataInputFactoryImpl implements EndianDataInputFactory{

    @Override
    public EndianDataInputStream getDataInput(File file, boolean bigEndian) {
        try {
            
            BufferedInputStream bS = new BufferedInputStream(new FileInputStream(file));
            
            if(bigEndian){
                
                return new BigEndianDataInputStream(bS);
               
                
            }else{
                
                
                return new LittleEndianDataInputStream(bS);
            }
        
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EndianDataInputFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    return null;
    }
    
    
    
    
    
}
