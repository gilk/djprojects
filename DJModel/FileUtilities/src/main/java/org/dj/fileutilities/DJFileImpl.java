/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.fileutilities;

import java.io.File;
import org.dj.fileutilities.api.DJFile;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public class DJFileImpl implements DJFile{
    
    private final File file;
    private final HostInfo hI;
    
    @Override
    public String getPath(){
        
        String path = file.getPath();
        
        if (System.getProperty("os.name").toLowerCase().contains("win") 
                &&!hI.isLocal()){
            
            String backSlash = File.separator;

            path = path.replace(backSlash,"/" );
        }
        
        return path;

    }
    
    public DJFileImpl(File file){
        
        this(file,HostInfo.LOCAL_HOST);
    }
    
    public DJFileImpl(File file, HostInfo hI){
        this.file=file;
        this.hI=hI;
        
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public HostInfo getHostInfo() {
        return hI;
    }

    @Override
    public boolean equals(Object o) {
        
        if(o instanceof DJFile){
            DJFile f = (DJFile) o;
            
            if(f.getFile().getPath().equals(this.getFile().getPath())
                    
                    &&f.getHostInfo().getServerName().equals(this.getHostInfo().getServerName())){
                
                return true;
            }
            
            return false;
            
        }
        
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return this.file.getName().hashCode()+this.hI.getServerName().hashCode();
    }
    
    
    
    
    

   
}
