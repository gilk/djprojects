/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.globeviewer;

import org.dj.service.api.ServiceProviderAbstr;
import org.dj.worldwindmodel.globe.api.GlobeManager;
import org.dj.worldwindmodel.globeviewer.api.GlobeController;
import org.dj.worldwindmodel.globeviewer.api.GlobeServiceProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = GlobeServiceProvider.class)
public class GlobeServiceProviderImpl extends ServiceProviderAbstr<GlobeController,GlobeManager> implements GlobeServiceProvider{
    
    //private final Map<GlobeManager,GlobeEditorController> m = new FastMap<GlobeManager,GlobeEditorController>();
    
    public GlobeServiceProviderImpl(){
        

    }

    



    @Override
    public boolean filter(GlobeManager obj) {
        return true;
    }

    @Override
    public GlobeController createObject(GlobeManager obj) {
        return new GlobeEditorController(obj);
    }

    

   
    
}
