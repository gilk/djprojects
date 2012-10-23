/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import org.dj.domainmodel.api.DJObject;
import org.dj.service.api.ServiceProviderAbstr;
import org.lblrtm.lblrtmfilewriter.api.DJTAPE5;
import org.lblrtm.lblrtmfilewriter.api.FFTScanner;
import org.lblrtm.lblrtmfilewriter.api.ScannerTemplate;
import org.lblrtm.lblrtmfilewriter.api.ScannerTemplateServiceProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = ScannerTemplateServiceProvider.class)
public class FFTScannerProvider extends ServiceProviderAbstr<ScannerTemplate,DJTAPE5> implements ScannerTemplateServiceProvider{

    @Override
    public boolean filter(DJTAPE5 obj) {
       return true;
    }

    @Override
    public ScannerTemplate createObject(DJTAPE5 obj) {
        FFTScanner sc=  new FFTScannerImpl();
        obj.setScannerTemplate(sc);
        return sc;
    }

    @Override
    public String getTemplateName() {
        return ScannerTemplate.FFT_SCANNER;
    }
    
    @Override
    public String toString() {
        return getTemplateName();
    }
    
}
