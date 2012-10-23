/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import org.dj.service.api.ServiceProviderAbstr;
import org.lblrtm.lblrtmfilewriter.api.DJTAPE5;
import org.lblrtm.lblrtmfilewriter.api.ScannerTemplate;
import org.lblrtm.lblrtmfilewriter.api.ScannerTemplateServiceProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = ScannerTemplateServiceProvider.class)
public class InterpolationScannerProvider extends ServiceProviderAbstr<ScannerTemplate,DJTAPE5> implements ScannerTemplateServiceProvider{

    @Override
    public boolean filter(DJTAPE5 obj) {
        if(obj instanceof DJTAPE5){
            
            return true;
        }
        
        return false;
    }

    @Override
    public ScannerTemplate createObject(DJTAPE5 obj) {
        InterpolationScanner iS =  new InterpolationScanner();
        obj.setScannerTemplate(iS);
        return iS;
         
    }

    @Override
    public String getTemplateName() {
        return ScannerTemplate.INTERPOLATION_SCANNER;
    }
    
    @Override
    public String toString() {
        return getTemplateName();
    }
    
}
