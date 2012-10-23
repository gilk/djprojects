/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import org.dj.domainmodel.api.DJObject;
import org.dj.service.api.ServiceProviderAbstr;
import org.lblrtm.lblrtmfilewriter.api.CalculationTemplate;
import org.lblrtm.lblrtmfilewriter.api.CalculationTemplateServiceProvider;
import org.lblrtm.lblrtmfilewriter.api.DJTAPE5;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = CalculationTemplateServiceProvider.class)
public class RadianceProvider extends ServiceProviderAbstr<CalculationTemplate,DJTAPE5> implements CalculationTemplateServiceProvider{

    @Override
    public boolean filter(DJTAPE5 obj) {
        return true;
    }

    @Override
    public CalculationTemplate createObject(DJTAPE5 obj) {
       RadianceImpl r =  new RadianceImpl();
       obj.setCalculationTemplate(r);
       return r;
    }

    @Override
    public String getTemplateName() {
        return CalculationTemplate.RADIANCE_CALCULATION;
    }

    @Override
    public String toString() {
        return getTemplateName();
    }
    
    

    
    
}
