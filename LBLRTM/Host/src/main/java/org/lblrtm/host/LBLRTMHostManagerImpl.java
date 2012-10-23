/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.host;

import org.lblrtm.host.api.LBLRTMHostInfoImpl;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import javolution.util.FastSet;
import org.dj.db.api.DBObjectManagerAbstr;
import org.dj.domainmodel.api.DJNodeObject;
import org.lblrtm.host.api.LBLRTMHostInfo;
import org.lblrtm.host.api.LBLRTMHostManager;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = LBLRTMHostManager.class)
//@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
public class LBLRTMHostManagerImpl extends DBObjectManagerAbstr<LBLRTMHostInfo> implements LBLRTMHostManager {
    
   
    
    public LBLRTMHostManagerImpl(){
        
        super(); 
        
    }


    private String generateDefaultName(){
        
        int i=0;
        String n = "Local host";
        
        while(nameExists(n)){
            i++;
            n = "Local host "+i;
        }
        
        return n;
        
    }
    
    private boolean nameExists(String name){
        
        Iterator<DJNodeObject> iterator = this.getChildren().iterator();
        
        while(iterator.hasNext()){
            DJNodeObject next = iterator.next();
            if(next.getName().equals(name)){
                
                return true;
            }
            
        }
        
        return false;
    }
//
    @Override
    public LBLRTMHostInfo createHost() {
        return createHost(
                true,
                generateDefaultName(),
                new File("/Applications/lblrtm/lblrtm_dbl"),
                new File("/Users/lblrtm_working_dir"),
                "",
                "",
                "");
    }
//    

    @Override
    public Set<LBLRTMHostInfo> getHosts() {
        Set<LBLRTMHostInfo> hosts = new FastSet<LBLRTMHostInfo>();
        
        for (DJNodeObject child : getChildren()) {
            
            if (child instanceof LBLRTMHostInfo) {
                LBLRTMHostInfo lblrtmHostInfo = (LBLRTMHostInfo) child;
                
                hosts.add(lblrtmHostInfo);
            }
            
        }
        
        return hosts;
    }
//    

    @Override
    public void deleteHost(LBLRTMHostInfo hI) {
        
        if (hI instanceof DJNodeObject) {
            DJNodeObject djNodeObject = (DJNodeObject) hI;
            
            this.removeChild(djNodeObject);
            
        }
        
        
        
    }
    
    
    @Override
    public LBLRTMHostInfo createHost(boolean local,
            String name,
            File executable,
            File workingDir,
            String serverName,
            String userName,
            String password) {
        
        LBLRTMHostInfoImpl hI = new LBLRTMHostInfoImpl();
        hI.setLocal(local);
        hI.setName(name);
        hI.setExecutable(executable);
        hI.setWorkingDir(workingDir);
        hI.setServerName(serverName);
        hI.setUserName(userName);
        hI.setPassword(password);
        
        this.addChild(hI);
        
        
        return hI;
    }

    @Override
    public DJNodeObject createObject() {
        return (DJNodeObject)this.createHost();
    }


}
