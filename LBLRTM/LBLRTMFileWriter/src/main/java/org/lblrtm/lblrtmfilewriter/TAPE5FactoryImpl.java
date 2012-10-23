/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;


import org.lblrtm.lblrtmdata.api.Observer;
import org.lblrtm.lblrtmdata.api.Surface;
import org.lblrtm.lblrtmdata.api.TAPE5;
import org.lblrtm.lblrtmfilewriter.api.*;
import org.lblrtm.profile.api.Profile;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider (service = TAPE5Factory.class)
public class TAPE5FactoryImpl implements TAPE5Factory{
    
    private static final ScannerTemplateService sTS = Lookup.getDefault().lookup(ScannerTemplateService.class);
    private static final CalculationTemplateService cTS = Lookup.getDefault().lookup(CalculationTemplateService.class);
    
    @Override
    public TAPE5 createTAPE5(
    Profile profile, 
    Surface surface, 
    Observer observer,
    double v1,
    double v2,
    double dv) {
        
        
        DJTAPE5 t5= new TAPE5Impl(v1,v2,dv,
           profile, generateRunString(v1,v2), surface,
            observer);
        
        cTS.createTemplateForName(t5, CalculationTemplate.RADIANCE_CALCULATION);
        sTS.createTemplateForName(t5, ScannerTemplate.INTERPOLATION_SCANNER);
 
        return t5;
    }
    
    
    private String generateHMOD(){
        
        return "Test atmospheric profile";
    }
    
    private String generateRunString(double v1, double v2){
        
        return "test";
    }
    
   
   
    
}
