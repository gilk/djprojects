/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import org.lblrtm.lblrtmfilewriter.api.*;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service= ScannerTemplateService.class)
public class ScannerTemplateServiceImpl extends RunTemplateServiceAbstr<ScannerTemplate> implements ScannerTemplateService{

    private static final Result<ScannerTemplateServiceProvider> result = Lookup.getDefault().lookupResult(ScannerTemplateServiceProvider.class);
    @Override
    public Result<? extends Provider<ScannerTemplate,DJTAPE5>> getResult() {
        return result; 
    }
    
    

    
    
}
