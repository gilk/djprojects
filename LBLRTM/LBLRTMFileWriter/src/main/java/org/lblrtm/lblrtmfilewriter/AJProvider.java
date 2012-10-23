/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import org.dj.domainmodel.api.DJObject;
import org.dj.service.api.ServiceProviderAbstr;
import org.lblrtm.lblrtmfilewriter.api.AnalyticJacobian;
import org.lblrtm.lblrtmfilewriter.api.CalculationTemplate;
import org.lblrtm.lblrtmfilewriter.api.CalculationTemplateServiceProvider;
import org.lblrtm.lblrtmfilewriter.api.DJTAPE5;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = CalculationTemplateServiceProvider.class)
public class AJProvider extends ServiceProviderAbstr<CalculationTemplate,DJTAPE5> implements CalculationTemplateServiceProvider{

    @Override
    public boolean filter(DJTAPE5 obj) {
        return true;
    }

    @Override
    public CalculationTemplate createObject(DJTAPE5 obj) {
        AnalyticJacobian aJ =  new AnalyticalJacobianImpl();
        obj.setCalculationTemplate(aJ);
        return aJ;
    }

    @Override
    public String getTemplateName() {
        return CalculationTemplate.AJ_CALCULATION;
    }

    @Override
    public String toString() {
        return getTemplateName();
    }
    
    
}
