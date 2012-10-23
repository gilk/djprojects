/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.host.icon;

import org.dj.domainmodel.api.DJObject;
import org.dj.icon.api.IconProvider;
import org.dj.icon.api.IconProviderAbstr;
import org.lblrtm.host.api.LBLRTMHostInfo;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */

@ServiceProvider (service = IconProvider.class)
public class HostInfoIconProvider extends IconProviderAbstr{
    
    public HostInfoIconProvider(){
        
        super("org/lblrtm/host/icon/server.png");
    }

    @Override
    public boolean filter(DJObject obj) {
        if(obj instanceof LBLRTMHostInfo){
            
            return true;
        }
        
        return false;
    }

   
}
