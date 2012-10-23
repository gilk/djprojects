/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.cookies;

import org.dj.cookies.api.CookieManager;
import org.dj.cookies.api.CookieManagerAbstr;
import org.dj.cookies.api.CookieServiceProvider;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObject;
import org.dj.service.api.ServiceProviderAbstr;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */

@ServiceProvider (service = CookieServiceProvider.class)
public class RemoveCookieService extends ServiceProviderAbstr<CookieManager,DJObject> implements CookieServiceProvider{

    @Override
    public boolean filter(DJObject obj) {
        if(obj instanceof DJNodeObject){
            
            return true;
            
        }
        return false;
    }

    @Override
    public CookieManager createObject(DJObject obj) {
        return new RemoveCookieManager((DJNodeObject)obj);
    }

    
    
    public class RemoveCookieManager extends CookieManagerAbstr {
        
        public RemoveCookieManager(DJNodeObject obj){
            
            super(obj, new RemoveCookieImpl(obj));
            
            
        }
        
    }
    
}
