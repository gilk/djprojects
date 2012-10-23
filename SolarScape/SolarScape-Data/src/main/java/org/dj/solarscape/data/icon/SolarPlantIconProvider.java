/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.solarscape.data.icon;

import org.dj.domainmodel.api.DJObject;
import org.dj.icon.api.IconProvider;
import org.dj.icon.api.IconProviderAbstr;
import org.dj.solarscape.data.api.SolarPlant;
import org.dj.solarscape.data.api.SolarPlantManager;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = IconProvider.class)
public class SolarPlantIconProvider extends IconProviderAbstr{
    
    public SolarPlantIconProvider(){
        
        super("org/dj/solarscape/data/icon/solar.png");
    }

    @Override
    public boolean filter(DJObject obj) {
       
        if(obj instanceof SolarPlant||obj instanceof SolarPlantManager){
            
            return true;
            
        }
        
        return false;
    }
    
}
