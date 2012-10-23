/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.solarscape.globe;

import org.dj.solarscape.data.api.SolarPlantManager;
import org.dj.worldwindmodel.globe.api.GlobeManagerAbstr;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */

@ServiceProvider(service = SolarPlantGlobe.class)
public class SolarPlantGlobe extends GlobeManagerAbstr{
    private static final SolarPlantManager sPM = Lookup.getDefault().lookup(SolarPlantManager.class);
    public SolarPlantGlobe(){
        this.attachSource(sPM);
    }
    
}
