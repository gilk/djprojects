/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.plot;

import org.dj.domainmodel.api.DJObject;
import org.dj.plot.api.PlotController;
import org.dj.plot.api.PlotServiceProvider;
import org.dj.plot.api.Plottable;
import org.dj.service.api.ServiceProviderAbstr;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider (service = PlotServiceProvider.class)
public class AreaLinePlotProvider extends ServiceProviderAbstr<PlotController,Plottable> implements PlotServiceProvider{

    @Override
    public boolean filter(Plottable p) {
        
            
            if(p.getDimensions().length==1||
                    p.getDimensions().length==2){
                
                return true;
            } 
        
        
        return false;
    }

    @Override
    public PlotController createObject(Plottable obj) {
        
        int numDims  =obj.getDimensions().length;
        
        if(numDims ==1||numDims ==2){
            return new AreaLinePlotController(obj);
        
        }
        
        return null;
       
    }
    
}
