/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.cookies;

import java.util.Set;
import org.dj.cookies.api.CookieManager;
import org.dj.cookies.api.CookieService;
import org.dj.cookies.api.CookieServiceProvider;
import org.dj.cookies.api.ProxyCookieManager;
import org.dj.domainmodel.api.DJObject;
import org.dj.service.api.MultiServiceAbstr;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = CookieService.class)
public class CookieServiceImpl extends MultiServiceAbstr<CookieManager,DJObject> implements CookieService {

    private static final Result<CookieServiceProvider> providers = Lookup.getDefault().lookupResult(CookieServiceProvider.class);
    

    public CookieServiceImpl(){

        super();
        this.setCacheResults(true);
    }

    @Override
    public Result<? extends Provider<CookieManager,DJObject>> getResult() {
        return providers;
    }

    @Override
    public CookieManager createObject(Set<CookieManager> c) {
        return new ProxyCookieManager(c);
    }

    
    

    
}
