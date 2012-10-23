/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.globeviewer;

import org.dj.service.api.ProxyServiceAbstr;
import org.dj.worldwindmodel.globe.api.GlobeManager;
import org.dj.worldwindmodel.globeviewer.api.GlobeController;
import org.dj.worldwindmodel.globeviewer.api.GlobeService;
import org.dj.worldwindmodel.globeviewer.api.GlobeServiceProvider;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service=  GlobeService.class)
public class GlobeServiceImpl extends ProxyServiceAbstr<GlobeController,GlobeManager> implements GlobeService{

    public GlobeServiceImpl(){
        
        super();
        this.setCacheResults(true);;
    }
    
    @Override
    public Result<? extends Provider<GlobeController, GlobeManager>> getResult() {
        return Lookup.getDefault().lookupResult(GlobeServiceProvider.class);
    }
    
}
