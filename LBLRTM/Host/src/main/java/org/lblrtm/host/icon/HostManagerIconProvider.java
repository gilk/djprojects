/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.host.icon;

import org.dj.domainmodel.api.DJObject;
import org.dj.icon.api.IconProvider;
import org.dj.icon.api.IconProviderAbstr;
import org.lblrtm.host.api.LBLRTMHostManager;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider (service = IconProvider.class)
public class HostManagerIconProvider extends IconProviderAbstr{

   public HostManagerIconProvider(){
       
       super("org/lblrtm/host/icon/folder.png");
   }

    @Override
    public boolean filter(DJObject obj) {
        if(obj instanceof LBLRTMHostManager){
            
            return true;
        }
        
        return false;
    }
    
}
