/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import org.dj.service.api.ProxyServiceAbstr;
import org.lblrtm.lblrtmfilewriter.api.*;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = CalculationTemplateService.class)
public class CalculationTemplateServiceImpl extends RunTemplateServiceAbstr<CalculationTemplate> implements CalculationTemplateService {

    private static final Result<CalculationTemplateServiceProvider> result = Lookup.getDefault().lookupResult(CalculationTemplateServiceProvider.class);
    @Override
    public Result<? extends Provider<CalculationTemplate,DJTAPE5>> getResult() {
        return result;
    }
    
}
