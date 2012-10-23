/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.file;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lblrtm.file.api.TAPE6;
import org.lblrtm.file.api.TAPE6Manager;
import org.lblrtm.lblrtmdata.api.AtmosphericModel;
import org.openide.util.lookup.ServiceProvider;



/**
 *
 * @author djabry
 */
@ServiceProvider (service=TAPE6Manager.class)
public class TAPE6ManagerImpl implements TAPE6Manager{
    
     private final LoadingCache<File,TAPE6> cache = CacheBuilder.newBuilder().maximumSize(100).build(new CacheLoader<File, TAPE6>() {

            @Override
            public TAPE6 load(File f) throws Exception {
                return doLoad(f);
            }
        });
     
     private TAPE6 doLoad(File f) throws IOException{
         return new TAPE6Impl(f);
         
     }

    @Override
    public TAPE6 getTAPE6ForFile(File f) throws IOException{
        try {
            return cache.get(f);
        } catch (ExecutionException ex) {
            Logger.getLogger(TAPE6ManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("Failed to load TAPE6");
        }
        
    }

    @Override
    public void closeAll() {

        this.cache.invalidateAll();
    }

    @Override
    public void closeFile(File f) {

                cache.invalidate(f);


    }
    
}
