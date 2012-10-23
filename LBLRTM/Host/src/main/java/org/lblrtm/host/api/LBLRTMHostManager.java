/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.host.api;

import java.io.File;
import java.util.Set;
import org.dj.db.api.DBObjectManager;
import org.dj.domainmodel.api.DJNodeObject;

/**
 *
 * @author djabry
 */
public interface LBLRTMHostManager extends DBObjectManager<LBLRTMHostInfo>, DJNodeObject.Source{
    
    
    
    Set<LBLRTMHostInfo> getHosts();
    
    void deleteHost(LBLRTMHostInfo hI);
    
    LBLRTMHostInfo createHost(
            boolean local,
            String name,
            File executable,
            File workingDir,
            String serverName,
            String userName,
            String password);

    LBLRTMHostInfo createHost();
    
}
