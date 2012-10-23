/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.icon;

import org.dj.domainmodel.api.DJObject;
import org.dj.icon.api.IconProvider;
import org.dj.icon.api.IconProviderAbstr;
import org.lblrtm.task.api.LBLRTMParallelTask;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider (service= IconProvider.class)
public class TaskIconProvider extends IconProviderAbstr{
    
    public TaskIconProvider(){
        
        super("org/lblrtm/task/icon/task.png");
    }

    @Override
    public boolean filter(DJObject obj) {
        if(obj instanceof LBLRTMParallelTask){
            
            return true;
        }
        
        return false;
    }
    
}
