/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.plot;

import org.dj.plot.api.PlotController;
import org.dj.plot.api.PlotService;
import org.dj.plot.api.PlotServiceProvider;
import org.dj.plot.api.Plottable;
import org.dj.service.api.ProxyServiceAbstr;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;

/**
 *
 * @author djabry
 */

@ServiceProvider(service = PlotService.class)
public class PlotServiceImpl extends ProxyServiceAbstr<PlotController,Plottable> implements PlotService{
    
    private static final Result<PlotServiceProvider> providers = Lookup.getDefault().lookupResult(PlotServiceProvider.class);
    
    public PlotServiceImpl(){        
        super();
        this.setCacheResults(true);
    }


    @Override
    public Result<? extends Provider<PlotController,Plottable>> getResult() {
        return providers;
    }


    
}
