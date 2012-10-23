/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.solarscape.render;

import org.dj.service.api.ServiceProviderAbstr;
import org.dj.solarscape.data.api.SolarPlant;
import org.dj.worldwindmodel.object.api.WWDJObject;
import org.dj.worldwindmodel.render.api.RenderableController;
import org.dj.worldwindmodel.render.api.RenderableServiceProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = RenderableServiceProvider.class)
public class SolarPlantRenderableProvider extends ServiceProviderAbstr<RenderableController,WWDJObject> implements RenderableServiceProvider{

    @Override
    public boolean filter(WWDJObject obj) {
        if(obj instanceof SolarPlant){
            
            return true;
        }
        
        return false;
    }

    @Override
    public RenderableController createObject(WWDJObject obj) {
        return new SolarPlantRController((SolarPlant)obj);
    }



   
    
}
