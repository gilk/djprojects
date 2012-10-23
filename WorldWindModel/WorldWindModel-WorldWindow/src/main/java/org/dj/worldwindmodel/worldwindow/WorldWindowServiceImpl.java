/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.worldwindow;

import gov.nasa.worldwind.WorldWindow;
import org.dj.service.api.ProxyServiceAbstr;
import org.dj.worldwindmodel.globe.api.GlobeManager;
import org.dj.worldwindmodel.worldwindow.api.WorldWindowService;
import org.dj.worldwindmodel.worldwindow.api.WorldWindowServiceProvider;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = WorldWindowService.class)
public class WorldWindowServiceImpl extends ProxyServiceAbstr<WorldWindow,GlobeManager> implements WorldWindowService{
    
    public WorldWindowServiceImpl(){
        super();
        this.setCacheResults(true);
    }

    @Override
    public Result<? extends Provider<WorldWindow, GlobeManager>> getResult() {
       return Lookup.getDefault().lookupResult(WorldWindowServiceProvider.class);
    }
    
}
