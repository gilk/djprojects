/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javolution.util.FastSet;
import org.dj.service.api.ProxyServiceAbstr;
import org.dj.service.api.Service;
import org.openide.util.Lookup.Result;

/**
 *
 * @author djabry
 */
public abstract class RunTemplateServiceAbstr<R extends RunTemplate> extends ProxyServiceAbstr<R,DJTAPE5> implements RunTemplateService<R>{

    public RunTemplateServiceAbstr(){
        this.setCacheResults(true);
    }

    @Override
    public Set<Provider<R,DJTAPE5>> getAllProviders() {
        Set<Provider<R,DJTAPE5>> providers = new FastSet<Provider<R,DJTAPE5>>();
        providers.addAll(getResult().allInstances());
        return providers;
        
    }
    
    public Set<Service<R,DJTAPE5>> getAllServices(DJTAPE5 t5){
        
        Set<Service<R,DJTAPE5>> services = new FastSet<Service<R,DJTAPE5>>();
        Iterator<Provider<R, DJTAPE5>> iterator = getAllProviders().iterator();
        while(iterator.hasNext()){
            Provider<R, DJTAPE5> next = iterator.next();
            services.add(next.getService());
        }
        
        return services;
        
    }

    @Override
    public Provider<R,DJTAPE5> getProviderForName(String templateName) {
        Iterator<Provider<R,DJTAPE5>> iterator = getAllProviders().iterator();
        while(iterator.hasNext()){
            Provider<R,DJTAPE5> next = iterator.next();
            if(next instanceof RunTemplateServiceProvider){
                RunTemplateServiceProvider rP = (RunTemplateServiceProvider) next;
                if(rP.getTemplateName().equals(templateName)){
                    
                    return rP;
                }
                
            }
        }
        
        return null;
    }

    @Override
    public R createTemplateForName(DJTAPE5 t5, String templateName) {
        return getServiceForName(t5,templateName).createObject(t5);
    }

    @Override
    public Service<R, DJTAPE5> getServiceForName(DJTAPE5 t5, String name) {
        return getProviderForName(name).getService();
    }
 
    
}
