/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import java.util.Collection;
import java.util.Set;
import org.dj.service.api.Service;

/**
 *
 * @author djabry
 */
public interface RunTemplateService<R extends RunTemplate> extends Service<R,DJTAPE5>{
    
    Set<Provider<R,DJTAPE5>> getAllProviders();
    Provider<R,DJTAPE5> getProviderForName(String templateName);
    R createTemplateForName(DJTAPE5 t5,String templateName);
    Service<R,DJTAPE5> getServiceForName(DJTAPE5 t5,String name);
    Set<Service<R,DJTAPE5>> getAllServices(DJTAPE5 t5);
    
    
    
}
